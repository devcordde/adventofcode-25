use std::borrow::Borrow;
use std::cmp::{max, min};
use std::collections::HashMap;
use std::fs::{File, read_to_string};
use std::hash::Hash;
use std::io::BufRead;
use std::sync::Arc;

pub enum InputType {
    Example,
    Real,
}

impl InputType {
    fn get_file_name(&self) -> &'static str {
        match self {
            InputType::Example => "example",
            InputType::Real => "input",
        }
    }
}

fn get_path(day: usize, input_type: &InputType) -> String {
    format!(
        "./{}-{}.txt",
        input_type.get_file_name(),
        left_pad_zero(day.to_string().as_str(), 2)
    )
}

pub fn read_input(day: usize, input_type: &InputType) -> String {
    read_to_string(get_path(day, input_type))
        .unwrap()
        .as_str()
        .trim()
        .to_string()
}

pub fn read_lines(day: usize, input_type: &InputType) -> Vec<String> {
    let lines = read_to_string(get_path(day, input_type)).unwrap();

    let mut output = Vec::new();
    for line in lines.lines() {
        output.push(line.to_string());
    }
    output
}

fn left_pad_zero(str: &str, length: usize) -> String {
    left_pad(str, '0', length)
}

fn left_pad(str: &str, c: char, length: usize) -> String {
    if str.len() < length {
        String::from(c).as_str().repeat(length - str.len()) + str
    } else {
        str.to_string()
    }
}

#[derive(Debug, Clone)]
pub struct Range {
    pub l: i64,
    pub h: i64,
}

impl Range {
    pub fn new(l: i64, h: i64) -> Self {
        Self { l, h }
    }

    pub fn from_unordered(l: i64, h: i64) -> Self {
        Self {
            l: min(l, h),
            h: max(l, h),
        }
    }
    pub fn len(&self) -> i64 {
        self.h - self.l + 1
    }

    pub fn intersect(&self, other: &Range) -> i64 {
        self.intersection(other).map(|r| r.len()).unwrap_or(0)
    }

    pub fn intersection(&self, other: &Range) -> Option<Range> {
        let l = max(self.l, other.l);
        let h = min(self.h, other.h);
        if l > h { None } else { Some(Range { l, h }) }
    }
}

pub struct DisjointRangeSet {
    ranges: Vec<Range>,
}

impl DisjointRangeSet {
    pub fn new() -> Self {
        Self { ranges: Vec::new() }
    }

    pub fn add_range(&mut self, range: Range) {
        if self.ranges.is_empty() {
            self.ranges.push(range);
            return;
        }

        let mut intersecting: Vec<Range> = self
            .ranges
            .iter()
            .filter(|r| r.intersection(&range).is_some())
            .map(|r| r.clone())
            .collect();
        intersecting.push(range.clone());

        let l = intersecting.iter().map(|r| r.l).min().unwrap();
        let h = intersecting.iter().map(|r| r.h).max().unwrap();
        let union = Range::new(l, h);

        self.ranges.retain(|r| r.intersection(&range).is_none());
        self.ranges.push(union);
    }

    pub fn len(&self) -> usize {
        self.ranges.len()
    }

    pub fn min(&self) -> i64 {
        self.ranges.iter().map(|r| r.l).min().unwrap()
    }

    pub fn max(&self) -> i64 {
        self.ranges.iter().map(|r| r.h).max().unwrap()
    }
}

type NodeIndex = usize;
type EdgeIndex = usize;

pub struct Graph<T: Eq + Hash> {
    node_values: Vec<Arc<T>>,
    node_index: HashMap<Arc<T>, NodeIndex>,

    nodes: Vec<NodeData>,
    edges: Vec<EdgeData>,
}

struct NodeData {
    first_edge: Option<EdgeIndex>,
}

struct EdgeData {
    target_node: NodeIndex,
    next_edge: Option<EdgeIndex>,
}

impl<T: Eq + Hash> Graph<T> {
    pub fn new() -> Self {
        Self {
            node_index: HashMap::new(),
            node_values: Vec::new(),

            nodes: Vec::new(),
            edges: Vec::new(),
        }
    }

    pub fn add_node(&mut self, node: T) {
        assert_eq!(self.nodes.len(), self.node_values.len());

        let index = self.nodes.len();
        self.nodes.push(NodeData { first_edge: None });
        let arc = Arc::new(node);
        self.node_values.push(arc.clone());
        self.node_index.insert(arc, index);
    }

    pub fn add_edge(&mut self, start: &T, end: &T) {
        let start_node_idx = *self.node_index.get(start).unwrap();
        let start_node = &mut self.nodes[start_node_idx];
        let end_node_idx = *self.node_index.get(end).unwrap();

        let edge_index = self.edges.len();
        self.edges.push(EdgeData {
            target_node: end_node_idx,
            next_edge: start_node.first_edge,
        });
        start_node.first_edge = Some(edge_index)
    }

    pub fn successors(&'_ self, node: &T) -> Successors<'_, T> {
        let node_idx = *self.node_index.get(node).unwrap();
        let first_edge = self.nodes[node_idx].first_edge;
        Successors {
            graph: self,
            current_edge_idx: first_edge,
        }
    }
}

pub struct Successors<'g, T: Eq + Hash> {
    graph: &'g Graph<T>,
    current_edge_idx: Option<EdgeIndex>,
}

impl<'g, T: Eq + Hash> Iterator for Successors<'g, T> {
    type Item = &'g T;

    fn next(&mut self) -> Option<Self::Item> {
        match self.current_edge_idx {
            None => None,
            Some(idx) => {
                let data = &self.graph.edges[idx];
                self.current_edge_idx = data.next_edge;

                let node = &self.graph.node_values[data.target_node];
                Some(node.as_ref())
            }
        }
    }
}

#[cfg(test)]
mod test {
    use crate::common::{Graph, left_pad, left_pad_zero};

    #[test]
    fn test_left_pad() {
        assert_eq!("1", left_pad_zero("1", 1));
        assert_eq!("01", left_pad_zero("1", 2));
        assert_eq!("001", left_pad_zero("1", 3));
        assert_eq!("100", left_pad_zero("100", 3));
        assert_eq!("1000", left_pad_zero("1000", 3));
    }

    #[test]
    fn test_graph() {
        let mut graph = Graph::new();
        graph.add_node("a".to_string());
        graph.add_node("b".to_string());
        graph.add_node("c".to_string());
        graph.add_node("d".to_string());

        graph.add_edge(&"a".into(), &"b".into());
        graph.add_edge(&"a".into(), &"c".into());
        graph.add_edge(&"a".into(), &"d".into());

        graph.add_edge(&"b".into(), &"c".into());

        assert_eq!(
            vec![&"d", &"c", &"b"],
            graph.successors(&"a".into()).collect::<Vec<_>>()
        );
        assert_eq!(
            vec![&"c"],
            graph.successors(&"b".into()).collect::<Vec<_>>()
        );
        let empty: Vec<&String> = Vec::new();
        assert_eq!(
            empty,
            graph.successors(&"c".into()).collect::<Vec<&String>>()
        );
    }
}
