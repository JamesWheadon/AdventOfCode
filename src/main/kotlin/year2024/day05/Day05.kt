package year2024.day05

import readInput

fun part1(input: List<String>): Int {
    val rules = getRules(input)
    return input.subList(input.indexOfFirst { it.length < 3 } + 1, input.size)
        .map { order -> order.split(",").map { it.toInt() } }
        .filter { order -> !isOutOfOrder(order, rules) }
        .sumOf { it[it.size / 2] }
}

fun part2(input: List<String>): Int {
    val rules = getRules(input)
    val outOfOrder = input.subList(input.indexOfFirst { it.length < 3 } + 1, input.size)
        .map { order -> order.split(",").map { it.toInt() } }
        .filter { order -> isOutOfOrder(order, rules) }
    return outOfOrder.map { ordered ->
        ordered.sortedWith { o1, o2 ->
            if (rules.firstOrNull { it.first == o1 && it.second == o2 } != null) {
                -1
            } else if (rules.firstOrNull { it.second == o1 && it.first == o1 } != null) {
                1
            } else {
                0
            }
        }
    }.sumOf { it[it.size / 2] }
}

fun isOutOfOrder(order: List<Int>, rules: List<Pair<Int, Int>>): Boolean {
    for (i in order.indices) {
        for (j in i..<order.size) {
            if (rules.any { it.first == order[j] && it.second == order[i] }) {
                return true
            }
        }
    }
    return false
}

fun getRules(input: List<String>): List<Pair<Int, Int>> {
    return input.subList(0, input.indexOfFirst { it.length < 3 })
        .map { rule ->
            val split = rule.split("|")
            split[0].toInt() to split[1].toInt()
        }
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2024/day05/Day05_test")
    check(part1(testInput) == 143)
    check(part2(testInput) == 123)

    val input = readInput("src/main/kotlin/year2024/day05/Day05")
    println(part1(input))
    println(part2(input))
}
