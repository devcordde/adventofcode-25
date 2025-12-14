use crate::common::{InputType, read_input};

const DAY: usize = 3;

pub fn part_one(input_type: &InputType) {
    let value: u64 = get_banks(input_type)
        .into_iter()
        .map(|bank| max_joltage(bank, 2, 0))
        .sum();
    println!("{}", value)
}

pub fn part_two(input_type: &InputType) {
    let value: u64 = get_banks(&input_type)
        .into_iter()
        .map(|bank| max_joltage(bank, 12, 0))
        .sum();
    println!("{}", value)
}

fn max_joltage(bank: Vec<u8>, digits: usize, start: usize) -> u64 {
    if digits == 0 {
        return 0;
    }
    let mut max = bank[start];
    let mut maxi = start;
    for i in start + 1..=bank.len() - digits {
        if bank[i] > max {
            max = bank[i];
            maxi = i;
        }
    }

    let current_digit_value = (bank[maxi] as u64) * 10u64.pow((digits-1) as u32);
    let remaining_digits_value = max_joltage(bank, digits - 1, maxi + 1);

    current_digit_value + remaining_digits_value
}

fn get_banks(input_type: &InputType) -> Vec<Vec<u8>> {
    read_input(DAY, input_type)
        .as_str()
        .trim()
        .split("\n")
        .map(|line| {
            line.chars()
                .map(|c| c.to_digit(10).unwrap() as u8)
                .collect()
        })
        .collect()
}
