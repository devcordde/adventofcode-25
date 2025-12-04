from shared.paul2708.input_reader import *
from shared.paul2708.output import write, write_2d_array

grid = [list(line) for line in read_plain_input(day=4, example=None)]

write_2d_array(grid)

xx = 0
change = True
while change:

    change = False
    diff = 0
    indices = []

    for i in range(len(grid)):
        for j in range(len(grid[0])):
            if grid[i][j] != "@":
                continue

            rolls = 0

            for x in [-1, 0, 1]:
                for y in [-1, 0, 1]:
                    if x == 0 and y == 0:
                        continue

                    a = i + x
                    b = j + y

                    if 0 <= a < len(grid) and 0 <= b < len(grid[0]):
                        if grid[a][b] == "@":
                            rolls += 1

            if rolls < 4:
                xx += 1
                diff += 1
                change = True
                indices.append((i, j))

    for a, b in indices:
        grid[a][b] = "."

    print(f"Removed {diff}")
    write_2d_array(grid)

print(xx)