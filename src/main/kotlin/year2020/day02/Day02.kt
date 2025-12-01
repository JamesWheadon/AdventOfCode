package year2020.day02

import readInput

fun part1(input: List<String>): Int {
    return input.sumOf { row ->
        val parts = row.split(" ")
        val range = parts[0].split("-")[0].toInt()..parts[0].split("-")[1].toInt()
        val char = parts[1].first { it.isLetter() }
        if (parts[2].count { it == char } in range) {
            "1".toInt()
        } else {
            0
        }
    }
}

fun part2(input: List<String>): Int {
    return input.sumOf { row ->
        val parts = row.split(" ")
        val range = parts[0].split("-")[0].toInt() - 1 to parts[0].split("-")[1].toInt() - 1
        val char = parts[1].first { it.isLetter() }
        if ((parts[2][range.first] == char).xor(parts[2][range.second] == char)) {
            "1".toInt()
        } else {
            0
        }
    }
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2020/day02/Day02_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 1)

    val input = readInput("src/main/kotlin/year2020/day02/Day02")
    println(part1(input))
    println(part2(input))
}
