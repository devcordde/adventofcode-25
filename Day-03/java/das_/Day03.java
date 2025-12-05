void main() throws IOException {
    var banks = Files.lines(Path.of("input03.txt"))
            .map(line -> Arrays.stream(line.split(""))
                    .mapToInt(Integer::parseInt)
                    .toArray())
            .toList();

    IO.println("Part 1: " + banks.stream().mapToLong(bank -> largestBattery(bank, 2)).sum());
    IO.println("Part 2: " + banks.stream().mapToLong(bank -> largestBattery(bank, 12)).sum());
}

private long largestBattery(int[] bank, int batteryCount) {
    int index = -1;
    long number = 0;

    for (int k = 0; k < batteryCount; k++) {
        int digit = 1;
        for (int i = index + 1; i < bank.length - (batteryCount - k - 1); i++) {
            if (bank[i] > digit) {
                index = i;
                digit = bank[i];
            }
        }

        number += (long) Math.pow(10, (batteryCount - 1 - k)) * digit;
    }

    return number;
}
