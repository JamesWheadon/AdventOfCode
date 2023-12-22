package year2022.day04

import readInput

fun part1(input: List<String>): Int {
    return input.map { pair -> pair.split(",").map { elf -> elf.split("-").map { id -> id.toInt() } } }
        .filter { pair -> (pair[0][0] <= pair[1][0] && pair[0][1] >= pair[1][1]) || (pair[1][0] <= pair[0][0] && pair[1][1] >= pair[0][1]) }.size
}

fun part2(input: List<String>): Int {
    return input.map { pair -> pair.split(",").map { elf -> elf.split("-").map { id -> id.toInt() } } }
        .filter { pair -> (pair[0][0] >= pair[1][0] && pair[0][0] <= pair[1][1]) || (pair[0][1] >= pair[1][0] && pair[0][0] <= pair[1][1]) }.size
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("src/main/kotlin/year2022/day04/Day04_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("src/main/kotlin/year2022/day04/Day04")
    println(part1(input))
    println(part2(input))
}
