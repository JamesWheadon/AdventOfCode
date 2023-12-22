package year2021.day15

import readInput

class Path(val cost: Int, val currentRow: Int, val currentColumn: Int)

fun addNewPath(paths: MutableList<Path>, newPath: Path) {
    val pathsToPosition =
        paths.filter { it.currentRow == newPath.currentRow && it.currentColumn == newPath.currentColumn }
    if (pathsToPosition.isEmpty()) {
        paths.add(newPath)
    } else {
        if (pathsToPosition.minByOrNull { it.cost }!!.cost > newPath.cost) {
            paths.removeAll(pathsToPosition)
            paths.add(newPath)
        }
    }
}

fun findPath(
    grid: MutableList<MutableList<Int>>,
    paths: MutableList<Path>,
    positions: MutableList<Pair<Int, Int>>,
) {
    val path = paths.minByOrNull { it.cost }!!
    positions.add(Pair(path.currentRow, path.currentColumn))
    if (path.currentRow != grid.lastIndex || path.currentColumn != grid[0].lastIndex) {
        paths.remove(path)
        if (path.currentRow > 0 && noPositions(positions, path.currentRow - 1, path.currentColumn)) {
            val newPath = Path(
                path.cost + grid[path.currentRow - 1][path.currentColumn],
                path.currentRow - 1,
                path.currentColumn,
            )
            addNewPath(paths, newPath)
        }
        if (path.currentRow < grid.lastIndex && noPositions(positions, path.currentRow + 1, path.currentColumn)) {
            val newPath = Path(
                path.cost + grid[path.currentRow + 1][path.currentColumn],
                path.currentRow + 1,
                path.currentColumn,
            )
            addNewPath(paths, newPath)
        }
        if (path.currentColumn > 0 && noPositions(positions, path.currentRow, path.currentColumn - 1)) {
            val newPath = Path(
                path.cost + grid[path.currentRow][path.currentColumn - 1],
                path.currentRow,
                path.currentColumn - 1,
            )
            addNewPath(paths, newPath)
        }
        if (path.currentColumn < grid.lastIndex && noPositions(positions, path.currentRow, path.currentColumn + 1)) {
            val newPath = Path(
                path.cost + grid[path.currentRow][path.currentColumn + 1],
                path.currentRow,
                path.currentColumn + 1,
            )
            addNewPath(paths, newPath)
        }
    }
}

private fun noPositions(
    positions: MutableList<Pair<Int, Int>>,
    row: Int,
    column: Int,
) = positions.none { it.first == row && it.second == column }

fun part1(input: List<String>): Int {
    val grid = mutableListOf<MutableList<Int>>()
    input.forEach {
        val row = mutableListOf<Int>()
        it.forEach { cell -> row.add(cell.digitToInt()) }
        grid.add(row)
    }
    return getPathCost(grid)
}

fun part2(input: List<String>): Int {
    val grid = mutableListOf<MutableList<Int>>()
    for (i in 0..<5) {
        input.forEach { line ->
            val row = mutableListOf<Int>()
            for (j in 0..<5) {
                line.forEach {
                    var element = it.digitToInt() + i + j
                    if (element > 9) {
                        element -= 9
                    }
                    row.add(element)
                }
            }
            grid.add(row)
        }
    }
    return getPathCost(grid)
}

private fun getPathCost(grid: MutableList<MutableList<Int>>): Int {
    val paths = mutableListOf(Path(0, 0, 0))
    val positions = mutableListOf<Pair<Int, Int>>()
    while (!positions.contains(Pair(grid.lastIndex, grid[0].lastIndex))) {
        findPath(grid, paths, positions)
    }
    return paths.filter { it.currentRow == grid.lastIndex && it.currentColumn == grid[0].lastIndex }[0].cost
}

fun main() {
    val testInput1 = readInput("src/main/kotlin/year2021/day15/Day15_test")
    check(part1(testInput1) == 40)
    check(part2(testInput1) == 315)

    val input = readInput("src/main/kotlin/year2021/day15/Day15")
    println(part1(input))
    println(part2(input))
}
