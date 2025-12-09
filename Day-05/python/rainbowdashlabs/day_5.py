from typing import NamedTuple

Range = NamedTuple("Range", [("start", int), ("end", int)])
ranges: str
ranges, ids = open("input").read().split("\n\n")

ranges: list = [list(map(int, e.split("-"))) for e in ranges.splitlines()]
ranges: list[Range] = [Range(e[0], e[1]) for e in ranges]
ids = [int(e) for e in ids.splitlines()]


def is_fresh(id: int) -> bool:
    for e in ranges:
        if e.start <= id <= e[1]:
            return True
    return False


fresh = sum(map(is_fresh, ids))
print("Part 1:", fresh)

# Not enough memory :c
# ranges = [list(range(e[0], e[1] + 1)) for e in ranges]
# fresh = {item for row in ranges for item in row}

ranges.sort(key=lambda r: r.start)

merged = []

current_start, current_end = ranges[0].start, ranges[0].end

for i in range(1, len(ranges)):
    next_start, next_end = ranges[i].start, ranges[i].end

    if next_start <= current_end + 1:
        current_end = max(current_end, next_end)
    else:
        merged.append((current_start, current_end))
        current_start, current_end = next_start, next_end

merged.append((current_start, current_end))

print("Part 2:", sum(end - start + 1 for start, end in merged))
