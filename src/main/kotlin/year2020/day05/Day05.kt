package year2020.day05

import check
import readInput

fun part1(input: List<String>): Int {
    return input.maxOf { pass ->
        var row = 0 to 127
        var column = 0 to 7
        pass.forEach {
            when (it) {
                'F' -> row = row.copy(second = row.second - (row.second - row.first + 1) / 2)
                'B' -> row = row.copy(first = row.first + (row.second - row.first + 1) / 2)
                'L' -> column = column.copy(second = column.second - (column.second - column.first + 1) / 2)
                'R' -> column = column.copy(first = column.first + (column.second - column.first + 1) / 2)
            }
        }
        row.first * 8 + column.first
    }
}

fun part2(input: List<String>): Int {
    return input.map { pass ->
        var row = 0 to 127
        var column = 0 to 7
        pass.forEach {
            when (it) {
                'F' -> row = row.copy(second = row.second - (row.second - row.first + 1) / 2)
                'B' -> row = row.copy(first = row.first + (row.second - row.first + 1) / 2)
                'L' -> column = column.copy(second = column.second - (column.second - column.first + 1) / 2)
                'R' -> column = column.copy(first = column.first + (column.second - column.first + 1) / 2)
            }
        }
        row.first * 8 + column.first
    }
        .sorted()
        .windowed(2)
        .first { it[1] - it[0] != 1 }
        .average()
        .toInt()
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2020/day05/Day05_test")
    check(part1(testInput), 820)

    val input = readInput("src/main/kotlin/year2020/day05/Day05")
    println(part1(input))
    println(part2(input))
}
