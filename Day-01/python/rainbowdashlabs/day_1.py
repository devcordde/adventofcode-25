input = [ - int(e[1:]) if "L" in e else int(e[1:]) for e in open("input").readlines()]

index = 50
max = 99
min = 0
zero_part_1 = 0
zero_part_2 = 0

for e in input:
    while e != 0:
        if e > 0:
            index += 1
            e -= 1
            if index > max:
                index = min
        else:
            index -= 1
            e += 1
            if index < min:
                index = max
        if index == 0:
            zero_part_2 += 1
    if index == 0:
        zero_part_1 += 1

print(f"Part 1: {zero_part_1}")
print(f"Part 2: {zero_part_2}")
