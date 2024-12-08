package year2024.day07

import readInput

fun part1(input: List<String>): Long {
    return input.filter { row ->
        val target = row.split(": ")[0].toLong()
        val nums = row.split(": ")[1].split(" ").map { it.toLong() }
        var poss = listOf(nums[0])
        for (i in 1..<nums.size) {
            poss = poss.flatMap { listOf(it + nums[i], it * nums[i]) }
        }
        poss.contains(target)
    }.sumOf { it.split(": ")[0].toLong() }
}

fun part2(input: List<String>): Long {
    return input.filter { row ->
        val target = row.split(": ")[0].toLong()
        val nums = row.split(": ")[1].split(" ").map { it.toLong() }
        var poss = listOf(nums[0])
        for (i in 1..<nums.size) {
            poss = poss.flatMap { listOf(it + nums[i], it * nums[i], (it.toString() + nums[i].toString()).toLong()) }
        }
        poss.contains(target)
    }.sumOf { it.split(": ")[0].toLong() }
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2024/day07/Day07_test")
    check(part1(testInput) == 3749L)
    check(part2(testInput) == 11387L)

    val input = readInput("src/main/kotlin/year2024/day07/Day07")
    println(part1(input))
    println(part2(input))
}
