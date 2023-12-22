package year2023.day13

import readInput

fun part1(input: List<String>): Int {
    val grids = getGrids(input)
    return grids.sumOf { grid ->
        val rowMirror = rowMirror(grid)
        val score = if (rowMirror == -1) {
            columnMirror(grid)
        } else {
            rowMirror * 100
        }
        score
    }
}

fun part2(input: List<String>): Int {
    val grids = getGrids(input)
    return grids.sumOf { grid ->
        val oldRowMirror = rowMirror(grid)
        val oldColumnMirror = columnMirror(grid)
        var score = 0
        for (smudge in 0..<grid[0].length * grid.size) {
            val smudgeGrid = getSmudgeGrid(grid, smudge)
            val rowMirror = rowMirror(smudgeGrid, oldRowMirror)
            if (rowMirror == -1) {
                val columnMirror = columnMirror(smudgeGrid, oldColumnMirror)
                if (columnMirror != -1) {
                    score = columnMirror
                    break
                }
            } else {
                score = rowMirror * 100
                break
            }
        }
        score
    }
}

private fun getSmudgeGrid(
    grid: List<String>,
    smudge: Int,
): MutableList<String> {
    val smudgeGrid = grid.toMutableList()
    val row = smudge / grid[0].length
    val column = smudge % grid[0].length
    var smudgeRow = smudgeGrid[row]
    smudgeRow = smudgeRow.toList().mapIndexed { index, c ->
        if (index == column) {
            if (c == '.') {
                '#'
            } else {
                '.'
            }
        } else {
            c
        }
    }.joinToString("")
    smudgeGrid[row] = smudgeRow
    return smudgeGrid
}

private fun getGrids(input: List<String>): MutableList<List<String>> {
    val grids = mutableListOf<List<String>>()
    var remaining = input
    while (remaining.contains("")) {
        val i = remaining.indexOf("")
        grids.add(remaining.subList(0, i))
        remaining = remaining.subList(i + 1, remaining.size)
    }
    grids.add(remaining)
    return grids
}

fun rowMirror(grid: List<String>, skip: Int = -1): Int {
    for (i in 1..<grid.size) {
        if (i == skip) {
            continue
        }
        val before = grid.subList(0, i)
        val after = grid.subList(i, grid.size)
        if (before.reversed().zip(after).all { it.first == it.second }) return i
    }
    return -1
}

fun columnMirror(grid: List<String>, skip: Int = -1): Int {
    return rowMirror((0..<grid[0].length).map { columnNum -> grid.map { it[columnNum] }.joinToString("") }, skip)
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2023/day13/Day13_test")
    check(part1(testInput) == 405)
    check(part2(testInput) == 400)

    val input = readInput("src/main/kotlin/year2023/day13/Day13")
    println(part1(input))
    println(part2(input))
}
