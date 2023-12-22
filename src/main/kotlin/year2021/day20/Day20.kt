package year2021.day20

import readInput

fun part1(input: List<String>): Int {
    return lightUpGrid(input, 2)
}

fun part2(input: List<String>): Int {
    return lightUpGrid(input, 50)
}

private fun lightUpGrid(input: List<String>, iterations: Int): Int {
    val pixelValues = input[0].replace('.', '0').replace('#', '1').toMutableList()
    var grid = getAndExpandGrid(input, iterations)
    for (i in 0..<iterations) {
        val edgeValue = getEdgeValue(i, pixelValues)
        val nextGrid = MutableList(grid.size) { MutableList(grid[0].size) { '0' } }
        grid.forEachIndexed { row, chars ->
            chars.forEachIndexed { column, _ ->
                nextGrid[row][column] = pixelValues[getBinaryRepresentation(row, column, grid, edgeValue).toInt(2)]
            }
        }
        grid = nextGrid
    }
    return grid.flatten().count { it == '1' }
}

private fun getBinaryRepresentation(
    row: Int,
    column: Int,
    grid: MutableList<MutableList<Char>>,
    edgeValue: Char,
): String {
    var output = ""
    for (x in row - 1..row + 1) {
        for (y in column - 1..column + 1) {
            output += if (x in 0..<grid.size && y in 0..<grid[0].size) {
                grid[x][y]
            } else {
                edgeValue
            }
        }
    }
    return output
}

private fun getAndExpandGrid(
    input: List<String>,
    iterations: Int,
): MutableList<MutableList<Char>> {
    val grid = mutableListOf<MutableList<Char>>()
    input.subList(2, input.size).forEach {
        val row = it.replace('.', '0').replace('#', '1').toMutableList()
        grid.add(row)
    }
    val startSize = grid.size
    while (grid.size <= startSize + 2 * (iterations + 1)) {
        grid.forEach { row ->
            row.add(0, '0')
            row.add('0')
        }
        grid.add(MutableList(grid[0].size) { '0' })
        grid.add(0, MutableList(grid[0].size) { '0' })
    }
    return grid
}

fun getEdgeValue(iterations: Int, pixelValues: MutableList<Char>): Char {
    var empty = List(3) { List(3) { '0' } }
    var output = '0'
    for (i in 0..<iterations) {
        output = pixelValues[empty.flatten().joinToString("").toInt(2)]
        empty = List(3) { List(3) { output } }
    }
    return output
}

fun main() {
    val testInput1 = readInput("src/main/kotlin/year2021/day20/Day20_test")
    check(part1(testInput1) == 35)
    check(part2(testInput1) == 3351)

    val input = readInput("src/main/kotlin/year2021/day20/Day20")
    println(part1(input))
    println(part2(input))
}
