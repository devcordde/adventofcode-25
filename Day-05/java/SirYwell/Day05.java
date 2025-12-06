void main() throws IOException {
    List<String> lines = Files.readAllLines(Path.of("day05.txt"));
    int split = lines.indexOf("");
    List<Range> ranges = lines.subList(0, split).stream().map(s -> {
        String[] a = s.split("-");
        return new Range(Long.parseLong(a[0]), Long.parseLong(a[1]));
    }).collect(Collectors.toCollection(ArrayList::new));
    long count = 0;
    for (String ingredient : lines.subList(split + 1, lines.size())) {
        long in = Long.parseLong(ingredient);
        for (Range range : ranges) {
            if (range.min <= in && in <= range.max) {
                count++;
                break; // only count once
            }
        }
    }
    IO.println(count);
    ranges.sort(Comparator.comparingLong(r -> r.min));
    List<Range> merged = new ArrayList<>();
    outer: 
    for (Range range : ranges) {
        for (Range existing : merged) {
            if (existing.min <= range.min && range.min <= existing.max) {
                existing.max = Math.max(range.max, existing.max);
                continue outer;
            }
        }
        merged.add(new Range(range.min, range.max));    
    }
    long all = merged.stream().mapToLong(r -> r.max - r.min + 1).reduce(Math::addExact).orElseThrow();
    IO.println(all);
}

static class Range {
    private final long min;
    private long max;

    Range(long min, long max) {
        this.min = min;
        this.max = max;
    }
}
