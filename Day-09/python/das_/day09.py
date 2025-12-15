red_tiles = list(map(lambda line: (int((coordinates := line.split(","))[0]), int(coordinates[1])), open("input09.txt").read().split("\n")))
areas = []

for i in range(len(red_tiles)):
    for j in range(i + 1, len(red_tiles)):
        area = (abs(red_tiles[i][0] - red_tiles[j][0]) + 1) * (abs(red_tiles[i][1] - red_tiles[j][1]) + 1)
        areas.append((red_tiles[i], red_tiles[j], area, i))

sorted_areas = sorted(areas, key=lambda area: area[2], reverse=True)
print("Part 1:", sorted_areas[0][2])

for area in sorted_areas:
    left, right = sorted([area[0][0], area[1][0]])
    down, up = sorted([area[0][1], area[1][1]])
    x2, y2 = area[0]
    valid = True
    for i in range(len(red_tiles)):
        x1, y1 = x2, y2
        x2, y2 = red_tiles[(area[3] + i) % len(red_tiles)][0], red_tiles[(area[3] + i) % len(red_tiles)][1]
        x_l, x_h = sorted([x1, x2])
        y_l, y_h = sorted([y1, y2])
        if (left < x1 < right and down < y1 < up) or (down < y1 < up and x_l <= left < x_h and x_l < right <= x_h) or (left < x1 < right and y_l <= down < y_h and y_l < up <= y_h):
            valid = False
            break
    if valid:
        part2 = area[2]
        break

print("Part 2:", part2)
