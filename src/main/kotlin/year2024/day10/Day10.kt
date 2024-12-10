package year2024.day10

import readInput
import kotlin.math.max
import kotlin.math.min

fun part1(input: List<String>): Int {
    var trails = mutableSetOf<Trail>()
    getStarts(input) { r, c -> trails.add(Trail(Location(r, c), Location(r, c))) }
    var current = '1'
    while (current <= '9') {
        trails = trails.flatMap { trail ->
            val next = mutableSetOf<Trail>()
            findNext(input, current, trail.current.row, trail.current.column) { i, j -> next.add(trail.copy(current = Location(i, j))) }
            next
        }.toMutableSet()
        current += 1
    }
    return trails.size
}

fun part2(input: List<String>): Int {
    var trails = mutableListOf<Pair<Int, Int>>()
    getStarts(input) { r, c -> trails.add(r to c) }
    var current = '1'
    while (current <= '9') {
        trails = trails.flatMap { loc ->
            val next = mutableListOf<Pair<Int, Int>>()
            findNext(input, current, loc.first, loc.second) { i, j -> next.add(i to j) }
            next
        }.toMutableList()
        current += 1
    }
    return trails.size
}

private fun getStarts(
    input: List<String>,
    collectStarts: (Int, Int) -> Boolean
) {
    input.forEachIndexed { r, row ->
        row.forEachIndexed { c, col ->
            if (col == '0') {
                collectStarts(r, c)
            }
        }
    }
}

private fun findNext(
    input: List<String>,
    current: Char,
    row: Int,
    column: Int,
    collectNext: (Int, Int) -> Boolean
) {
    for (i in max(0, row - 1)..min(input.size - 1, row + 1)) {
        for (j in max(0, column - 1)..min(input[0].length - 1, column + 1)) {
            if ((i == row || j == column) && input[i][j] == current) {
                collectNext(i, j)
            }
        }
    }
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2024/day10/Day10_test")
    check(part1(testInput) == 36)
    check(part2(testInput) == 81)

    val input = readInput("src/main/kotlin/year2024/day10/Day10")
    println(part1(input))
    println(part2(input))
}

data class Location(val row: Int, val column: Int)
data class Trail(val start: Location, val current: Location)
