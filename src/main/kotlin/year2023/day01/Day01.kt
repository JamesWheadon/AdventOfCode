package year2023.day01

import readInput

fun part1(input: List<String>): Int {
    return input.sumOf { row ->
        val numbers = row.filter { c: Char -> c.isDigit() }
        numbers.first().digitToInt() * 10 + numbers.last().digitToInt()
    }
}

fun part2(input: List<String>): Int {
    val numberWords = mapOf("one" to 1, "two" to 2, "three" to 3, "four" to 4, "five" to 5, "six" to 6, "seven" to 7, "eight" to 8, "nine" to 9)
    return input.sumOf { row ->
        numberWords.map { row.indexOf(it.key) to it.value }.plus(row.indexOfFirst { it.isDigit() } to row.firstOrNull { it.isDigit() }?.digitToInt()).filter { it.first >= 0 }.minBy { it.first }.second!! * 10 +
            numberWords.map { row.lastIndexOf(it.key) to it.value }.plus(row.indexOfLast { it.isDigit() } to row.lastOrNull { it.isDigit() }?.digitToInt()).filter { it.first >= 0 }.maxBy { it.first }.second!!
    }
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2023/day01/Day01_test")
    val testInputPart2 = readInput("src/main/kotlin/year2023/day01/Day01_test_part2")
    check(part1(testInput) == 142)
    check(part2(testInputPart2) == 281)

    val input = readInput("src/main/kotlin/year2023/day01/Day01")
    println(part1(input))
    println(part2(input))
}
