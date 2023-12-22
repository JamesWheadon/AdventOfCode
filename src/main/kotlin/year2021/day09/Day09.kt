package year2021.day09

import readInput

fun findBasin(grid: MutableList<MutableList<Int>>, row: Int, column: Int): Int {
    var newBasinSize = 1
    grid[row][column] = 9
    for (x in -1..1 step 2) {
        if (grid[row + x][column] != 9) {
            newBasinSize += findBasin(grid, row + x, column)
        }
    }
    for (y in -1..1 step 2) {
        if (grid[row][column + y] != 9) {
            newBasinSize += findBasin(grid, row, column + y)
        }
    }
    return newBasinSize
}

fun makeGridAndPad(input: List<String>): MutableList<MutableList<Int>> {
    val grid = input.map { row -> row.map { it.digitToInt() }.toMutableList() }.toMutableList()
    grid.forEach { it.add(0, 9); it.add(9) }
    grid.add(0, MutableList(grid[0].size) { 9 })
    grid.add(MutableList(grid[1].size) { 9 })
    return grid
}

fun part1(input: List<String>): Int {
    var total = 0
    val grid = makeGridAndPad(input)
    grid.forEachIndexed { row, ints ->
        ints.forEachIndexed { column, i ->
            if (row > 0 && row < grid.size - 1 && column > 0 && column < grid[0].size - 1 && isDeepPoint(
                    i,
                    grid,
                    row,
                    column,
                )
            ) {
                total += (1 + i)
            }
        }
    }
    return total
}

private fun isDeepPoint(
    i: Int,
    grid: MutableList<MutableList<Int>>,
    row: Int,
    column: Int,
): Boolean {
    var deepPoint = true
    for (x in -1..1 step 2) {
        if (i >= grid[row + x][column]) {
            deepPoint = false
        }
    }
    for (y in -1..1 step 2) {
        if (i >= grid[row][column + y]) {
            deepPoint = false
        }
    }
    return deepPoint
}

fun part2(input: List<String>): Int {
    val basinSizes = mutableListOf<Int>()
    val grid = makeGridAndPad(input)
    grid.forEachIndexed { row, ints ->
        ints.forEachIndexed { column, i ->
            if (row > 0 && row < grid.size - 1 && column > 0 && column < grid[0].size - 1 && i != 9) {
                basinSizes.add(findBasin(grid, row, column))
            }
        }
    }
    val highest = basinSizes.maxOrNull()
    basinSizes.remove(highest)
    val highest2 = basinSizes.maxOrNull()
    basinSizes.remove(highest2)
    val highest3 = basinSizes.maxOrNull()
    if (highest2 != null && highest3 != null && highest != null) {
        return highest * highest2.toInt() * highest3.toInt()
    }
    return 0
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2021/day09/Day09_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 1134)

    val input = readInput("src/main/kotlin/year2021/day09/Day09")
    println(part1(input))
    println(part2(input))
}
