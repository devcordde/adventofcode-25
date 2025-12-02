package fyi.pauli.aoc

import java.io.InputStreamReader

class Day(val day: Int, var first: () -> Any = { 0 }, var second: () -> Any = { 0 }) {

    operator fun invoke() {
        println("Part 1: ${first()}")
        println("Part 2: ${second()}")
    }

    val inputReader: InputStreamReader
        get() = Day::class.java.getResourceAsStream("/input_${day.toString().padStart(2, '0')}.txt")!!.reader()

    val inputLines: Collection<String>
        get() = inputReader.readLines()

    val input: String
        get() = inputReader.readText()
}

fun day(day: Int, body: Day.() -> Unit) = Day(day).apply(body)