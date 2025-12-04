void main() throws IOException {
    List<String> banks = Files.readAllLines(Path.of("day03.txt"));
    long sum1 = 0, sum2 = 0;
    for (String bank : banks) {
        int max = 0;
        int pos = 0;
        for (int i = 0; i < bank.length() - 1; i++) {
            int v = bank.charAt(i) - '0';
            if (v > max) {
                max = v;
                pos = i;
            }
        }
        int max2 = 0;
        for (int i = pos + 1; i < bank.length(); i++) {
            int v = bank.charAt(i) - '0';
            if (v > max2) {
                max2 = v;
            }
        }
        sum1 += max * 10L + max2;
        byte[] x = new byte[12];
        initialFill(bank, x);
        byte[] t = new byte[12];
        long m = 0;
        for (int i = bank.length() - 13; i >= 0; i--) {
            byte v = (byte) (bank.charAt(i) - '0');
            System.arraycopy(x, 0, t, 0, 12);
            long update = update(t, v);
            if (update > m) {
                m = update;
                var s = x;
                x = t;
                t = s;
            }
        }
        sum2 += m;
    }
    IO.println(sum1);
    IO.println(sum2);
}

private void initialFill(String bank, byte[] x) {
    for (int i = 0; i < 12; i++) {
        int v = bank.charAt(bank.length() - 12 + i) - '0';
        x[i] = (byte) v;
    }
}

long toNum(byte[] b) {
    long r = 0;
    for (byte x : b) {
        r = r * 10L + x;
    }
    return r;
}
long update(byte[] b, byte v) {
    for (int i = 0; i < 12; i++) {
        byte t = b[i];
        if (t > v) {
            return i == 0 ? 0 : toNum(b);
        }
        b[i] = v;
        v = t;
    }
    return toNum(b);
}
