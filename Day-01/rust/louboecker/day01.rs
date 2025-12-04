// https://github.com/louboecker/aoc-2025/blob/main/src/bin/day01.rs

pub fn main() {
    let input = include_str!("day01.txt");

    let (_, part1, part2) = input
        .lines()
        .fold((50, 0, 0), |(running, part1, part2), line| {
            let (instr, number) = line.split_at(1);
            let number = number.parse::<i32>().unwrap();

            let instr = if instr == "L" { -number } else { number };

            let new = running + instr;
            let new_running = new.rem_euclid(100);

            let p1 = if new_running == 0 { 1 } else { 0 };
            let p2 = if instr > 0 {
                ((running + 1)..=new).filter(|&x| x % 100 == 0).count() as i32
            } else {
                (new..running).filter(|&x| x % 100 == 0).count() as i32
            };

            (new_running, part1 + p1, part2 + p2)
        });

    println!("Part 1: {part1}, Part 2: {part2}");
}
