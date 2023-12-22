package year2022.day03

import readInput

fun part1(input: List<String>): Int {
    return input.sumOf {
        it.chunked(it.length / 2)
            .map { compartment -> compartment.toSet() }
            .reduce { x, y -> x.intersect(y) }
            .map { common -> if (common.isUpperCase()) common.code - 38 else common.code - 96 }.first()
    }
}

fun part2(input: List<String>): Int {
    return input.chunked(3).map { group ->
        group.map { elf -> elf.toSet() }.reduce { x, y -> x.intersect(y) }
            .map { common -> if (common.isUpperCase()) common.code - 38 else common.code - 96 }.first()
    }.sum()
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("src/main/kotlin/year2022/day03/Day03_test")
    println(part1(testInput))
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput("src/main/kotlin/year2022/day03/Day03")
    println(part1(input))
    println(part2(input))
}
