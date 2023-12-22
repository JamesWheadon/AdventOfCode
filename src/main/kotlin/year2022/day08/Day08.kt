package year2022.day08

import readInput

fun traverseGrid(grid: List<List<Int>>, y: Int, x: Int): MutableList<Int> {
    val row = grid[y]
    val column = grid.map { it[x] }
    val values = MutableList(4) { 0 }
    for (i in 0..<row.subList(0, x).size) {
        values[0]++
        if (row.subList(0, x).reversed()[i] >= row[x]) {
            break
        }
    }
    for (i in 0..<row.subList(x + 1, row.size).size) {
        values[1]++
        if (row.subList(x + 1, row.size)[i] >= row[x]) {
            break
        }
    }
    for (i in 0..<column.subList(0, y).size) {
        values[2]++
        if (column.subList(0, y).reversed()[i] >= column[y]) {
            break
        }
    }
    for (i in 0..<column.subList(y + 1, column.size).size) {
        values[3]++
        if (column.subList(y + 1, column.size)[i] >= column[y]) {
            break
        }
    }
    return values
}

fun part1(input: List<String>): Int {
    val grid = input.map { line -> line.map { it.digitToInt() } }.map { it.toList() }
    val visible = MutableList(grid.size) { MutableList(grid[0].size) { 0 } }
    for (y in grid.indices) {
        for (x in grid[0].indices) {
            val row = grid[y]
            val column = grid.map { it[x] }
            if (row.subList(0, x).isEmpty() || row.subList(0, x).maxOrNull()!! < row[x] || row.subList(x + 1, grid.size)
                    .isEmpty() || row.subList(x + 1, grid.size).maxOrNull()!! < row[x] ||
                column.subList(0, y).isEmpty() || column.subList(0, y)
                    .maxOrNull()!! < column[y] || column.subList(y + 1, grid.size).isEmpty() || column.subList(
                    y + 1,
                    grid.size,
                ).maxOrNull()!! < column[y]
            ) {
                visible[y][x] = 1
            }
        }
    }
    return visible.sumOf { it.reduce { a, b -> a + b } }
}

fun part2(input: List<String>): Int {
    val grid = input.map { line -> line.map { it.digitToInt() } }.map { it.toList() }
    val visible = MutableList(grid.size) { MutableList(grid[0].size) { 0 } }
    for (y in grid.indices) {
        for (x in grid[0].indices) {
            val values = traverseGrid(grid, y, x)
            visible[y][x] = values.reduce { a, b -> a * b }
        }
    }
    println(visible.maxOf { it.maxOrNull()!! })
    return visible.maxOf { it.maxOrNull()!! }
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("src/main/kotlin/year2022/day08/Day08_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 8)

    val input = readInput("src/main/kotlin/year2022/day08/Day08")
    println(part1(input))
    println(part2(input))
}
