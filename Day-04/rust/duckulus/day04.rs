use crate::common::{InputType, read_input};

const DAY: usize = 4;

pub fn part_one(input_type: &InputType) {
    let mut grid = get_grid(input_type);
    let value = remove_unaccessible(&mut grid);
    println!("{}", value)
}

pub fn part_two(input_type: &InputType) {
    let mut grid = get_grid(input_type);
    let mut sum = 0;
    let mut value = 0;
    value = remove_unaccessible(&mut grid);
    while value > 0 {
       sum += value;
        value = remove_unaccessible(&mut grid);
    }
    println!("{}", sum)
}

fn remove_unaccessible(grid: &mut Vec<Vec<Cell>>) -> usize {
    let mut remove: Vec<(usize, usize)> = Vec::new();

    let rows = grid.len() as i32;
    for y in 0..rows {
        let cols = grid[y as usize].len() as i32;
        for x in 0..cols {
            if grid[y as usize][x as usize] != Cell::Roll {
                continue;
            }
            let mut neighbors = 0;
            for dy in -1..=1 {
                let ny = (y) + dy;
                if (ny < 0 || ny >= rows) {
                    continue;
                }
                for dx in -1..=1 {
                    if (dx == 0 && dy == 0) {
                        continue;
                    }
                    let nx = (x) + dx;
                    if (nx < 0 || nx >= cols) {
                        continue;
                    }
                    if grid[ny as usize][nx as usize] == Cell::Roll {
                        neighbors += 1;
                    }
                }
            }
            if neighbors < 4 {
                remove.push((x as usize, y as usize));
            }
        }
    }

    for (x, y) in remove.iter() {
        grid[*y][*x] = Cell::Empty;
    }

    remove.len()
}

#[derive(PartialEq)]
enum Cell {
    Roll,
    Empty,
}

fn to_cell(c: char) -> Cell {
    match c {
        '.' => Cell::Empty,
        '@' => Cell::Roll,
        _ => panic!("Found invalid cell: {}", c),
    }
}

fn get_grid(input_type: &InputType) -> Vec<Vec<Cell>> {
    read_input(DAY, input_type)
        .as_str()
        .trim()
        .lines()
        .map(|line| line.trim().chars().map(to_cell).collect())
        .collect()
}
