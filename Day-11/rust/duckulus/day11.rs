use crate::common::{Graph, InputType, read_lines};
use std::collections::HashMap;

const DAY: usize = 11;

fn build_graph(input_type: &InputType) -> Graph<String> {
    let lines = read_lines(DAY, input_type);
    let mut graph = Graph::new();
    for line in &lines {
        let (source, _) = line.trim().split_once(": ").unwrap();
        graph.add_node(source.to_string())
    }
    graph.add_node("out".to_string());
    for line in &lines {
        let (source, dest) = line.trim().split_once(": ").unwrap();
        let target_nodes = dest.split(" ").collect::<Vec<_>>();
        for target in target_nodes.iter().rev() {
            graph.add_edge(&source.to_string(), &target.to_string());
        }
    }
    graph
}

pub fn part_one(input_type: &InputType) {
    let graph = build_graph(input_type);
    println!("{}", count_paths_one(&graph, "you", "out"))
}

fn count_paths_one(graph: &Graph<String>, src: &str, dest: &str) -> i64 {
    if (src == dest) {
        return 1;
    }
    let mut sum = 0;
    for succ in graph.successors(&src.to_string()) {
        sum += count_paths_one(graph, succ.as_str(), dest);
    }
    sum
}

pub fn part_two(input_type: &InputType) {
    let graph = build_graph(input_type);
    let mut cache = HashMap::new();
    let mut path = Vec::new();
    println!(
        "{}",
        count_paths_two(&mut cache, &graph, "svr", "out", &mut path)
    )
}

fn count_paths_two(
    cache: &mut HashMap<(String, (bool, bool)), i64>,
    graph: &Graph<String>,
    src: &str,
    dest: &str,
    path: &mut Vec<String>,
) -> i64 {
    let b1 = path.contains(&"dac".to_string());
    let b2 = path.contains(&"fft".to_string());
    if (src == dest) {
        return if b1 && b2 { 1 } else { 0 };
    }
    let key = (src.to_string(), (b1, b2));
    if let Some(paths) = cache.get(&key) {
        return *paths;
    }
    let mut sum = 0;
    path.push(src.to_string());
    for succ in graph.successors(&src.to_string()) {
        sum += count_paths_two(cache, graph, succ.as_str(), dest, path);
    }
    assert_eq!(src, path.pop().unwrap());
    cache.insert(key , sum);
    sum
}
