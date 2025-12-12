use crate::common::{DisjointRangeSet, InputType, Range, read_lines};
use std::cmp::{max, min};
use std::collections::{HashMap, HashSet};

const DAY: usize = 9;

pub fn part_one(input_type: &InputType) {
    let tiles = read_lines(DAY, input_type)
        .iter()
        .map(|s| {
            let (l, r) = s.split_once(",").unwrap();
            (l.parse::<i64>().unwrap(), r.parse::<i64>().unwrap())
        })
        .collect::<Vec<_>>();

    let mut max_area = 0;
    for i in 0..tiles.len() {
        let tile_a = tiles[i];
        for j in i + 1..tiles.len() {
            let tile_b = tiles[j];
            let width = (tile_a.0 - tile_b.0).abs() + 1;
            let height = (tile_a.1 - tile_b.1).abs() + 1;
            max_area = max(max_area, width * height);
        }
    }

    println!("{}", max_area)
}

pub fn part_two(input_type: &InputType) {
    let tiles = read_lines(DAY, input_type)
        .iter()
        .map(|s| {
            let (l, r) = s.split_once(",").unwrap();
            (l.parse::<i64>().unwrap(), r.parse::<i64>().unwrap())
        })
        .collect::<Vec<_>>();

    let mut max_area = 0;
    for i in 0..tiles.len() {
        let tile_a = tiles[i];
        for j in i + 1..tiles.len() {
            let tile_b = tiles[j];
            let min_x = min(tile_a.0, tile_b.0);
            let max_x = max(tile_a.0, tile_b.0);
            let min_y = min(tile_a.1, tile_b.1);
            let max_y = max(tile_a.1, tile_b.1);
            let x_range = Range::new(min_x, max_x);
            let y_range = Range::new(min_y, max_y);
            let width = max_x - min_x + 1;
            let height = max_y - min_y + 1;
            let area = width * height;
            if area <= max_area {
                continue;
            }

            let mut left = DisjointRangeSet::new();
            let mut right = DisjointRangeSet::new();
            let mut top = DisjointRangeSet::new();
            let mut bottom = DisjointRangeSet::new();
            for k in 0..tiles.len() {
                let corner1 = tiles[k];
                let corner2 = tiles[(k + 1) % tiles.len()];

                if corner1.0 == corner2.0 {
                    //vertical edge
                    if corner1.0 <= min_x {
                        let edge_range = Range::from_unordered(corner1.1, corner2.1);
                        if let Some(intersect) = edge_range.intersection(&y_range) {
                            left.add_range(intersect);
                        }
                    } else if corner1.0 >= max_x {
                        let edge_range = Range::from_unordered(corner1.1, corner2.1);
                        if let Some(intersect) = edge_range.intersection(&y_range) {
                            right.add_range(intersect);
                        }
                    }
                } else if corner1.1 == corner2.1 {
                    // horizontal edge
                    if corner1.1 <= min_y {
                        let edge_range = Range::from_unordered(corner1.0, corner2.0);
                        if let Some(intersect) = edge_range.intersection(&x_range) {
                            top.add_range(intersect);
                        }
                    } else if corner1.1 >= max_y {
                        let edge_range = Range::from_unordered(corner1.0, corner2.0);
                        if let Some(intersect) = edge_range.intersection(&x_range) {
                            bottom.add_range(intersect);
                        }
                    }
                } else {
                    panic!("Expected all corners to be adjacent")
                }
            }
            if left.len() == 1 && left.min() ==min_y && left.max() == max_y
                && right.len() == 1 && right.min() ==min_y && right.max() == max_y
                && top.len() == 1 && top.min() ==min_x && top.max() == max_x
                && bottom.len() == 1 && bottom.min() ==min_x && bottom.max() == max_x {
                max_area = area;
            }
        }
    }

    println!("{}", max_area)
}
