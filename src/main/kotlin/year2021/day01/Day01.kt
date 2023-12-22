package year2021.day01

import readInput

fun part1(input: List<String>): Int {
    val depths = input.map { it.toInt() }
    var depthIncreases = 0
    for (i in 1..<depths.size) {
        if (depths[i] > depths[i - 1]) {
            depthIncreases++
        }
    }
    return depthIncreases
}

fun part2(input: List<String>): Int {
    val depths = input.map { it.toInt() }
    val movingSums = mutableListOf<Int>()
    for (i in 1..<depths.size - 1) {
        movingSums.add(movingSums.size, depths[i - 1] + depths[i] + depths[i + 1])
    }
    var depthIncreases = 0
    for (i in 1..<movingSums.size) {
        if (movingSums[i] > movingSums[i - 1]) {
            depthIncreases++
        }
    }
    return depthIncreases
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2021/day01/Day01_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 5)

    val input = readInput("src/main/kotlin/year2021/day01/Day01")
    println(part1(input))
    println(part2(input))
}
