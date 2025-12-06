void main() throws IOException {
    int[][] rows = Files.lines(Path.of("day04.txt"))
        .map(s -> s.chars().map(i -> i == '@' ? Integer.MAX_VALUE : -1).toArray()).toArray(int[][]::new);
    int accessible = 0;
    int accessibleBefore;
    int round = 1;
    do {
        accessibleBefore = accessible;
        for (int y = 0; y < rows.length; y++) {
            int[] r = rows[y];
            for (int x = 0; x < r.length; x++) {
                if (r[x] < round) {
                    continue;
                }
                int adjacent = 0;
                for (int dy = -1; dy < 2; dy++) {
                    for (int dx = -1; dx < 2; dx++) {
                        if ((dy | dx) == 0) {
                            continue;
                        }
                        int ry = y + dy;
                        int rx = x + dx;
                        if ((ry | rx) < 0 || ry >= rows.length || rx >= r.length) {
                            continue;
                        }
                        if (rows[ry][rx] >= round) {
                            adjacent++;
                        }
                    }
                }
                if (adjacent < 4) {
                    accessible++;
                    r[x] = round;
                }
            }
        }
        if (accessibleBefore == 0) {
            IO.println(accessible);
        }
        round++;
    } while (accessible != accessibleBefore);
    IO.println(accessible);
}
