void main() throws IOException {
    var boxes = Files.lines(Path.of("input08.txt"))
            .map(line -> {
                var parts = line.split(",");
                return new Triple(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
            }).toList();

    var pairs = new HashSet<Distance>();
    for (int k = 0; k < boxes.size() - 1; k++) {
        Triple t1 = boxes.get(k);
        for (int i = k + 1; i < boxes.size(); i++) {
            Triple t2 = boxes.get(i);
            long distance = (long) (Math.pow(t1.x - t2.x, 2) + Math.pow(t1.y - t2.y, 2) + Math.pow(t1.z - t2.z, 2));
            pairs.add(new Distance(t1, t2, distance));
        }
    }

    var closestPairs = pairs.stream()
            .sorted(Comparator.comparingLong(distance -> distance.distance))
            .collect(Collectors.toCollection(ArrayDeque::new));

    List<Set<Triple>> circuits = new ArrayList<>(boxes.stream().map(box -> new HashSet<>(Set.of(box))).toList());
    long part1 = 0, part2 = 1;
    int steps = 0;

    while (!closestPairs.isEmpty()) {
        steps++;
        Distance next = closestPairs.pop();

        var skip = false;
        for (Set<Triple> circuit : circuits) {
            if (circuit.contains(next.a) && circuit.contains(next.b)) {
                skip = true;
                break;
            }

            if (circuit.contains(next.a) || circuit.contains(next.b)) {
                circuit.add(next.a);
                circuit.add(next.b);
                skip = true;
            }
        }

        if (!skip) circuits.add(new HashSet<>(Set.of(next.a, next.b)));

        for (int l = 0; l < circuits.size() - 1; l++) {
            for (int m = l + 1; m < circuits.size(); m++) {
                if (!Collections.disjoint(circuits.get(l), circuits.get(m))) {

                    if (circuits.size() == 2) {
                        for (Triple box1 : circuits.get(0)) {
                            for (Triple box2 : circuits.get(1)) {
                                if (box1 == box2) {
                                    part2 *= box1.x;
                                }
                            }
                        }
                    }
                    circuits.get(l).addAll(circuits.get(m));
                    circuits.remove(m);
                }
            }
        }

        if (steps == 1000) {
            part1 = circuits.stream()
                    .map(Set::size)
                    .sorted(Comparator.reverseOrder())
                    .map(Long::valueOf)
                    .limit(3)
                    .reduce(1L, (acc, element) -> acc * element);
        }
    }

    IO.println("Part 1: " + part1);
    IO.println("Part 2: " + part2);
}

record Triple(int x, int y, int z) {
    @Override
    public boolean equals(Object o) {
        if (o instanceof Triple(int x1, int y1, int z1)) {
            return x1 == x && y1 == y && z1 == z;
        }
        return false;
    }
}

record Distance(Triple a, Triple b, long distance) {
    @Override
    public boolean equals(Object o) {
        if (o instanceof Distance(Triple a1, Triple b1, long distance1)) {
            return (a1 == a && b1 == b) || (b1 == a && a1 == b);
        }
        return false;
    }
}
