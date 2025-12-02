void main() throws IOException {
    String[] ranges = Files.readAllLines(Path.of("day02.txt")).getFirst().split(",");
    long sum1 = 0, sum2 = 0;
    for (String s : ranges) {
        int x = s.indexOf('-');
        long l = Long.parseLong(s, 0, x, 10);
        long h = Long.parseLong(s, x + 1, s.length(), 10);
        long uhh = upperHalf(h);
        Set<Long> found = new HashSet<>();
        for (long j = 1; j <= uhh; j++) {
            int cl = nOfDigits(l) / nOfDigits(j);
            int ch = nOfDigits(h) / nOfDigits(j);
            if (ch == 1) continue;
            long rep = repeat(j, cl);
            if (rep > 9 && contains(l, h, rep)) {
                if (found.add(rep)) {
                    sum2 += rep;
                }
                if (cl == 2) sum1 += rep;
            }
            if (cl != ch) {
                rep = repeat(j, ch);
                if (contains(l, h, rep)) {
                    if (found.add(rep)) {
                        sum2 += rep;
                    }
                    if (ch == 2) sum1 += rep;
                }
            }
        }
    }
    IO.println(sum1);
    IO.println(sum2);
}

private static int nOfDigits(long v) {
    return (int) Math.log10(v) + 1;
}

long repeat(long v, int n) {
    if (v == 0) return 0;
    int f = Math.powExact(10, nOfDigits(v));
    long r = v;
    while (--n > 0) {
        r = f * r + v;
    }
    return r;
}

long upperHalf(long v) {
    int div = (int) (Math.log10(v) + 1) / 2;
    return (v / (Math.powExact(10, div)));
}

boolean contains(long l, long h, long v) {
    return l <= v && v <= h;
}
