package year2020.day09

import check
import readInput

fun part1(input: List<Long>, previous: Int): Long {
    input.subList(previous, input.size).forEachIndexed { index, l ->
        val possible = input.subList(index, index + previous)
        if (possible.none { possible.contains(l - it) }) {
            return l
        }
    }
    return 0
}

fun part2(input: List<Long>, previous: Int): Long {
    val invalid = part1(input, previous)
    var step = 2
    while (true) {
        input.windowed(step).firstOrNull { it.sum() == invalid }?.let {
            return it.min() + it.max()
        } ?: run { step += 1 }
    }
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2020/day09/Day09_test")
    check(part1(testInput.map { it.toLong() }, 5), 127)
    check(part2(testInput.map { it.toLong() }, 5), 62)

    val input = readInput("src/main/kotlin/year2020/day09/Day09")
    println(part1(input.map { it.toLong() }, 25))
    println(part2(input.map { it.toLong() }, 25))
}
