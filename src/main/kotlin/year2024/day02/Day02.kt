package year2024.day02

import readInput
import kotlin.math.abs

fun part1(input: List<String>): Int {
    return input.count { row ->
        val diffs = row.split(" ")
            .windowed(2)
            .map { it[1].toInt() - it[0].toInt() }
        diffs.all { abs(it) in 1..3 && it * diffs[0] > 0 }
    }
}

fun part2(input: List<String>): Int {
    return input.count { row ->
        val ints = row.split(" ").map { it.toInt() }
        val diffs = ints
            .windowed(2)
            .map { it[1] - it[0] }
        diffs.all { abs(it) in 1..3 && it * diffs[0] > 0 } || ints.indices.any { dropped ->
            val droppedDiffs = ints.toMutableList().filterIndexed { index, _ -> index != dropped }.windowed(2).map { it[1] - it[0] }
            droppedDiffs.all { abs(it) in 1..3 && it * droppedDiffs[0] > 0 }
        }
    }
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2024/day02/Day02_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("src/main/kotlin/year2024/day02/Day02")
    println(part1(input))
    println(part2(input))
}
