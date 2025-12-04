// https://github.com/louboecker/aoc-2025/blob/main/src/bin/day03.rs

pub fn main() {
    let input = include_str!("day03.txt");

    let line_len = input.lines().next().unwrap().len();
    let (part1, part2) = input.lines().fold((0, 0), |(part1, part2), line| {
        (
            part1 + most_joltage(line, 2, line_len),
            part2 + most_joltage(line, 12, line_len),
        )
    });

    println!("Part 1: {part1}, Part 2: {part2}");
}

fn most_joltage(line: &str, count: usize, line_len: usize) -> u64 {
    let mut numbers: Vec<u8> = Vec::with_capacity(count);

    let mut last_index = 0_usize;
    while numbers.len() < count {
        let part = &line[(last_index)..=(line_len - (count - numbers.len()))];

        let (max, index) =
            part.chars()
                .rev()
                .enumerate()
                .fold((0_u8, 0_usize), |(max, index), (i, number)| {
                    let number = number.to_string().parse::<u8>().unwrap();

                    if number >= max {
                        return (number, part.len() - i);
                    } else {
                        return (max, index);
                    }
                });

        last_index += index;
        numbers.push(max);
    }

    numbers
        .iter()
        .map(|x| x.to_string())
        .reduce(|mut acc, e| {
            acc.push_str(&e);
            acc
        })
        .unwrap()
        .parse::<u64>()
        .unwrap()
}
