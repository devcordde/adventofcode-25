package fyi.pauli.aoc.days

import fyi.pauli.aoc.day

val day6 = day(6) {
    val trimmed = inputLines.map { it.split(" ").map(String::trim).filterNot(String::isEmpty) }.toMutableList()
    val operators = trimmed.removeLast()
    val numbers = (0..<trimmed.maxOf(List<String>::size))
        .map { column ->
            trimmed.mapNotNull { row -> row.getOrNull(column)?.toLongOrNull() }
        }

    val calc: (String, Long, Long) -> Long = { op, first, second ->
        when (op) {
            "+" -> first + second
            "*" -> first * second
            else -> 0
        }
    }

    first = {
        numbers.mapIndexed { index, nums ->
            nums.reduce { first, second ->
                calc(operators[index], first, second)
            }
        }.sum()
    }

    second = {
        (inputLines.first().indices).reversed()
            .map { col -> inputLines.joinToString("") { it.getOrNull(col)?.toString() ?: " " } }
            .fold(mutableListOf(mutableListOf<String>())) { problems, col ->
                problems.apply {
                    if (col.isBlank()) add(mutableListOf())
                    else last() += col
                }
            }
            .filter(MutableList<String>::isNotEmpty)
            .sumOf { problem ->
                val nums = problem.mapNotNull { col ->
                    col.filter(Char::isDigit).takeIf(String::isNotEmpty)?.toLong()
                }

                val op = problem.firstNotNullOf { col -> col.trimEnd().lastOrNull()?.takeIf { it == '+' || it == '*' } }

                nums.reduce { first, second -> calc(op.toString(), first, second) }
            }
    }


}