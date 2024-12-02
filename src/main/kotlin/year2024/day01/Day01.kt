package year2024.day01

import readInput
import kotlin.math.abs

fun part1(input: List<String>): Int {
    var (left, right) = input.map { it.split("   ") }.map { it[0].toInt() to it[1].toInt() }.unzip()
    left = left.sorted()
    right = right.sorted()
    return left.mapIndexed { index, i -> abs(right[index] - i) }.sum()
}

fun part2(input: List<String>): Long {
    val (left, right) = input.map { it.split("   ") }.map { it[0].toLong() to it[1].toLong() }.unzip()
    return left.sumOf { it * right.count { i -> it == i } }
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2024/day01/Day01_test")
    check(part1(testInput) == 11)
    check(part2(testInput) == 31L)

    val input = readInput("src/main/kotlin/year2024/day01/Day01")
    println(part1(input))
    println(part2(input))
}
