package fyi.pauli.aoc.days

import fyi.pauli.aoc.day
import kotlin.math.abs

val day5 = day(5) {
    val splitIndex = inputLines.indexOf("")
    val ranges = inputLines.take(splitIndex).map { it.split("-") }.map { it[0].toLong()..it[1].toLong() }
    val ingredients = inputLines.takeLast(inputLines.size - splitIndex - 1).map(String::toLong)

    println(ranges)
    println(ingredients)

    first = {
        ingredients.count { ingredient -> ranges.any { range -> range.contains(ingredient) } }
    }

    second = {
        ranges
            .asSequence()
            .sortedBy { it.first }
            .fold(mutableListOf<LongRange>()) { acc, r ->
                if (acc.lastIndex == -1) {
                    acc.add(r)
                } else {
                    val last = acc[acc.lastIndex]
                    acc[acc.lastIndex] = if (r.first <= last.last + 1) {
                        last.first .. maxOf(last.last, r.last)
                    } else {
                        acc.add(r)
                        last
                    }
                }
                acc
            }
            .sumOf { it.last - it.first + 1 }
    }
}