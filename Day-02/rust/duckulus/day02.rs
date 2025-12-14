use crate::common::{InputType, read_input};
use std::ops::{Range, RangeInclusive};

const DAY: usize = 2;

pub fn part_one(input_type: InputType) {
    let ranges = read_ranges(input_type);

    let mut sum = 0;
    for range in ranges {
        for i in range {
            let s = i.to_string();
            let str = s.as_str();
            let len = str.len();

            if len % 2 != 0 {
                continue
            }

            if str[..len / 2] == str[len / 2..] {
                sum += i;
            }
        }
    }
    println!("{}", sum);
}

pub fn part_two(input_type: InputType) {
    let ranges = read_ranges(input_type);

    let mut sum = 0;
    for range in ranges {
        for i in range {
            let s = i.to_string();
            let str = s.as_str();
            let len = str.len();
            for pref in 1..=len / 2 {
                if len % pref != 0 {
                    continue;
                }
                let reps = len / pref;
                if str[..len / reps].repeat(reps) == str {
                    sum += i;
                    break;
                }
            }
        }
    }
    println!("{}", sum);
}

fn read_ranges(input_type: InputType) -> Vec<RangeInclusive<u64>> {
    let ranges: Vec<_> = read_input(DAY, &input_type)
        .as_str()
        .split(",")
        .map(|s| {
            let split: Vec<_> = s.split("-").collect();
            split[0].trim().parse::<u64>().unwrap()..=split[1].trim().parse::<u64>().unwrap()
        })
        .collect();
    ranges
}
