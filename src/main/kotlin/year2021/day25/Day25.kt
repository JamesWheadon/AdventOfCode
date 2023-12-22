package year2021.day25

import readInput

fun part1(input: List<String>): Int {
    var grid = input.map { row ->
        row.replace('.', '0').replace('>', '1').replace('v', '2')
            .map { it.digitToInt() }
            .toMutableList()
    }.toMutableList()
    var newGrid = MutableList(grid.size) { MutableList(grid[0].size) { 0 } }
    var iteration = 0
    while (grid != newGrid) {
        if (iteration > 0) {
            grid = newGrid
        }
        newGrid = MutableList(grid.size) { MutableList(grid[0].size) { 0 } }
        grid.forEachIndexed { row, ints ->
            ints.forEachIndexed { column, i ->
                if (i == 1) {
                    handleOne(column, grid, ints, newGrid, row)
                } else if (i == 2) {
                    handleTwo(row, grid, column, newGrid)
                }
            }
        }
        iteration += 1
    }
    return iteration
}

private fun handleOne(
    column: Int,
    grid: MutableList<MutableList<Int>>,
    ints: MutableList<Int>,
    newGrid: MutableList<MutableList<Int>>,
    row: Int,
) {
    var newColumn = column + 1
    if (newColumn == grid[0].size) {
        newColumn = 0
    }
    if (ints[newColumn] == 0) {
        newGrid[row][newColumn] = 1
    } else {
        newGrid[row][column] = 1
    }
}

private fun handleTwo(
    row: Int,
    grid: MutableList<MutableList<Int>>,
    column: Int,
    newGrid: MutableList<MutableList<Int>>,
) {
    var newRow = row + 1
    if (newRow == grid.size) {
        newRow = 0
    }
    var previousColumn = column - 1
    if (previousColumn == -1) {
        previousColumn = grid[0].lastIndex
    }
    var nextColumn = column + 1
    if (nextColumn == grid[0].size) {
        nextColumn = 0
    }
    if ((grid[newRow][column] == 0 && grid[newRow][previousColumn] != 1) ||
        (grid[newRow][column] == 1 && grid[newRow][nextColumn] == 0)
    ) {
        newGrid[newRow][column] = 2
    } else {
        newGrid[row][column] = 2
    }
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput1 = readInput("src/main/kotlin/year2021/day25/Day25_test")
    check(part1(testInput1) == 58)

    val input = readInput("src/main/kotlin/year2021/day25/Day25")
    println(part1(input))
}
