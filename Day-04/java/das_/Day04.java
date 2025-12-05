void main() throws IOException {
    var grid = Files.lines(Path.of("input04.txt"))
            .map(line -> line.split(""))
            .toArray(String[][]::new);

    var paperRollPositions = new HashSet<Tuple>();
    for (int y = 0; y < grid.length; y++) {
        for (int x = 0; x < grid[0].length; x++) {
            if (grid[y][x].equals("@")) {
                paperRollPositions.add(new Tuple(y, x));
            }
        }
    }

    int part1;
    IO.println("Part 1: " + (part1 = solve(paperRollPositions, false)));
    IO.println("Part 2: " + (part1 + solve(paperRollPositions, true)));
}

int solve(Set<Tuple> paperRollPositions, boolean part2) {
    var total = 0;
    var removed = true;

    do {
        var removedPositions = new HashSet<>();

        for (Tuple position : paperRollPositions) {
            if (isForkliftable(position, paperRollPositions)) {
                total++;
                removedPositions.add(position);
            }
        }

        removed = !removedPositions.isEmpty();
        paperRollPositions.removeIf(removedPositions::contains);
    } while (removed && part2);

    return total;
}

boolean isForkliftable(Tuple roll, Set<Tuple> positions) {
    int count = 0;

    for (int a = -1; a <= 1; a++) {
        for (int b = -1; b <= 1; b++) {
            if (positions.contains(new Tuple(roll.y + a, roll.x + b))) {
                ++count;
            }
        }
    }

    return count < 5;
}

record Tuple(int y, int x) {
    @Override
    public boolean equals(Object o) {
        if (o instanceof Tuple(int y1, int x1)) {
            return x1 == x && y1 == y;
        }
        return false;
    }
}
