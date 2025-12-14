use crate::common::{InputType, read_lines};
use std::collections::{HashMap, HashSet};

const DAY: usize = 7;

pub fn part_one(input_type: &InputType) {
    let grid = read_lines(DAY, input_type)
        .iter()
        .map(|s| s.chars().collect::<Vec<_>>())
        .collect::<Vec<_>>();
    let start = grid[0].iter().position(|c| *c == 'S').unwrap();

    let mut beams = HashSet::new();
    beams.insert(start);

    let mut splits = 0;
    for i in 1..grid.len() {
        let row = &grid[i];
        for beam in beams.clone() {
            if row[beam] == '^' {
                beams.remove(&beam);
                beams.insert(beam - 1);
                beams.insert(beam + 1);
                splits += 1;
            }
        }
    }

    println!("{}", splits);
}

pub fn part_two(input_type: &InputType) {
    let grid = read_lines(DAY, input_type)
        .iter()
        .map(|s| s.chars().collect::<Vec<_>>())
        .collect::<Vec<_>>();
    let start = grid[0].iter().position(|c| *c == 'S').unwrap();

    let mut memo = HashMap::new();
    println!("{}", timelines(&mut memo, &grid, 2, start));
}

fn timelines(
    memo: &mut HashMap<(usize, usize), i64>,
    grid: &Vec<Vec<char>>,
    row: usize,
    col: usize,
) -> i64 {
    if let Some(tl) = memo.get(&(row, col)) {
        return *tl;
    }

    let mut left_timelines = 1;
    for i in row + 1..grid.len() {
        if grid[i][col - 1] == '^' {
            left_timelines = timelines(memo, grid, i, col - 1);
            break;
        }
    }
    let mut right_timelines = 1;
    for i in row + 1..grid.len() {
        if grid[i][col + 1] == '^' {
            right_timelines = timelines(memo, grid, i, col + 1);
            break;
        }
    }
    let timelines = left_timelines + right_timelines;
    memo.insert((row, col), timelines);
    timelines
}
