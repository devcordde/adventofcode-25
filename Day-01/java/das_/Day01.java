void main() throws IOException {
    var lines = Files.lines(Path.of("input01.txt")).toList();

    int dial = 50;
    int password1 = 0;
    int password2 = 0;
    boolean lastZero = false;

    for (String line : lines) {
        int number = Integer.parseInt(line.substring(1));

        if (line.startsWith("R")) {
            dial += number;
        } else {
            dial -= number;
        }

        if (dial == 0 && !lastZero) {
            password2++;
        } else if (dial < 0) {
            password2 -= Math.floorDiv(dial - 1, 100);
            if (lastZero) {
                password2 -= 1;
            }
        } else if (dial > 99) {
            password2 += Math.floorDiv(dial, 100);
        }

        dial = (dial % 100 + 100) % 100;

        lastZero = false;
        if (dial == 0) {
            lastZero = true;
            password1++;
        }
    }

    System.out.println("Part 1: " + password1);
    System.out.println("Part 2: " + password2);
}
