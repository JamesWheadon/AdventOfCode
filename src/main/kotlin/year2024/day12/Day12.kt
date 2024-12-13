package year2024.day12

import readInput
import java.util.*
import kotlin.math.abs

fun part1(input: List<String>): Int {
    val grid = createAndConnectPlots(input)
    var t = 0
    val visited = mutableSetOf<Plot>()
    for (i in grid.indices) {
        for (j in grid[0].indices) {
            if (!visited.contains(grid[i][j])) {
                val plots = mutableListOf<Plot>()
                val queue: Queue<Plot> = LinkedList()
                queue.add(grid[i][j])
                while (queue.isNotEmpty()) {
                    val plot = queue.poll()
                    plot.connectedPlots().forEach { nextPlot ->
                        if (!queue.contains(nextPlot) && !plots.contains(nextPlot)) {
                            queue.add(nextPlot)
                        }
                    }
                    plots.add(plot)
                }
                visited.addAll(plots)
                t += (plots.size * plots.sumOf { it.perimeter() })
            }
        }
    }
    return t
}

fun part2(input: List<String>): Int {
    val grid = createAndConnectPlots(input)
    var t = 0
    val visited = mutableSetOf<Plot>()
    for (i in grid.indices) {
        for (j in grid[0].indices) {
            if (!visited.contains(grid[i][j])) {
                val plots = mutableListOf<Plot>()
                val queue: Queue<Plot> = LinkedList()
                queue.add(grid[i][j])
                while (queue.isNotEmpty()) {
                    val plot = queue.poll()
                    plot.connectedPlots().forEach { nextPlot ->
                        if (!queue.contains(nextPlot) && !plots.contains(nextPlot)) {
                            queue.add(nextPlot)
                        }
                    }
                    plots.add(plot)
                }
                visited.addAll(plots)
                val up = plots.filter { plot -> plot.up == null }.toMutableList()
                val down = plots.filter { plot -> plot.down == null }.toMutableList()
                val left = plots.filter { plot -> plot.left == null }.toMutableList()
                val right = plots.filter { plot -> plot.right == null }.toMutableList()
                val sides = listOf(up, down, left, right).sumOf { dir ->
                    var dirSides = 0
                    while (dir.size > 0) {
                        val currentSide = mutableListOf(dir.removeFirst())
                        while (true) {
                            val adjacent =
                                dir.filter { plot -> currentSide.any { abs(plot.row - it.row) + abs(plot.column - it.column) == 1 } }
                            if (adjacent.isEmpty()) {
                                break
                            }
                            dir.removeAll(adjacent)
                            currentSide.addAll(adjacent)
                        }
                        dirSides += 1
                    }
                    dirSides
                }
                t += (plots.size * sides)
            }
        }
    }
    return t
}

private fun createAndConnectPlots(input: List<String>): List<List<Plot>> {
    val grid = input.mapIndexed { row, plots -> plots.mapIndexed { column, plot -> Plot(plot, row, column) } }
    grid.forEachIndexed { row, plots ->
        plots.forEachIndexed { column, plot ->
            if (row > 0 && grid[row - 1][column].type == plot.type) {
                plot.up = grid[row - 1][column]
            }
            if (row < grid.size - 1 && grid[row + 1][column].type == plot.type) {
                plot.down = grid[row + 1][column]
            }
            if (column > 0 && grid[row][column - 1].type == plot.type) {
                plot.left = grid[row][column - 1]
            }
            if (column < grid[0].size - 1 && grid[row][column + 1].type == plot.type) {
                plot.right = grid[row][column + 1]
            }
        }
    }
    return grid
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2024/day12/Day12_test")
    check(part1(testInput) == 1930)
    check(part2(testInput) == 1206)

    val input = readInput("src/main/kotlin/year2024/day12/Day12")
    println(part1(input))
    println(part2(input))
}

class Plot(val type: Char, val row: Int, val column: Int) {
    var up: Plot? = null
    var down: Plot? = null
    var left: Plot? = null
    var right: Plot? = null

    fun connectedPlots() = listOfNotNull(up, down, left, right)
    fun perimeter() = 4 - connectedPlots().count()
    override fun toString(): String {
        return "Plot(type=$type, row=$row, column=$column)"
    }
}
