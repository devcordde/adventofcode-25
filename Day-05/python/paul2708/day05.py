from typing import List

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

print(fresh_id_ranges)
print(ids)

c = 0

for i in ids:
    for a, b in fresh_id_ranges:
        if a <= i <= b:
            c+= 1
            break

print(c)