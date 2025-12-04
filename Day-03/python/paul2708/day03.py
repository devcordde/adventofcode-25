from shared.paul2708.input_reader import *
from shared.paul2708.output import write

lines = read_plain_input(day=3)


# Part 1
def contains_a_after_b(line: str, digit_a: str, digit_b: str) -> bool:
    if digit_a not in line or digit_b not in line:
        return False

    a_index = line.index(digit_a)
    b_index = len(line) - line[::-1].index(digit_b) - 1

    return a_index < b_index


joltage = 0

for line in lines:
    for i in range(99, 9, -1):
        highest_num = str(i)

        if contains_a_after_b(line, highest_num[0], highest_num[1]):
            joltage += i
            break

write(f"The total output joltage is <{joltage}>.")


# Part 2
def look_ahead(line: str, start_index: int, k: int):
    head = []

    for i in range(1, k + 1):
        if start_index + i < len(line):
            head.append(int(line[start_index + i]))

    return head


total_removals = len(lines[0]) - 12

joltage = 0

for line in lines:
    removals = 0
    largest_joltage = ""

    for i in range(len(line)):
        if removals == total_removals:
            largest_joltage += line[i]
            continue

        current = int(line[i])
        head = look_ahead(line, i, total_removals - removals)

        if current == max(head + [current]):
            largest_joltage += line[i]
        else:
            removals += 1

    joltage += int(largest_joltage[:12])

write(f"The new total output joltage is <{joltage}>.")
