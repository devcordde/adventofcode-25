from typing import List

import tqdm

from shared.paul2708.input_reader import *
from shared.paul2708.output import write

lines = read_plain_input(day=5, example=None)

fresh_id_ranges = []
ids = []

a = True
for line in lines:
    if line == "":
        a = False
        continue
    if a:
        x = line.split("-")
        fresh_id_ranges.append((int(x[0]), int(x[1])))
    else:
        ids.append(int(line))


c = 0

for i in ids:
    for a, b in fresh_id_ranges:
        if a <= i <= b:
            c += 1
            break


clean_id_ranges = []
clean_id_ranges.append(fresh_id_ranges[0])


def merge(clean_a, clean_b, a, b):
    if clean_a <= a <= b <= clean_b:
        return [(clean_a, clean_b)]

    if a <= clean_a <= clean_b <= b:
        return [(a, b)]

    if clean_a <= a <= clean_b <= b:
        return [(clean_a, b)]

    if a <= clean_a <= b <= clean_b:
        return [(a, clean_b)]

    return [(a, b), (clean_a, clean_b)]


def merge_list(l):
    result = set()
    for i in range(len(l)):
        for j in range(0, len(l)):
            merged = merge(l[i][0], l[i][1], l[j][0], l[j][1])
            merged += merge(l[j][0], l[j][1], l[i][0], l[i][1])
            merged = set(merged)

            if (l[i][0], l[i][1]) in result:
                result.remove((l[i][0], l[i][1]))
            if (l[j][0], l[j][1]) in result:
                result.remove((l[j][0], l[j][1]))

            for m in merged:
                result.add(m)

    return result


def merge_til_clean(l):
    merged = merge_list(l)

    if set(merged) == set(l):
        return merged

    return merge_til_clean(list(merged))


res = merge_til_clean(fresh_id_ranges)

total = 0

for a, b in res:
    total += b - a + 1

print(total)

#clean_res = []
#clean_res.append(fresh_id_ranges[0])

#for i in range(1, len(fresh_id_ranges)):
#    a = fresh_id_ranges[i]


# To high: 1043087815449996