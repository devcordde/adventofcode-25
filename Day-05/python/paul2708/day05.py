from typing import List, Optional, Tuple

from shared.paul2708.input_reader import *
from shared.paul2708.output import write
from shared.paul2708.utility import copy_list

lines = read_plain_input(day=5)

# Parse ranges and IDs
fresh_id_ranges = []
ids = []

separator = lines.index("")

for i in range(separator):
    r = lines[i].split("-")
    fresh_id_ranges.append((int(r[0]), int(r[1])))

for i in range(separator + 1, len(lines)):
    ids.append(int(lines[i]))

# Part 1
counter = 0

for single_id in ids:
    for a, b in fresh_id_ranges:
        if a <= single_id <= b:
            counter += 1
            break

write(f"There are <{counter}> fresh ingredients out of the given IDs.")


# Part 2
def merge_two_ranges(first: Tuple[int, int], second: Tuple[int, int]) -> Optional[Tuple[int, int]]:
    x, y = first
    a, b = second

    if x <= a <= b <= y:
        return x, y

    if a <= x <= y <= b:
        return a, b

    if x <= a <= y <= b:
        return x, b

    if a <= x <= b <= y:
        return a, y

    return None


def merge_list_of_ranges(ranges: List[Tuple[int, int]]) -> List[Tuple[int, int]]:
    result = copy_list(ranges)

    for i in range(len(ranges)):
        for j in range(i + 1, len(ranges)):
            merged = merge_two_ranges(ranges[i], ranges[j])

            if merged is not None:
                result.remove(ranges[i])
                result.remove(ranges[j])

                result.append(merged)

                return merge_list_of_ranges(result)

    return result


merged_ranges = merge_list_of_ranges(fresh_id_ranges)
accumulated_lengths = sum([b - a + 1 for a, b in merged_ranges])

write(f"There are <{accumulated_lengths}> fresh ingredients in total.")
