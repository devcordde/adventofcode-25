// https://github.com/louboecker/aoc-2025/blob/main/src/bin/day02.rs

pub fn main() {
    let input = include_str!("day02.txt");

    let mut part1 = 0;
    let mut part2 = 0;

    for (one, two) in input.split(",").map(|range| {
        let (one, two) = range.split_once("-").unwrap();
        (one.parse::<u64>().unwrap(), two.parse::<u64>().unwrap())
    }) {
        for id in one..=two {
            let number = id.to_string();
            for i in 2..=number.len() {
                if number.len() % i != 0 {
                    continue;
                }
                let parts = number
                    .as_bytes()
                    .chunks(number.len() / i)
                    .map(str::from_utf8)
                    .collect::<Result<Vec<&str>, _>>()
                    .unwrap();

                let first = &parts[0];
                if parts.iter().all(|item| item == first) {
                    if i == 2 {
                        part1 += id;
                    }
                    part2 += id;
                    break;
                }
            }
        }
    }

    println!("Part 1: {part1}");
    println!("Part 2: {part2}");
}
