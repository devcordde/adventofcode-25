package fyi.pauli.aoc.days

import fyi.pauli.aoc.day

val day3 = day(3) {

    first = {
        inputLines.sumOf { bank ->
            bank.withIndex()
                .flatMap { (index, first) ->
                    bank
                        .drop(index + 1)
                        .map { second -> "$first$second".toInt() }
                }.max()
        }
    }

    second = {
        inputLines.sumOf { bank ->
            bank.withIndex()
                .runningFold(mutableListOf<Char>()) { stack, (index, digit) ->
                    stack.apply {
                        while (isNotEmpty() && last() < digit && size + bank.length - index > 12) {
                            removeLast()
                        }

                        add(digit)
                    }
                }
                .last()
                .take(12)
                .joinToString("")
                .toLong()
        }
    }
}