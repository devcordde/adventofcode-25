from shared.paul2708.input_reader import *
from shared.paul2708.output import write

grid = [list(line) for line in read_plain_input(day=4, example=None)]

xx = 0
for i in range(len(grid)):
    for j in range(len(grid[0])):
        if grid[i][j] != "@":
            continue

        rolls = -1

        for x in [-1, 0, 1]:
            for y in [-1, 0, 1]:
                a = i + x
                b = j + y

                if 0 <= a < len(grid) and 0 <= b < len(grid[0]):
                    if grid[a][b] == "@":
                        rolls += 1

        if rolls < 4:
            xx += 1


print(xx)