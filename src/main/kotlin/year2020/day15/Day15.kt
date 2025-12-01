package year2020.day15

import check
import readInput

fun part1(input: List<String>): Int {
    val spoken = input[0].split(",").map { it.toInt() }.toMutableList()
    while (spoken.size != 2020) {
        val previous = spoken.last()
        if (spoken.count { it == previous } == 1) {
            spoken.add(0)
        } else {
            spoken.add(spoken.size - spoken.subList(0, spoken.size - 1).lastIndexOf(previous) - 1)
        }
    }
    return spoken.last()
}

fun part2(input: List<String>): Int {
    val numbers = mutableMapOf<Int, MutableList<Int>>()
    val spoken = input[0].split(",").map { it.toInt() }
    spoken.toMutableList().forEachIndexed { index, i ->
        if (numbers.containsKey(i)) {
            numbers[i]!!.add(index)
        } else {
            numbers[i] = mutableListOf(index)
        }
    }
    var previous = spoken.last()
    var turn = spoken.size
    while (turn != 30000000) {
        if (numbers[previous]!!.size == 1) {
            previous = 0
        } else {
            val ints = numbers[previous]!!
            previous = ints.last() - ints[ints.size - 2]
        }
        if (numbers.containsKey(previous)) {
            numbers[previous]!!.add(turn)
        } else {
            numbers[previous] = mutableListOf(turn)
        }
        turn++
    }
    return previous
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2020/day15/Day15_test")
    check(part1(testInput), 436)
    check(part2(testInput), 175594)

    val input = readInput("src/main/kotlin/year2020/day15/Day15")
    println(part1(input))
    println(part2(input))
}
