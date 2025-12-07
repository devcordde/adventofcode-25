void main() throws IOException {
    List<String> lines = Files.readAllLines(Path.of("day07.txt"));
    String top = lines.getFirst();
    int length = top.length();
    int S = top.indexOf('S');
    BitSet beams = new BitSet(length);
    long[] a = new long[length], b = new long[length];
    beams.set(S);
    b[S] = 1;
    long splitCount = 0;
    for (int i = 1; i < lines.size(); i++) {
        String l = lines.get(i);
        for (int x = beams.nextSetBit(0); x >= 0;) {
            int n = beams.nextSetBit(x + 1);
            if (l.charAt(x) == '^') {
                splitCount++;
                beams.clear(x);
                if (x > 0) {
                    beams.set(x - 1);
                    a[x - 1] += b[x];
                }
                if (x < length - 1) {
                    beams.set(x + 1);
                    a[x + 1] += b[x];
                }
            } else {
                a[x] += b[x];
            }
            x = n;
        }
        var t = a;
        a = b;
        Arrays.fill(a, 0L);
        b = t;
    }
    IO.println(splitCount);
    IO.println(Arrays.stream(b).sum());
}
