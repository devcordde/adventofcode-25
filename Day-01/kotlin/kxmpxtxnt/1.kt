package fyi.pauli.aoc.days

import fyi.pauli.aoc.day

val day1 = day(1) {
    first = {
        inputLines.map { it.first() to it.drop(1).toInt() }
            .fold(50 to 0) { (code, zeros), (dir, amount) ->
                val newCode = ((code + when (dir) {
                    'L' -> -amount
                    else -> amount
                }) % 100 + 100) % 100
                newCode to (zeros + if (newCode == 0) 1 else 0)
            }.second
    }

    second = {
        inputLines
            .map { it.first() to it.drop(1).toInt() }
            .fold(50 to 0) { (code, zeros), (dir, amount) ->
                val delta = when (dir) { 'L' -> -amount else -> amount }
                val newCode = ((code + delta) % 100 + 100) % 100

                val crossings = (0 until amount).count { step ->
                    val pos = ((code + (if (dir == 'L') -step - 1 else step + 1)) % 100 + 100) % 100
                    pos == 0
                }

                newCode to (zeros + crossings)
            }
            .second
    }
}