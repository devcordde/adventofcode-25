use crate::common::{InputType, read_input, Range};
use std::cmp::{max, min};
use std::collections::HashSet;

const DAY: usize = 5;

fn parse(input_type: &InputType) -> (Vec<Range>, Vec<i64>) {
    let input = read_input(DAY, input_type);
    let (ranges, ingredients) = input.split_once("\n\n").unwrap();

    let ranges: Vec<_> = ranges
        .lines()
        .map(|line: &str| {
            let (l, h) = line.split_once("-").unwrap();
            Range {
                l: l.parse::<i64>().unwrap(),
                h: h.parse::<i64>().unwrap(),
            }
        })
        .collect();

    let ingredients: Vec<_> = ingredients
        .lines()
        .map(|line: &str| line.parse::<i64>().unwrap())
        .collect();

    (ranges, ingredients)
}

pub fn part_one(input_type: &InputType) {
    let (ranges, ingredients) = parse(input_type);

    let mut sum = 0;
    for ingredient in ingredients {
        for range in &ranges {
            if range.l <= ingredient && range.h >= ingredient {
                sum += 1;
                break;
            }
        }
    }
    println!("{}", sum);
}

pub fn part_two(input_type: &InputType) {
    let (ranges, _) = parse(input_type);

    let mut range_list = Vec::new();

    for range in ranges {
        if range_list.is_empty() {
            range_list.push(range);
            continue;
        }

        let mut intersecting: Vec<Range> = range_list
            .iter()
            .filter(|r| r.intersection(&range).is_some())
            .map(|r| r.clone())
            .collect();
        intersecting.push(range.clone());

        let l = intersecting.iter().map(|r| r.l).min().unwrap();
        let h = intersecting.iter().map(|r| r.h).max().unwrap();
        let union = Range { l, h };

        range_list.retain(|r| r.intersection(&range).is_none());
        range_list.push(union);
    }

    let sum: i64 = range_list.iter().map(|r| r.len()).sum();
    println!("{}", sum);
}

#[cfg(test)]
mod test {
    use crate::day05::Range;

    #[test]
    fn test_range_intersect() {
        let r1 = Range { l: 3, h: 5 };
        let r2 = Range { l: 6, h: 8 };
        let r3 = Range { l: 4, h: 9 };
        let r4 = Range { l: 1, h: 4 };
        let r5 = Range { l: 5, h: 6 };

        assert_eq!(0, r1.intersect(&r2));
        assert_eq!(2, r1.intersect(&r3));
        assert_eq!(2, r1.intersect(&r4));
        assert_eq!(1, r1.intersect(&r5));
        assert_eq!(3, r1.intersect(&r1));
    }
}
