input = [[int(x) for x in y.split("-")] for y in open("input").readline().split(",")]

invalid = []

for e in input:
    _min, _max = e
    for i in range(_min, _max + 1):
        num = str(i)
        # nothing can be repeated anyway
        if int(num) < 10:
            continue

        uniqe = len(set(num))
        # quick check for a single number repeated
        if uniqe == 1:
            invalid.append(i)
            continue
        # we have more unique number than the half of it
        if uniqe > len(num) // 2:
            continue
        # check different ranges hopefully
        # unique represents the min length of the number, so we can take a shortcut at least
        for r in range(uniqe, len(num) // 2 + 1):
            if  num == num[0:r] * (len(num) // r):
                invalid.append(i)
                continue

print(sum(set(invalid)))
