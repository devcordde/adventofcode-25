void main() throws IOException {
    List<String> lines = Files.readAllLines(Path.of("day06.txt"));
    String last = lines.getLast();
    record Task(int startPos, int length, LongBinaryOperator operator) {}
    List<Task> tasks = new ArrayList<>();
    LongBinaryOperator op = null;
    int start = 0;
    for (int i = 0; i < last.length(); i++) {
        LongBinaryOperator nextOp;
        if (last.charAt(i) == ' ') {
            continue;
        } else if (last.charAt(i) == '*') {
            nextOp = (a, b) -> a * b;
        } else {
            nextOp = Long::sum;
        }
        if (op != null) {
            tasks.add(new Task(start, i - 1 - start, op));
        }
        op = nextOp;
        start = i;
    }
    tasks.add(new Task(start, lines.getFirst().length() - start, op));
    long grandTotal = 0;
    for (Task task : tasks) {
        long result = Math.abs(task.operator.applyAsLong(-1, 1));
        for (int i = 0; i < lines.size() - 1; i++) {
            String line = lines.get(i);
            long l = Long.parseLong(line.substring(task.startPos, task.startPos + task.length).trim());
            result = task.operator.applyAsLong(result, l);
        }
        grandTotal += result;
    }
    IO.println(grandTotal);
    grandTotal = 0;
    for (Task task : tasks) {
        long result = Math.abs(task.operator.applyAsLong(-1, 1));
        for (int i = 0; i < task.length; i++) {
            long l = 0;
            for (int j = 0; j < lines.size() - 1; j++) {
                var v = digitAt(lines.get(j), task.startPos + i);
                if (v.isPresent()) {
                    l = l * 10 + v.orElseThrow();
                }
            }
            result = task.operator.applyAsLong(result, l);
        }
        grandTotal += result;
    }
    IO.println(grandTotal);
}

OptionalInt digitAt(String s, int pos) {
    if (s.length() <= pos) {
        return OptionalInt.empty();
    }
    char c = s.charAt(pos);
    if ('0' > c || c > '9') {
        return OptionalInt.empty();
    }
    return OptionalInt.of(c - '0');
}
