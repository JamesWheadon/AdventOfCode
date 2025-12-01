package year2020.day06

import check
import readInput

fun part1(input: List<String>): Int {
    return input.fold(mutableListOf(mutableListOf<String>())) { acc, s ->
        if (s.isEmpty()) {
            acc.add(mutableListOf())
        } else {
            acc.last().add(s)
        }
        acc
    }.sumOf { group ->
        group.flatMap { it.toCharArray().asList() }.distinct().count()
    }
}

fun part2(input: List<String>): Int {
    return input.fold(mutableListOf(mutableListOf<String>())) { acc, s ->
        if (s.isEmpty()) {
            acc.add(mutableListOf())
        } else {
            acc.last().add(s)
        }
        acc
    }.sumOf { group ->
        group.flatMap { it.toCharArray().asList().distinct() }.groupingBy { it }.eachCount().count { it.value == group.size }
    }
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2020/day06/Day06_test")
    check(part1(testInput), 11)
    check(part2(testInput), 6)

    val input = readInput("src/main/kotlin/year2020/day06/Day06")
    println(part1(input))
    println(part2(input))
}
