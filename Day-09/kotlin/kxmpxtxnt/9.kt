package fyi.pauli.aoc.days

import fyi.pauli.aoc.day
import kotlin.math.abs

val day9 = day(9) {
    first = {
        inputLines
            .map { it.trim().split(",") }
            .map { (x, y) -> x.toLong() to y.toLong() }
            .let { positions ->
                positions.flatMapIndexed { index, pos ->
                    positions.drop(index + 1).map { other ->
                        (abs(pos.first - other.first) + 1) * (abs(pos.second - other.second) + 1)
                    }
                }
            }.max()
    }

    second = {
        inputLines
            .map { it.split(",").map(String::toLong).let { (x, y) -> x to y } }
            .let { tiles ->
                fun isInsideOrOnBoundary(x: Long, y: Long): Boolean =
                    tiles.indices.any { i ->
                        val (x1, y1) = tiles[i]
                        val (x2, y2) = tiles[(i + 1) % tiles.size]
                        (x1 == x2 && x == x1 && y in minOf(y1, y2)..maxOf(y1, y2)) ||
                            (y1 == y2 && y == y1 && x in minOf(x1, x2)..maxOf(x1, x2))
                    } || tiles.indices.count { i ->
                        val (x1, y1) = tiles[i]
                        val (x2, y2) = tiles[(i + 1) % tiles.size]
                        (y1 > y) != (y2 > y) && x < x1 + (y - y1).toDouble() / (y2 - y1) * (x2 - x1)
                    } % 2 == 1

                val xCoords = tiles.map { it.first }.toSortedSet()
                val yCoords = tiles.map { it.second }.toSortedSet()

                tiles.indices
                    .asSequence()
                    .flatMap { i -> (i + 1 until tiles.size).map { j -> i to j } }
                    .map { (i, j) -> tiles[i] to tiles[j] }
                    .filter { (p1, p2) -> p1.first != p2.first && p1.second != p2.second }
                    .map { (p1, p2) ->
                        val minX = minOf(p1.first, p2.first)
                        val maxX = maxOf(p1.first, p2.first)
                        val minY = minOf(p1.second, p2.second)
                        val maxY = maxOf(p1.second, p2.second)
                        Triple(
                            minX to maxX,
                            minY to maxY,
                            listOf(minX to minY, minX to maxY, maxX to minY, maxX to maxY)
                        )
                    }
                    .filter { (_, _, corners) -> corners.all { (x, y) -> isInsideOrOnBoundary(x, y) } }
                    .filter { (xRange, yRange, _) ->
                        xCoords.filter { it in xRange.first..xRange.second }
                            .all { x ->
                                isInsideOrOnBoundary(x, yRange.first) && isInsideOrOnBoundary(
                                    x,
                                    yRange.second
                                )
                            }
                    }
                    .filter { (xRange, yRange, _) ->
                        yCoords.filter { it in yRange.first..yRange.second }
                            .all { y ->
                                isInsideOrOnBoundary(xRange.first, y) && isInsideOrOnBoundary(
                                    xRange.second,
                                    y
                                )
                            }
                    }
                    .maxOfOrNull { (xRange, yRange, _) -> (xRange.second - xRange.first + 1L) * (yRange.second - yRange.first + 1L) }
                    ?: 0L
            }
    }
}