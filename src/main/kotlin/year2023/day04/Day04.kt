package year2023.day04

import readInput
import kotlin.math.pow

fun part1(input: List<String>): Int {
    return input.sumOf { row ->
        val wins = getWinningNumbers(row)
        if (wins == 0) {
            0
        } else {
            2.0.pow(wins - 1).toInt()
        }
    }
}

fun part2(input: List<String>): Int {
    val cards = input.map { row -> mutableListOf(1, getWinningNumbers(row)) }
    for (i in cards.indices) {
        for (j in 1..cards[i][1]) {
            cards[i + j][0] += cards[i][0]
        }
    }
    return cards.sumOf { it[0] }
}

private fun getWinningNumbers(row: String): Int {
    val numbers = row.split("|")
    val winningNumbers = numbers[0].split(" ").filter { it.contains("[0-9]".toRegex()) }.toSet()
    val ownedNumbers = numbers[1].split(" ").filter { it.contains("[0-9]".toRegex()) }.toSet()
    return winningNumbers.intersect(ownedNumbers).size
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2023/day04/Day04_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("src/main/kotlin/year2023/day04/Day04")
    println(part1(input))
    println(part2(input))
}
