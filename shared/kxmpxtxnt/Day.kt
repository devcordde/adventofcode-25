package fyi.pauli.aoc

import java.io.InputStreamReader
import kotlin.time.Duration
import kotlin.time.measureTime

class Day(val day: Int, var first: () -> Any = { 0 }, var second: () -> Any = { 0 }) {

    operator fun invoke() {
        var firstValue: Any? = null
        val firstTimed = measureTime { firstValue = first() }
        println("Part 1: $firstValue, took ${firstTimed.inWholeMilliseconds}ms")

        var secondValue: Any? = null
        val secondTimed = measureTime { secondValue = second() }
        println("Part 2: $secondValue, took ${secondTimed.inWholeMilliseconds}ms")

        println("Together took ${firstTimed.inWholeMilliseconds + secondTimed.inWholeMilliseconds}ms")
    }

    val inputReader: InputStreamReader
        get() = Day::class.java.getResourceAsStream("/input_${day.toString().padStart(2, '0')}.txt")!!.reader()

    val inputLines: Collection<String>
        get() = inputReader.readLines()

    val input: String
        get() = inputReader.readText()
}

fun day(day: Int, body: Day.() -> Unit) = Day(day).apply(body)