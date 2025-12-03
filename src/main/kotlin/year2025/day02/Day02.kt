package year2025.day02

import check
import readInput

fun part1(input: List<String>): Long {
    return input.first()
        .split(",")
        .flatMap { (it.split("-")[0].toLong()..it.split("-")[1].toLong()).toList() }
        .filter {
            it.toString().substring(0, it.toString().length / 2) == it.toString()
                .substring(it.toString().length / 2, it.toString().length)
        }
        .sum()
}

fun part2(input: List<String>): Long {
    return input.first()
        .split(",")
        .flatMap { (it.split("-")[0].toLong()..it.split("-")[1].toLong()).toList() }
        .filter {
            val s = it.toString()
            var invalid = false
            for (i in 1..(s.length / 2)) {
                invalid = invalid || (s.substring(0, i).repeat(s.length / i) == s)
            }
            invalid
        }
        .sum()
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2025/day02/Day02_test")
    check(part1(testInput), 1227775554)
    check(part2(testInput), 4174379265)

    val input = readInput("src/main/kotlin/year2025/day02/Day02")
    println(part1(input))
    println(part2(input))
}
