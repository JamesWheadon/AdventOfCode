package year2025.day12

import readInput

fun part1(input: List<String>): Int {
    return input.takeLastWhile { it.isNotEmpty() }.count { puzzle ->
        val grid = puzzle.split(": ").first().split("x").map { it.toInt() }.map { it / 3 }.reduce { acc, i -> acc * i }
        val shapes = puzzle.split(": ").last().split(" ").map { it.toInt() }.reduce { acc, i -> acc + i }
        grid >= shapes
    }
}

fun main() {
    println(part1(readInput("src/main/kotlin/year2025/day12/Day12")))
}
