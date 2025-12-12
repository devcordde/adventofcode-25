use crate::common::{InputType, read_lines};

const DAY: usize = 1;

pub fn part_one(input_type: InputType) {
    let lines = read_lines(DAY, &input_type);
    let mut dial = 50;
    let mut password = 0;
    for line in lines {
        let direction = if &line.as_str()[..1] == "L" { -1 } else { 1 };
        dial += direction * line.as_str()[1..].parse::<i32>().unwrap();
        dial = dial % 100;
        while dial < 0 {
            dial = 100 + dial
        }
        if dial == 0 {
            password += 1;
        }
    }
    println!("{}", password);
}

pub fn part_two(input_type: InputType) {
    let lines = read_lines(DAY, &input_type);
    let mut dial = 50;
    let mut password = 0;
    for line in lines {
        let direction = if &line.as_str()[..1] == "L" { -1 } else { 1 };
        let change = line.as_str()[1..].parse::<i32>().unwrap();

        if direction == -1 {
            let t = dial;
            dial -= change;
            if dial < 0 && t == 0 {
                password -= 1;
            }
            while dial < 0 {
                dial = 100 + dial;
                password += 1;
            }
            if dial == 0 {
                password += 1;
            }
        } else {
            dial += change;
            if dial >= 100 {
                password += dial / 100;
                dial = dial % 100;
            }
        }
    }
    println!("{}", password);
}
