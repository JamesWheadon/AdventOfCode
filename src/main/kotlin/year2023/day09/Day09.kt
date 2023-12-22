package year2023.day09

import readInput

fun part1(input: List<String>): Int {
    return input.sumOf { row ->
        getSequence(row).reversed().sumOf { it.last() }
    }
}

fun part2(input: List<String>): Int {
    return input.sumOf { row ->
        getSequence(row).reversed().map { it.first() }.reduce { acc, int -> int - acc }
    }
}

private fun getSequence(row: String): MutableList<List<Int>> {
    val sequence = mutableListOf(row.split(" ").map { it.toInt() })
    while (sequence.last().any { it != 0 }) {
        sequence.add(sequence.last().windowed(2).map { it[1] - it[0] })
    }
    return sequence
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2023/day09/Day09_test")
    check(part1(testInput) == 114)
    check(part2(testInput) == 2)

    val input = readInput("src/main/kotlin/year2023/day09/Day09")
    println(part1(input))
    println(part2(input))
}
