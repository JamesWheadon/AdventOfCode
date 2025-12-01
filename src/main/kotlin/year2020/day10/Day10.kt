package year2020.day10

import check
import readInput

fun part1(input: List<String>): Int {
    return input.asSequence()
        .map { it.toInt() }
        .sorted()
        .windowed(2, 1)
        .map { it[1] - it[0] }
        .groupingBy { it }
        .eachCount()
        .let { (it[1]!! + 1) * (it[3]!! + 1) }
}

fun part2(input: List<String>): Long {
    val adaptors = input.map { it.toInt() }.plus(0)
    val adaptorList = adaptors.map { adaptor -> Adaptor(adaptor, adaptors.filter { it - adaptor in 1..3 }, if (adaptor == 0) 1 else 0) }
    adaptorList.sortedBy { it.value }.drop(1).forEach {
        it.pathsTo = adaptorList.filter { other -> other.nextConnections.contains(it.value) }.sumOf { other -> other.pathsTo }
    }
    return adaptorList.maxBy { it.value }.pathsTo
}

data class Adaptor(val value: Int, val nextConnections: List<Int>, var pathsTo: Long)

fun main() {
    val testInput = readInput("src/main/kotlin/year2020/day10/Day10_test")
    check(part1(testInput), 220)
    check(part2(testInput), 19208)

    val input = readInput("src/main/kotlin/year2020/day10/Day10")
    println(part1(input))
    println(part2(input))
}
