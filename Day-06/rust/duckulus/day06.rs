use crate::common::{InputType, read_lines};

const DAY: usize = 6;

pub fn part_one(input_type: &InputType) {
    let lines = read_lines(DAY, input_type);
    let mut rows = Vec::new();
    let mut operations = Vec::new();
    for line in &lines {
        if line.contains("+") {
            operations.append(&mut line.trim().split_whitespace().collect::<Vec<_>>());
            break;
        }
        rows.push(
            line.trim()
                .split_whitespace()
                .map(|s| s.parse::<u64>().unwrap())
                .collect::<Vec<_>>(),
        )
    }

    let mut results = rows.remove(0);
    for row in rows {
        for i in 0..row.len() {
            match operations[i] {
                "+" => results[i] += row[i],
                "*" => results[i] *= row[i],
                _ => panic!("Unexpected operation {}", operations[i]),
            }
        }
    }
    println!("{}", results.iter().sum::<u64>())
}

pub fn part_two(input_type: &InputType) {
    let mut lines = read_lines(DAY, input_type);
    let last_line = lines.remove(lines.len() - 1);
    let operations = last_line.trim().split_whitespace().collect::<Vec<_>>();
    let max_length = lines.iter().map(|l| l.len()).max().unwrap();
    lines
        .iter_mut()
        .for_each(|l| l.push_str(" ".repeat(max_length - l.len()).as_str()));

    let mut iters = lines.iter().map(|l| l.chars()).collect::<Vec<_>>();
    let mut results: Vec<u64> = Vec::new();
    let mut current_group = Vec::new();
    for i in 0..max_length {
        let chars = iters
            .iter_mut()
            .map(|iter| iter.next().unwrap())
            .collect::<Vec<_>>();
        if chars.iter().all(|c| *c == ' ') {
            let result = match operations[results.len()] {
                "+" => current_group.iter().fold(0, |a, b| a + b),
                "*" => current_group.iter().fold(1, |a, b| a * b),
                _ => panic!("Unexpected operation {}", operations[results.len()]),
            };
            results.push(result);
            current_group.clear();
            continue;
        }

        let mut num = 0;
        for c in chars.iter() {
            if *c != ' ' {
                num *= 10;
                num += c.to_digit(10).unwrap() as u64
            }
        }
        current_group.push(num);
    }
    let result = match operations[results.len()] {
        "+" => current_group.iter().fold(0, |a, b| a + b),
        "*" => current_group.iter().fold(1, |a, b| a * b),
        _ => panic!("Unexpected operation {}", operations[results.len()]),
    };
    results.push(result);

    println!("{}", results.iter().sum::<u64>())
}
