package year2021.day05

import readInput
import kotlin.math.max
import kotlin.math.min

fun part1(input: List<String>): Int {
    val grid = getGrid(input)
    input.forEach {
        val coords = it.replace(" -> ", ",").split(",").map { elem -> elem.toInt() }
        if (coords[0] == coords[2]) {
            firstThirdMatch(coords, grid)
        } else if (coords[1] == coords[3]) {
            secondFourthMatch(coords, grid)
        }
    }
    return getCount(grid)
}

private fun getGrid(input: List<String>): MutableList<MutableList<Int>> {
    var gridSize = 0
    input.forEach {
        val coords = it.replace(" -> ", ",").split(",").map { elem -> elem.toInt() }
        if (coords.maxOrNull()!! > gridSize) {
            gridSize = coords.maxOrNull()!!
        }
    }
    return MutableList(gridSize + 1) { MutableList(gridSize + 1) { 0 } }
}

fun part2(input: List<String>): Int {
    val grid = getGrid(input)
    input.forEach {
        val coords = it.replace(" -> ", ",").split(",").map { elem -> elem.toInt() }
        if (coords[0] == coords[2]) {
            firstThirdMatch(coords, grid)
        } else if (coords[1] == coords[3]) {
            secondFourthMatch(coords, grid)
        } else {
            otherCase(coords, grid)
        }
    }
    return getCount(grid)
}

private fun otherCase(
    coords: List<Int>,
    grid: MutableList<MutableList<Int>>,
) {
    if (coords[0] - coords[2] == coords[1] - coords[3]) {
        for (i in min(coords[0], coords[2])..<max(coords[0], coords[2]) + 1) {
            grid[min(coords[1], coords[3]) + (i - min(coords[0], coords[2]))][i] += 1
        }
    } else {
        for (i in min(coords[0], coords[2])..<max(coords[0], coords[2]) + 1) {
            grid[max(coords[1], coords[3]) - (i - min(coords[0], coords[2]))][i] += 1
        }
    }
}

private fun secondFourthMatch(
    coords: List<Int>,
    grid: MutableList<MutableList<Int>>,
) {
    for (i in min(coords[0], coords[2])..<max(coords[0], coords[2]) + 1) {
        grid[coords[1]][i] += 1
    }
}

private fun firstThirdMatch(
    coords: List<Int>,
    grid: MutableList<MutableList<Int>>,
) {
    for (i in min(coords[1], coords[3])..<max(coords[1], coords[3]) + 1) {
        grid[i][coords[0]] += 1
    }
}

private fun getCount(grid: MutableList<MutableList<Int>>): Int {
    var count = 0
    grid.forEach { row -> row.forEach { if (it > 1) count++ } }
    return count
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2021/day05/Day05_test")
    check(part1(testInput) == 5)
    check(part2(testInput) == 12)

    val input = readInput("src/main/kotlin/year2021/day05/Day05")
    println(part1(input))
    println(part2(input))
}
