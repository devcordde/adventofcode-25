from typing import List

from shared.paul2708.input_reader import *
from shared.paul2708.output import write

grid: List[List[str]] = [list(line) for line in read_plain_input(day=4)]


def count_surrounding_rolls_of_paper(i: int, j: int) -> int:
    rolls_of_paper = 0

    for x in [-1, 0, 1]:
        for y in [-1, 0, 1]:
            if x == 0 and y == 0:
                continue

            new_x = i + x
            new_y = j + y

            if 0 <= new_x < len(grid) and 0 <= new_y < len(grid[0]):
                if grid[new_x][new_y] == "@":
                    rolls_of_paper += 1

    return rolls_of_paper


removed_rolls = []
removed_rolls_per_iter = -1

while removed_rolls_per_iter != 0:
    removed_rolls_per_iter = 0

    indices = []

    for i in range(len(grid)):
        for j in range(len(grid[0])):
            if grid[i][j] != "@":
                continue

            if count_surrounding_rolls_of_paper(i, j) < 4:
                removed_rolls_per_iter += 1
                indices.append((i, j))

    for a, b in indices:
        grid[a][b] = "."

    removed_rolls.append(removed_rolls_per_iter)

write(f"After the first iteration, <{removed_rolls[0]}> rolls of paper are removed.")
write(f"In total, <{sum(removed_rolls)}> rolls of paper are removed.")
