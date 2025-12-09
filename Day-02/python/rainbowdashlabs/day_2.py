input = [[int(x) for x in y.split("-")] for y in open("input").readline().split(",")]

invalid = []

for e in input:
    _min, _max = e
    print(_min, "-",_max)
    if _min < 10:
        current = 1
    else:
        current: int = int(str(_min)[0: max(1,len(str(_min)) // 2)])
    while True:
        if int(str(current) * 2) < _min:
            current += 1
            continue
        if(int(str(current) * 2)) <= _max:
            invalid.append(int(str(current) * 2))
            print(int(str(current) * 2))
            current += 1
        else:
            break

print(sum(invalid))

