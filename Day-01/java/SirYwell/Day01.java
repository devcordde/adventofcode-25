void main() throws IOException {
    List<String> lines = Files.readAllLines(Path.of("day01.txt"));
    int pos = 50, c1 = 0, c2 = 0;
    for (String line : lines) {
        int move = Integer.parseInt(line, 1, line.length(), 10);
        move = line.charAt(0) == 'L' ? -move : move;
        boolean z = pos == 0;
        pos += move;
        if (pos >= 100) {
            c2 += pos / 100;
        } else if (pos < 0) {
            c2 -= pos / 100;
            if (!z) {
                c2++;
            }
        } else if (pos == 0) {
            c2++;
        }
        pos = Math.floorMod(pos, 100);
        if (pos == 0) {
            c1++;
        }
    }
    IO.println(c1);
    IO.println(c2);
}
