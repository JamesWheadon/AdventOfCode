package year2020.day20

import check
import readInput
import kotlin.math.sqrt

fun part1(input: List<String>): Long {
    val tiles = input.fold(listOf<Tile>()) { acc, s ->
        acc.lastOrNull()?.let { tile ->
            if (s.isEmpty()) {
                acc
            } else if (s.startsWith("Tile")) {
                acc + Tile(s.filter { it.isDigit() }.toLong())
            } else {
                tile.grid.add(s)
                acc
            }
        } ?: run { acc + Tile(s.filter { it.isDigit() }.toLong()) }
    }
    tiles.forEach { it.oppositeEdges() }
    val allEdges = tiles.flatMap { it.edges }
    return tiles.filter { tile ->
        tile.edges.count { edge ->
            val reversedEdge = edge.reversed()
            allEdges.count { it == edge || it == reversedEdge } == 1
        } == 2
    }.fold(1L) { a, b -> a * b.number }
}

fun part2(input: List<String>): Int {
    val tiles = input.fold(listOf<Tile>()) { acc, s ->
        acc.lastOrNull()?.let { tile ->
            if (s.isEmpty()) {
                acc
            } else if (s.startsWith("Tile")) {
                acc + Tile(s.filter { it.isDigit() }.toLong())
            } else {
                tile.grid.add(s)
                acc
            }
        } ?: run { acc + Tile(s.filter { it.isDigit() }.toLong()) }
    }.toMutableList()
    tiles.forEach { it.oppositeEdges() }
    val lines = mutableListOf<List<String>>()
    val lineLength = sqrt(tiles.size.toDouble()).toInt()
    while (tiles.isNotEmpty()) {
        val start = tiles.random()
        val verticalLine = mutableListOf(start)
        val rawLine = findLine(start.grid.rotateLeft(), tiles, verticalLine)
        if (verticalLine.size == lineLength) {
            lines.add(rawLine)
            tiles.removeAll(verticalLine)
        } else {
            val horizontalLine = mutableListOf(start)
            val horizontal = findLine(start.grid, tiles, horizontalLine)
            if (horizontalLine.size == lineLength) {
                lines.add(horizontal)
                tiles.removeAll(horizontalLine)
            }
        }
    }
    val sortedGrid = mutableListOf<String>()
    while (lines.isNotEmpty()) {
        if (sortedGrid.isEmpty()) {
            sortedGrid.addAll(lines.removeFirst())
        } else {
            lines.firstOrNull {
                it.first() == sortedGrid.last() ||
                        it.first().reversed() == sortedGrid.last() ||
                        it.last() == sortedGrid.last() ||
                        it.last().reversed() == sortedGrid.last()
            }?.let { line ->
                when {
                    line.first() == sortedGrid.last() -> sortedGrid.addAll(line)
                    line.first().reversed() == sortedGrid.last() -> sortedGrid.addAll(line.map { it.reversed() })
                    line.last() == sortedGrid.last() -> sortedGrid.addAll(line.reversed())
                    else -> sortedGrid.addAll(line.reversed().map { it.reversed() })
                }
                lines.remove(line)
            } ?: lines.first {
                it.first() == sortedGrid.first() ||
                        it.first().reversed() == sortedGrid.first() ||
                        it.last() == sortedGrid.first() ||
                        it.last().reversed() == sortedGrid.first()
            }.let { line ->
                when {
                    line.first() == sortedGrid.first() -> sortedGrid.addAll(0, line.reversed())
                    line.first().reversed() == sortedGrid.first() -> sortedGrid.addAll(
                        0,
                        line.reversed().map { it.reversed() })

                    line.last() == sortedGrid.first() -> sortedGrid.addAll(0, line)
                    else -> sortedGrid.addAll(0, line.map { it.reversed() })
                }
                lines.remove(line)
            }
        }
    }
    val seaMonsterSearch = sortedGrid.filterIndexed { index, _ ->
        index % 10 != 0 && index % 10 != 9
    }.map { it.filterIndexed { index, _ -> index % 10 != 0 && index % 10 != 9 } }
    val seaMonster = listOf(
        "..................#.",
        "#....##....##....###",
        ".#..#..#..#..#..#..."
    )
    val horizontalSearches = listOf(
        seaMonster,
        seaMonster.reversed(),
        seaMonster.map { it.reversed() },
        seaMonster.reversed().map { it.reversed() }
    )
    val verticalSearches = listOf(
        seaMonster.rotateLeft(),
        seaMonster.rotateLeft().reversed(),
        seaMonster.rotateLeft().map { it.reversed() },
        seaMonster.rotateLeft().reversed().map { it.reversed() }
    )
    val findings = (horizontalSearches + verticalSearches).associateWith { 0 }.toMutableMap()
    for (i in 0..seaMonsterSearch.size - seaMonster.size) {
        for (j in 0..seaMonsterSearch[0].length - seaMonster[0].length) {
            horizontalSearches.forEach arrangement@{ m ->
                for (y in m.indices) {
                    for (x in m[0].indices) {
                        if (m[y][x] == '#' && seaMonsterSearch[i + y][j + x] != '#') {
                            return@arrangement
                        }
                    }
                }
                findings[m] = findings[m]!! + 1
            }
        }
    }
    for (i in 0..seaMonsterSearch.size - seaMonster[0].length) {
        for (j in 0..seaMonsterSearch[0].length - seaMonster.size) {
            verticalSearches.forEach arrangement@{ m ->
                for (y in m.indices) {
                    for (x in m[0].indices) {
                        if (m[y][x] == '#' && seaMonsterSearch[i + y][j + x] != '#') {
                            return@arrangement
                        }
                    }
                }
                findings[m] = findings[m]!! + 1
            }
        }
    }
    return seaMonsterSearch.sumOf { it.count { it == '#' } } - findings.values.max() * seaMonster.sumOf { it.count { it == '#' } }
}

private fun findLine(
    rawLineStart: List<String>,
    tiles: MutableList<Tile>,
    tilesInLine: MutableList<Tile>
): List<String> {
    var rawLine = rawLineStart
    while (true) {
        val startEdge = rawLine.map { it.first() == '#' }
        val endEdge = rawLine.map { it.last() == '#' }
        val nextTiles = tiles.filter { tile -> !tilesInLine.contains(tile) }
            .filter { tile ->
                tile.edges.any { edge ->
                    (edge == startEdge || edge.reversed() == startEdge) || (edge == endEdge || edge.reversed() == endEdge)
                }
            }
        if (nextTiles.isEmpty()) {
            break
        }
        nextTiles.forEach { next ->
            next.edges.firstOrNull { it == startEdge || it.reversed() == startEdge }?.let {
                when (startEdge) {
                    next.right -> rawLine = rawLine.mapIndexed { index, s -> next.grid[index] + s }
                    next.right.reversed() -> rawLine =
                        rawLine.mapIndexed { index, s -> next.grid.reversed()[index] + s }

                    next.left -> rawLine = rawLine.mapIndexed { index, s -> next.grid[index].reversed() + s }
                    next.left.reversed() -> rawLine =
                        rawLine.mapIndexed { index, s -> next.grid.reversed()[index].reversed() + s }

                    next.down -> rawLine = rawLine.mapIndexed { index, s -> next.grid.rotateLeft()[index] + s }
                    next.down.reversed() -> rawLine =
                        rawLine.mapIndexed { index, s -> next.grid.map { it.reversed() }.rotateLeft()[index] + s }

                    next.up -> rawLine = rawLine.mapIndexed { index, s -> next.grid.rotateRight()[index] + s }
                    next.up.reversed() -> rawLine = rawLine.mapIndexed { index, s ->
                        next.grid.map { it.reversed() }.rotateRight()[index] + s
                    }
                }
                tilesInLine.add(0, next)
            }
            next.edges.firstOrNull { it == endEdge || it.reversed() == endEdge }?.let {
                when (endEdge) {
                    next.right -> rawLine = rawLine.mapIndexed { index, s -> s + next.grid[index].reversed() }
                    next.right.reversed() -> rawLine =
                        rawLine.mapIndexed { index, s -> s + next.grid.reversed()[index].reversed() }

                    next.left -> rawLine = rawLine.mapIndexed { index, s -> s + next.grid[index] }
                    next.left.reversed() -> rawLine = rawLine.mapIndexed { index, s -> s + next.grid.reversed()[index] }
                    next.down -> rawLine =
                        rawLine.mapIndexed { index, s -> s + next.grid.rotateRight()[index] }

                    next.down.reversed() -> rawLine = rawLine.mapIndexed { index, s ->
                        s + next.grid.map { it.reversed() }.rotateRight()[index]
                    }

                    next.up -> rawLine = rawLine.mapIndexed { index, s -> s + next.grid.rotateLeft()[index] }
                    next.up.reversed() -> rawLine =
                        rawLine.mapIndexed { index, s -> s + next.grid.map { it.reversed() }.rotateLeft()[index] }
                }
                tilesInLine.add(next)
            }
        }
    }
    return rawLine
}

private fun List<String>.rotateLeft(): List<String> {
    val cols = this[0].length
    val rows = size
    return List(cols) { j ->
        List(rows) { i ->
            this[i][j]
        }.joinToString("")
    }
}

private fun List<String>.rotateRight(): List<String> {
    val cols = this[0].length
    val rows = size
    return List(cols) { j ->
        List(rows) { i ->
            this[i][j]
        }.joinToString("").reversed()
    }
}

data class Tile(val number: Long, val grid: MutableList<String> = mutableListOf()) {
    lateinit var up: List<Boolean>
    lateinit var down: List<Boolean>
    lateinit var left: List<Boolean>
    lateinit var right: List<Boolean>
    lateinit var edges: List<List<Boolean>>

    fun oppositeEdges() {
        up = grid.first().map { it == '#' }
        left = grid.map { it.first() == '#' }
        down = grid.last().map { it == '#' }
        right = grid.map { it.last() == '#' }
        edges = listOf(up, left, down, right)
    }
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2020/day20/Day20_test")
    check(part1(testInput), 20899048083289)
    check(part2(testInput), 273)

    val input = readInput("src/main/kotlin/year2020/day20/Day20")
    println(part1(input))
    println(part2(input))
}
