package year2020.day13

import check
import readInput

fun part1(input: List<String>): Int {
    val time = input[0].toInt()
    return input[1].split(",").filter { it != "x" }.map { it.toInt() }.minBy { it - time % it }.let { (it - time % it) * it }
}

fun part2(input: List<String>): Long {
    val buses = input[1].split(",").mapIndexedNotNull { index, b ->
        if (b == "x") {
            null
        } else {
            index.toLong() to b.toLong()
        }
    }
    val product = buses.fold(1L) { a, b -> a * b.second }
    var lowest = -buses.sumOf {
        val m = product / it.second
        val mMod = m % it.second
        var y = 1L
        while (mMod * y % it.second != 1L) {
            y++
        }
        it.first * m * y
    } % product
    while (lowest < 0) {
        lowest += product
    }
    return lowest
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2020/day13/Day13_test")
    check(part1(testInput), 295)
    check(part2(testInput), 1068781)

    val input = readInput("src/main/kotlin/year2020/day13/Day13")
    println(part1(input))
    println(part2(input))
}
