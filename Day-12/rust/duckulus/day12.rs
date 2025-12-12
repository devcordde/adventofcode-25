use crate::common::{Graph, InputType, read_input, read_lines};
use std::arch::x86_64::_mm256_lddqu_si256;
use std::collections::HashMap;

const DAY: usize = 12;

#[derive(Debug)]
struct Region {
    width: i64,
    height: i64,
    quantities: Vec<i64>,
}

pub fn part_one(input_type: &InputType) {
    let (shapes, regions) = parse(input_type);
    let mut possible_regions = 0;
    for region in &regions {
        let mut used_area = 0;
        for i in 0..shapes.len() {
            let shape_size = shapes[i].iter().filter(|b| **b).count() as i64;
            used_area += region.quantities[i] * shape_size;
        }
        let available_area = region.width * region.height;
        if used_area <= available_area {
            possible_regions += 1;
        }
    }

    println!("Regions possible {}/{}", possible_regions, regions.len());
}

pub fn part_two(input_type: &InputType) {}

fn parse(input_type: &InputType) -> (Vec<[bool; 9]>, Vec<Region>) {
    let input = read_input(DAY, input_type);
    let mut split = input.trim().split("\n\n").collect::<Vec<_>>();

    let regions = split
        .pop()
        .unwrap()
        .split("\n")
        .map(|line| {
            let (left, right) = line.trim().split_once(": ").unwrap();
            let (w, h) = left.split_once("x").unwrap();
            let width = w.parse::<i64>().unwrap();
            let height = h.parse::<i64>().unwrap();
            let quantities = right
                .trim()
                .split(" ")
                .map(|s| s.parse::<i64>().unwrap())
                .collect();
            Region {
                width,
                height,
                quantities,
            }
        })
        .collect::<Vec<_>>();
    let mut shapes: Vec<[bool; 9]> = Vec::new();
    for s in split {
        let mut shape = [false; 9];
        let lines = s.trim().split("\n").skip(1).collect::<Vec<_>>();
        let mut i = 0;
        for line in lines {
            for c in line.trim().chars() {
                if c == '#' {
                    shape[i] = true;
                } else if c != '.' {
                    panic!("Unexpected shape char {}", c);
                }
                i += 1;
            }
        }
        shapes.push(shape);
    }
    (shapes, regions)
}
