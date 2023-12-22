package year2023.day18

import readInput
import kotlin.math.abs

fun part1(input: List<String>): Int {
    var corners = mutableListOf(Pair(0, 0))
    input.forEach {
        when (it.split(" ")[0]) {
            "R" -> {
                corners.add(Pair(corners.last().first, corners.last().second + it.split(" ")[1].toInt()))
            }

            "L" -> {
                corners.add(Pair(corners.last().first, corners.last().second - it.split(" ")[1].toInt()))
            }

            "U" -> {
                corners.add(Pair(corners.last().first - it.split(" ")[1].toInt(), corners.last().second))
            }

            else -> {
                corners.add(Pair(corners.last().first + it.split(" ")[1].toInt(), corners.last().second))
            }
        }
    }
    val minRow = corners.minOf { it.first }
    val minColumn = corners.minOf { it.second }
    val rows = corners.maxOf { it.first } - minRow + 1
    val columns = corners.maxOf { it.second } - minColumn + 1
    val grid = List(rows) { List(columns) { Node(false) } }
    corners = corners.map { Pair(it.first - minRow, it.second - minColumn) }.toMutableList()
    var previous = corners.first()
    corners.forEach {
        for (i in minOf(previous.first, it.first)..maxOf(previous.first, it.first)) {
            for (j in minOf(previous.second, it.second)..maxOf(previous.second, it.second)) {
                grid[i][j].dug = true
            }
        }
        previous = it
    }
    grid.forEachIndexed { row, nodes ->
        nodes.forEachIndexed { column, node ->
            node.column = column
            node.row = row
        }
    }
    val nodes = grid.flatten()
    nodes.forEach { node ->
        node.neighbours = listOfNotNull(
            nodes.firstOrNull { it.row == node.row && it.column == node.column - 1 },
            nodes.firstOrNull { it.row == node.row && it.column == node.column + 1 },
            nodes.firstOrNull { it.row == node.row - 1 && it.column == node.column },
            nodes.firstOrNull { it.row == node.row + 1 && it.column == node.column },
        )
    }
    val groups = mutableListOf<MutableList<Node>>()
    val remaining = nodes.filter { !it.dug }.toMutableList()
    while (remaining.isNotEmpty()) {
        val newGroup = remaining.first().getNotDugGroup()
        remaining.removeAll(newGroup)
        groups.add(newGroup)
    }
    val innerGroups =
        groups.filter { group -> group.none { it.row == 0 || it.row == rows - 1 || it.column == 0 || it.column == columns - 1 } }
    return innerGroups.sumOf { it.size } + nodes.filter { it.dug }.size
}

fun part2(input: List<String>): Long {
    val corners = mutableListOf(Pair(0L, 0L))
    var edge = 0L
    input.forEach {
        val instruction = it.split(" ")[2].filter { c -> c.isLetterOrDigit() }
        val distance = instruction.substring(0, 5).toInt(16).toLong()
        edge += distance
        when (instruction.last().toString()) {
            "0" -> {
                corners.add(Pair(corners.last().first, corners.last().second + distance))
            }

            "2" -> {
                corners.add(Pair(corners.last().first, corners.last().second - distance))
            }

            "3" -> {
                corners.add(Pair(corners.last().first - distance, corners.last().second))
            }

            else -> {
                corners.add(Pair(corners.last().first + distance, corners.last().second))
            }
        }
    }
    val antiClockwise = input.first().last { it.isLetterOrDigit() } in listOf('1', '2')
    if (!antiClockwise) {
        corners.reverse()
    }
    val area = abs(
        corners.zipWithNext().sumOf {
            it.first.first * it.second.second - it.first.second * it.second.first
        },
    ) / 2
    return edge / 2 + area + 1
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2023/day18/Day18_test")
    check(part1(testInput) == 62)
    check(part2(testInput) == 952408144115L)

    val input = readInput("src/main/kotlin/year2023/day18/Day18")
    println(part1(input))
    println(part2(input))
}

class Node(var dug: Boolean) {
    var row: Int = 0
    var column: Int = 0
    var neighbours: List<Node> = listOf()

    fun getNotDugGroup(): MutableList<Node> {
        val group = mutableListOf(this)
        var new = listOf(this)
        while (new.isNotEmpty()) {
            new = new.map { node -> node.neighbours.filter { !it.dug && !group.contains(it) && !new.contains(it) } }
                .flatten().distinct()
            group.addAll(new)
        }
        return group.toMutableList()
    }
}
