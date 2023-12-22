package year2023.day06

import readInput
import kotlin.math.max

fun part1(input: List<String>): Long {
    val values = input.map { row -> row.split(" ").filter { it.contains("[0-9]".toRegex()) }.map { it.toLong() } }
    val distanceTimes = values[0].zip(values[1]).toMap()
    return distanceTimes.map { race ->
        getPossibleWins(race)
    }.reduce { acc, i -> acc * i }
}

fun part2(input: List<String>): Long {
    val values = input.map { row ->
        val num = row.filter { it.isDigit() }
        num.toLong()
    }
    return getPossibleWins(mapOf(values[0] to values[1]).entries.first())
}

private fun getPossibleWins(race: Map.Entry<Long, Long>): Long {
    var current = race.key / 2
    var gap = race.key / 4
    while (!(current * (race.key - current) <= race.value && (current + 1) * (race.key - current - 1) > race.value)) {
        if (current * (race.key - current) <= race.value) {
            current += gap
        } else {
            current -= gap
        }
        gap = max(gap / 2, 1)
    }
    val lower = current
    current = race.key / 2
    gap = race.key / 4
    while (!(current * (race.key - current) > race.value && (current + 1) * (race.key - current - 1) <= race.value)) {
        if (current * (race.key - current) > race.value) {
            current += gap
        } else {
            current -= gap
        }
        gap = max(gap / 2, 1)
    }
    return current - lower
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2023/day06/Day06_test")
    check(part1(testInput) == 288L)
    check(part2(testInput) == 71503L)

    val input = readInput("src/main/kotlin/year2023/day06/Day06")
    println(part1(input))
    println(part2(input))
}
