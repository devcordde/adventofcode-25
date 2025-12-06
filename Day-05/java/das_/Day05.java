void main() throws IOException {
    var input = Files.readString(Path.of("input05.txt")).split("\n\n");
    var ranges = Arrays.stream(input[0].split("\n")).map(range -> {
        var parts = range.split("-");
        return new long[]{Long.parseLong(parts[0]), Long.parseLong(parts[1])};
    }).toList();
    var ids = Arrays.stream(input[1].split("\n")).map(Long::parseLong).toList();

    IO.println("Part 1: " + ids.stream().filter(id -> isInRanges(id, ranges)).count());
    IO.println("Part 2: " + part2(new ArrayList<>(ranges)));
}

boolean isInRanges(long id, List<long[]> ranges) {
    for (long[] range : ranges) {
        if (id >= range[0] && id <= range[1]) {
            return true;
        }
    }
    return false;
}

long part2(List<long[]> ranges) {
    ranges.sort(Comparator.comparing(range -> range[0]));
    long[] currentRange = ranges.getFirst();
    long total = 0;

    for (int j = 1; j < ranges.size(); j++) {
        long[] tempRange = ranges.get(j);
        if (tempRange[0] <= currentRange[1] && tempRange[1] > currentRange[1]) {
            currentRange[1] = tempRange[1];
        }

        if (tempRange[0] > currentRange[1]) {
            total += currentRange[1] - currentRange[0] + 1;
            currentRange = tempRange;
        }
    }

    return total + currentRange[1] - currentRange[0] + 1;
}
