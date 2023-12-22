package year2021.day03

import readInput
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToInt

fun part1(input: List<String>): Int {
    val totals = MutableList(input[0].length) { 0.0 }
    input.map { it.toList() }
        .forEach { it.forEachIndexed { index, element -> totals[index] += element.toString().toDouble() } }
    val gamma = MutableList(input[0].length) { 0 }
    val epsilon = MutableList(input[0].length) { 0 }
    totals.forEachIndexed { index, element ->
        gamma[index] = (((element / input.size).roundToInt()) * 2.0.pow(totals.size - 1 - index)).toInt()
        epsilon[index] = (((1 - element / input.size).roundToInt()) * 2.0.pow(totals.size - 1 - index)).toInt()
    }
    return gamma.sum() * epsilon.sum()
}

fun part2(input: List<String>): Int {
    val diagnostics = input.map { it.toCharArray().map(Character::getNumericValue) }
    var oxygenResult = 0
    var carbonResult = 0
    val lists = listOf(
        MutableList(input[0].length) { 0 }, MutableList(input[0].length) { 0 }
    )
    for (gas in lists) {
        for (i in gas.indices) {
            val gasValues = diagnostics.filter {
                it.subList(0, i).toTypedArray().contentEquals(gas.subList(0, i).toTypedArray())
            }
            if (gasValues.size == 1) {
                gas.clear()
                gas.addAll(gasValues[0])
                break
            }
            val mostCommon = (gasValues.sumOf { it[i].toString().toDouble() } / gasValues.size).roundToInt()
            gas[i] = if (lists.indexOf(gas) == 0) {
                mostCommon
            } else {
                abs(mostCommon - 1)
            }
        }
    }
    for (i in lists[0].indices) {
        oxygenResult += (lists[0][i] * 2.0.pow(input[0].length - 1 - i)).toInt()
        carbonResult += (lists[1][i] * 2.0.pow(input[0].length - 1 - i)).toInt()
    }
    return oxygenResult * carbonResult
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2021/day03/Day03_test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("src/main/kotlin/year2021/day03/Day03")
    println(part1(input))
    println(part2(input))
}
