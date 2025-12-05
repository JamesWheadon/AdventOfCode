package year2025.day04

import check
import readInput
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun part1(input: List<String>): Int {
    return input.mapIndexed { y, s ->
        s.mapIndexed { x, c ->
            if (c == '@') {
                var p = 0
                for (i in max(0, y - 1)..min(input.size - 1, y + 1)) {
                    for (j in max(0, x - 1)..min(input.first().length - 1, x + 1)) {
                        if (input[i][j] == '@') {
                            p++
                        }
                    }
                }
                if (p < 5) {
                    c
                } else {
                    null
                }
            } else {
                null
            }
        }
    }.flatten()
        .filterNotNull()
        .size
}

fun part2(input: List<String>): Int {
    val rolls = input.mapIndexed { y, s ->
        s.mapIndexed { x, c ->
            if (c == '@') {
                Paper(x, y)
            } else {
                null
            }
        }
    }
        .flatten()
        .filterNotNull()
        .toMutableList()
    rolls.forEach { roll ->
        roll.neighbours = rolls.filter { it != roll }.filter { abs(it.x - roll.x) <= 1 && abs(it.y - roll.y) <= 1 }
    }
    val start = rolls.size
    while (true) {
        val toRemove = rolls.filter { it.neighbours.size < 4 }
        if (toRemove.isEmpty()) {
            break
        }
        rolls.removeAll(toRemove)
        rolls.forEach { roll ->
            roll.neighbours = rolls.filter { it != roll }.filter { abs(it.x - roll.x) <= 1 && abs(it.y - roll.y) <= 1 }
        }
    }
    return start - rolls.size
}

data class Paper(val x: Int, val y: Int) {
    var neighbours = listOf<Paper>()
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2025/day04/Day04_test")
    check(part1(testInput), 13)
    check(part2(testInput), 43)

    val input = readInput("src/main/kotlin/year2025/day04/Day04")
    println(part1(input))
    println(part2(input))
}
