banks = [list(map(int,list(e.strip()))) for e in open("input").readlines()]

def detect_joltage(bank: list[int], turn_on: int = 2) -> int:
    joltage = []
    index = 0
    for i  in range(turn_on):
        upper = (len(bank)) - (turn_on -1 - i)
        _max = max(bank[index:upper])
        index = bank.index(_max, index) + 1
        joltage.append(_max)
    print("".join(map(str, joltage)))
    return int("".join(map(str, joltage)))

print("Part 1:",sum([detect_joltage(bank) for bank in banks]))
print("Part 2:",sum([detect_joltage(bank, 12) for bank in banks]))
