package year2025.day03

import check
import readInput

fun part1(input: List<String>): Int {
    return input.sumOf { line ->
        val batteries = line.map { it.digitToInt() }
        val max = batteries.max()
        val afterMax = batteries.subList(batteries.indexOf(max) + 1, batteries.size)
        if (afterMax.isEmpty()) {
            batteries.dropLast(1).max() * 10 + max
        } else {
            max * 10 + afterMax.max()
        }
    }
}

fun part2(input: List<String>): Long {
    return input.sumOf { line ->
        var batteries = line.map { it.digitToInt() }
        val number = mutableListOf<Int>()
        while (number.size < 12) {
            val max = batteries.subList(0, batteries.size - (11 - number.size)).max()
            number.add(max)
            batteries = batteries.subList(batteries.indexOf(max) + 1, batteries.size)
        }
        number.joinToString("").toLong()
    }
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2025/day03/Day03_test")
    check(part1(testInput), 357)
    check(part2(testInput), 3121910778619)

    val input = readInput("src/main/kotlin/year2025/day03/Day03")
    println(part1(input))
    println(part2(input))
}
