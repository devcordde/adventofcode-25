package fyi.pauli.aoc.days

import fyi.pauli.aoc.day

val day2 = day(2) {

    val ranges: Collection<LongRange> = input.split(",").toMutableList().map {
        val (first, second) = it.split("-").map(String::trim).map(String::toLong)
        first..second
    }

    first = {
        ranges.sumOf { range ->
            range
                .map(Long::toString)
                .filter { it.length % 2 == 0 }
                .filter { it.take(it.length / 2) == it.takeLast(it.length / 2) }
                .sumOf(String::toLong)
        }
    }

    fun String.isRepeating(): Boolean = (1..length / 2)
        .filter { length % it == 0 && length / it >= 2 }
        .any { len ->
            chunked(len).all { part -> part == take(len) }
        }

    second = {
        ranges.sumOf { range ->
            range
                .map(Long::toString)
                .filter(String::isRepeating)
                .sumOf(String::toLong)
        }
    }
}