package year2021.day13

import readInput

fun part1(input: List<String>): Int {
    val triple = setUp(input)
    val dots = triple.first
    val folds = triple.second
    var grid = triple.third
    dots.forEach { grid[it.second][it.first] = 1 }
    val fold = folds[0]
    grid = changeGrid(fold, grid)
    var total = 0
    grid.forEach { row -> row.forEach { total += it } }
    return total
}

private fun setUp(input: List<String>): Triple<MutableList<Pair<Int, Int>>, MutableList<Pair<Char, Int>>, MutableList<MutableList<Int>>> {
    val dots = mutableListOf<Pair<Int, Int>>()
    val folds = mutableListOf<Pair<Char, Int>>()
    var gridRows = 0
    var gridColumns = 0
    input.forEach {
        if (it.contains("fold")) {
            folds.add(Pair(it.split("=")[0].last(), it.split("=")[1].toInt()))
        } else if (it != "") {
            val x = it.split(",")[0].toInt()
            val y = it.split(",")[1].toInt()
            dots.add(Pair(x, y))
            gridRows = maxOf(gridRows, y + 1)
            gridColumns = maxOf(gridColumns, x + 1)
        }
    }
    val grid = MutableList(gridRows) { MutableList(gridColumns) { 0 } }
    return Triple(dots, folds, grid)
}

private fun changeGrid(
    fold: Pair<Char, Int>,
    grid: MutableList<MutableList<Int>>,
): MutableList<MutableList<Int>> {
    var grid1 = grid
    if (fold.first == 'y') {
        grid1.forEachIndexed { row, ints ->
            ints.forEachIndexed { column, i ->
                if (row > fold.second && i == 1) {
                    grid1[2 * fold.second - row][column] = 1
                }
            }
        }
        grid1 = grid1.subList(0, fold.second)
    } else {
        grid1.forEachIndexed { row, ints ->
            ints.forEachIndexed { column, i ->
                if (column > fold.second && i == 1) {
                    grid1[row][2 * fold.second - column] = 1
                }
            }
        }
        grid1.forEachIndexed { row, ints ->
            grid1[row] = ints.subList(0, fold.second)
        }
    }
    return grid1
}

fun part2(input: List<String>): Int {
    val triple = setUp(input)
    val dots = triple.first
    val folds = triple.second
    var grid = triple.third
    dots.forEach { grid[it.second][it.first] = 1 }
    folds.forEach { fold ->
        grid = changeGrid(fold, grid)
    }
    var total = 0
    grid.forEach { row -> row.forEach { total += it } }
    println("")
    grid.forEach { println(it) }
    return 0
}

fun main() {
    val testInput1 = readInput("src/main/kotlin/year2021/day13/Day13_test")
    check(part1(testInput1) == 17)
    check(part2(testInput1) == 0)

    val input = readInput("src/main/kotlin/year2021/day13/Day13")
    println(part1(input))
    println(part2(input))
}
