package year2021.day11

import readInput

fun flash(grid: MutableList<MutableList<Int>>, row: Int, column: Int): Int {
    var flashes = 1
    for (x in row - 1..row + 1) {
        for (y in column - 1..column + 1) {
            if (!(x == row && y == column) && (x >= 0 && x < grid.size) && (y >= 0 && y < grid[0].size)) {
                grid[x][y] += 1
                if (grid[x][y] == 10) {
                    flashes += flash(grid, x, y)
                }
            }
        }
    }
    return flashes
}

fun part1(input: List<String>): Int {
    var total = 0
    val grid = input.map { row -> row.map { it.digitToInt() }.toMutableList() }.toMutableList()
    for (step in 0..<100) {
        grid.forEachIndexed { row, ints ->
            ints.forEachIndexed { column, i ->
                grid[row][column] = i + 1
                if (i == 9) {
                    total += flash(grid, row, column)
                }
            }
        }
        grid.forEachIndexed { row, ints ->
            ints.forEachIndexed { column, i ->
                if (i >= 10) {
                    grid[row][column] = 0
                }
            }
        }
    }
    return total
}

fun part2(input: List<String>): Int {
    var total = 0
    val grid = input.map { row -> row.map { it.digitToInt() }.toMutableList() }.toMutableList()
    for (step in 0..<10000) {
        grid.forEachIndexed { row, ints ->
            ints.forEachIndexed { column, i ->
                grid[row][column] = i + 1
                if (i == 9) {
                    total += flash(grid, row, column)
                }
            }
        }
        var count = 0
        grid.forEachIndexed { row, ints ->
            ints.forEachIndexed { column, i ->
                if (i >= 10) {
                    grid[row][column] = 0
                    count += 1
                }
            }
        }
        if (count == grid.size * grid[0].size) {
            return step + 1
        }
    }
    return total
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2021/day11/Day11_test")
    check(part1(testInput) == 1656)
    check(part2(testInput) == 195)

    val input = readInput("src/main/kotlin/year2021/day11/Day11")
    println(part1(input))
    println(part2(input))
}
