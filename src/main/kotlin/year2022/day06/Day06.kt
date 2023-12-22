package year2022.day06

import readInput

fun part1(input: List<String>): Int {
    input[0].toList().windowed(4, 1).forEachIndexed { index, chars ->
        if (chars.toSet().size == 4) {
            return index + 4
        }
    }
    return 0
}

fun part2(input: List<String>): Int {
    input[0].toList().windowed(14, 1).forEachIndexed { index, chars ->
        if (chars.toSet().size == 14) {
            return index + 14
        }
    }
    return 0
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("src/main/kotlin/year2022/day06/Day06_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 19)

    val input = readInput("src/main/kotlin/year2022/day06/Day06")
    println(part1(input))
    println(part2(input))
}
