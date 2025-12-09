import copy

input = [list(e.strip()) for e in open("input").readlines()]
x_max = len(input[0])
y_max = len(input)


def is_valid(x, y):
    return x in range(x_max) and y in range(y_max)


def is_roll(x, y) -> bool:
    if not is_valid(x, y):
        return False
    return input[y][x] == "@"


def check_adjascent(_x: int, _y: int) -> int:
    rolls = 0
    for x in range(_x - 1, _x + 2):
        for y in range(_y - 1, _y + 2):
            if x == _x and y == _y:
                continue
            if is_roll(x, y):
                rolls += 1
    return rolls


cleared = 0

solution = copy.deepcopy(input)

for y in range(y_max):
    for x in range(x_max):
        if not is_roll(x, y):
            continue
        if check_adjascent(x, y) < 4:
            cleared += 1
            solution[y][x] = "x"

print("Part 1:", cleared)

cleared = 0
while True:
    removed = 0

    for y in range(y_max):
        for x in range(x_max):
            if not is_roll(x, y):
                continue
            if check_adjascent(x, y) < 4:
                removed += 1
                input[y][x] = "x"

    cleared += removed
    if removed == 0:
        break

print("Part 2:", cleared)
