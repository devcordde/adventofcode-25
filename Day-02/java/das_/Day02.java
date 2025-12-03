void main() throws IOException {
    var ranges = Arrays.stream(Files.readString(Path.of("input02.txt")).split(","))
            .map(range -> {
                var parts = range.split("-");
                return new long[]{Long.parseLong(parts[0]), Long.parseLong(parts[1])};
            })
            .toList();

    long part1 = 0, part2 = 0;
    for (long[] range : ranges) {
        for (long num = range[0]; num <= range[1]; num++) {
            String numStr = Long.toString(num);
            int len = numStr.length();

            for (int l = len / 2; l > 0; l--) {
                if (len % l == 0 && numStr.matches("(" + numStr.substring(0, l) + "){2,}")) {
                    if (l == len / 2.0) part1 += num;
                    part2 += num;
                    break;
                }
            }
        }
    }

    IO.println("Part 1: " + part1);
    IO.println("Part 2: " + part2);
}
