package year2024.day03

import readInput

fun part1(input: List<String>): Int {
    return Regex("mul\\(([0-9]+),([0-9]+)\\)")
        .findAll(input[0])
        .sumOf { it.groups[1]!!.value.toInt() * it.groups[2]!!.value.toInt() }
}

fun part2(input: List<String>): Int {
    val include = Regex("do\\(\\)|don't\\(\\)")
        .findAll("do()" + input[0])
        .map { Pair(it.range.first, it.value == "do()") }
    return Regex("mul\\(([0-9]+),([0-9]+)\\)")
        .findAll("do()" + input[0])
        .filter { mul ->
            include
                .filter { it.first < mul.range.first }
                .maxBy { it.first }.second
        }
        .sumOf { it.groups[1]!!.value.toInt() * it.groups[2]!!.value.toInt() }
}

fun main() {
    check(part1(readInput("src/main/kotlin/year2024/day03/Day03_test_part1")) == 161)
    check(part2(readInput("src/main/kotlin/year2024/day03/Day03_test_part2")) == 48)

    val input = readInput("src/main/kotlin/year2024/day03/Day03")
    println(part1(input))
    println(part2(input))
}
