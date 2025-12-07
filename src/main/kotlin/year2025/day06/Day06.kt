package year2025.day06

import check
import readInput
import kotlin.math.min

fun part1(input: List<String>): Long {
    val problems = input.map { it.split(" ").filter { it.isNotEmpty() } }
    var t = 0L
    for (i in problems.first().indices) {
        val problem = problems.map { it[i] }.dropLast(1).map { it.toLong() }
        t += problem.reduce { acc, s ->
            if (problems.last()[i] == "+") {
                acc + s
            } else {
                acc * s
            }
        }
    }
    return t
}

fun part2(input: List<String>): Long {
    var previous = 0
    var next = 0
    var t = 0L
    while (next < input.maxOf { it.length }) {
        while (input.any { (it.getOrNull(next) ?: ' ') != ' ' }) {
            next++
        }
        var problem = input.map { it.substring(previous, min(it.length, next)) }
        problem = problem.map { it.padEnd(problem.maxOf { it.length }, ' ') }
        next++
        previous = next
        val numbers = mutableListOf<Long>()
        for (j in problem.first().indices) {
            numbers.add(problem.joinToString("") { it[j].toString() }.filter { it.isDigit() }.toLong())
        }
        t += numbers.reduce { acc, s ->
            if (problem.last().contains("+")) {
                acc + s
            } else {
                acc * s
            }
        }
    }
    return t
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2025/day06/Day06_test")
    check(part1(testInput), 4277556)
    check(part2(testInput), 3263827)

    val input = readInput("src/main/kotlin/year2025/day06/Day06")
    println(part1(input))
    println(part2(input))
}
