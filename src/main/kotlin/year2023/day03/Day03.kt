package year2023.day03

import readInput
import kotlin.math.abs

fun part1(input: List<String>): Int {
    val symbolIndexes = mutableListOf<Pair<Int, Int>>()
    input.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { index, c ->
            if (!c.isLetterOrDigit() && c != '.') {
                symbolIndexes.add(
                    Pair(
                        rowIndex,
                        index,
                    ),
                )
            }
        }
    }
    return input.sumOf { row ->
        val rowIndex = input.indexOf(row)
        val numbers = getNumbersFromRow(row)
        var cleanRow = row
        numbers.filter { number ->
            val adjacent = adjacent(rowIndex, cleanRow.indexOf(number), number.length, symbolIndexes)
            cleanRow = cleanRow.replaceFirst(number, ".".repeat(number.length))
            adjacent
        }.sumOf { it.toInt() }
    }
}

fun part2(input: List<String>): Int {
    val gearIndexes = mutableListOf<Pair<Int, Int>>()
    input.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { index, c -> if (c == '*') gearIndexes.add(Pair(rowIndex, index)) }
    }
    val numberPositions = input.map { row ->
        val rowIndex = input.indexOf(row)
        val numbers = getNumbersFromRow(row)
        var cleanRow = row
        numbers.map { number ->
            val triple = Triple(rowIndex, cleanRow.indexOf(number), number)
            cleanRow = cleanRow.replaceFirst(number, ".".repeat(number.length))
            triple
        }
    }.flatten()
    return gearIndexes.sumOf { gear ->
        val adjacentNumbers = numberPositions.filter { number ->
            adjacent(
                number.first,
                number.second,
                number.third.length,
                listOf(gear),
            )
        }.map { it.third.toInt() }
        if (adjacentNumbers.size == 2) {
            adjacentNumbers[0] * adjacentNumbers[1]
        } else {
            0
        }
    }
}

private fun getNumbersFromRow(row: String) = row.replace(("[^0-9.]").toRegex(), ".").split(".").filter { it != "" }
    .map { it.filter { c -> c.isLetterOrDigit() } }

fun adjacent(rowIndex: Int, index: Int, length: Int, symbolIndexes: List<Pair<Int, Int>>): Boolean {
    return symbolIndexes.any { abs(it.first - rowIndex) <= 1 && it.second in index - 1..index + length }
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2023/day03/Day03_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("src/main/kotlin/year2023/day03/Day03")
    println(part1(input))
    println(part2(input))
}
