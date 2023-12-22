package year2021.day07

import readInput
import kotlin.math.abs
import kotlin.math.round

fun part1(input: List<String>): Int {
    val positions = input[0].split(",").map { it.toInt() }.sortedDescending()
    val median = positions[round((positions.size / 2).toDouble()).toInt()]
    var distance = 0
    positions.forEach { distance += abs(it - median) }
    return distance
}

fun part2(input: List<String>): Int? {
    val positions = input[0].split(",").map { it.toInt() }.toList()
    val distances = mutableListOf<Int>()
    for (guess in positions.minOrNull()?.rangeTo(positions.maxOrNull()!!)!!) {
        var distance = 0
        positions.forEach {
            val difference = abs(it - guess)
            distance += ((difference * (difference + 1)) / 2)
        }
        distances.add(distance)
    }
    return distances.minOrNull()
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2021/day07/Day07_test")
    check(part1(testInput) == 37)
    check(part2(testInput) == 168)

    val input = readInput("src/main/kotlin/year2021/day07/Day07")
    println(part1(input))
    println(part2(input))
}
