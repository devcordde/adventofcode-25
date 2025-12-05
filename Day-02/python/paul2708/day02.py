from shared.paul2708.input_reader import *
from shared.paul2708.output import write

lines = read_plain_input(day=2)

ids = [(int(line.split("-")[0]), int(line.split("-")[1])) for line in lines[0].split(",")]


def is_repeated(num: str) -> bool:
    if len(num) % 2 == 1:
        return False

    return num[:len(num) // 2] == num[len(num) // 2:]


def is_repeated_at_least_twice(num: str) -> bool:
    for pattern_length in range(1, len(num) // 2 + 1):
        if len(num) % pattern_length != 0:
            continue

        occurrences = len(num) // pattern_length
        pattern = num[:pattern_length]

        # Check if the pattern matches all substrings
        valid_pattern = True

        for i in range(1, occurrences):
            index = i * pattern_length
            if num[index:index + pattern_length] != pattern:
                valid_pattern = False
                break

        if valid_pattern:
            return True

    return False


invalid_id_sum = 0
new_invalid_id_sum = 0

for start, end in ids:
    for i in range(start, end + 1):
        num = str(i)

        if is_repeated(num):
            invalid_id_sum += i
        if is_repeated_at_least_twice(num):
            new_invalid_id_sum += i

write(f"The sum of all invalid ids is <{invalid_id_sum}>.")
write(f"The sum of all invalid ids using the new rules is <{new_invalid_id_sum}>.")
