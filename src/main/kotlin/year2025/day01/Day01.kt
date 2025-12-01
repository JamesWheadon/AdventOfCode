package year2025.day01

import check
import readInput

fun part1(input: List<String>): Int {
    var dial = 50
    var count = 0
    input.forEach { turn ->
        val amount = turn.filter { it.isDigit() }.toInt()
        dial = if (turn.startsWith('L')) {
            (dial - amount) % 100
        } else {
            (dial + amount) % 100
        }
        if (dial == 0) count++
    }
    return count
}

fun part2(input: List<String>): Int {
    var dial = 50
    var count = 0
    val dialRange = 0..99
    input.forEach { turn ->
        val amount = turn.filter { it.isDigit() }.toInt()
        if (turn.startsWith('L')) {
            if (dial == 0) dial = 100
            dial = (dial - amount)
            while (!dialRange.contains(dial)) {
                dial+=100
                count++
            }
            if (dial == 0) count++
        } else {
            dial = (dial + amount)
            while (!dialRange.contains(dial)) {
                dial-=100
                count++
            }
        }
    }
    return count
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2025/day01/Day01_test")
    check(part1(testInput), 3)
    check(part2(testInput), 6)

    val input = readInput("src/main/kotlin/year2025/day01/Day01")
    println(part1(input))
    println(part2(input))
}
