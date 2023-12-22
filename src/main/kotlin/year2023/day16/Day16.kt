package year2023.day16

import readInput

fun part1(input: List<String>, start: Triple<Int, Int, Char>): Int {
    val grid = input.mapIndexed { row, s -> s.toList().mapIndexed { column, c -> Node(row, column, c) } }
    val gridSize = listOf(grid.indices, grid[0].indices)
    var beams = listOf(start)
    while (beams.isNotEmpty()) {
        beams = beams.map { beam -> grid[beam.first][beam.second].beamAtNode(beam.third) }.flatten().filter { it.first in gridSize[0] && it.second in gridSize[1] }
    }
    return grid.flatten().count { it.energized }
}

fun part2(input: List<String>): Int {
    val possibleStarts = listOf(input.indices.map { listOf(Triple(it, 0, 'd'), Triple(it, input[0].indices.last, 'a')) }.flatten(), input[0].indices.map { listOf(Triple(0, it, 's'), Triple(input.indices.last, it, 'w')) }.flatten()).flatten()
    return possibleStarts.maxOf { part1(input, it) }
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2023/day16/Day16_test")
    check(part1(testInput, Triple(0, 0, 'd')) == 46)
    check(part2(testInput) == 51)

    val input = readInput("src/main/kotlin/year2023/day16/Day16")
    println(part1(input, Triple(0, 0, 'd')))
    println(part2(input))
}

class Node(val row: Int, val column: Int, val char: Char) {
    var energized = false
    private val previous = mutableListOf<Char>()

    fun beamAtNode(beamDirection: Char): List<Triple<Int, Int, Char>> {
        if (previous.contains(beamDirection)) {
            return listOf()
        }
        previous.add(beamDirection)
        energized = true
        return when (char) {
            '.' -> {
                listOf(getBeamNextPosition(beamDirection))
            }
            '/' -> {
                when (beamDirection) {
                    'w' -> listOf(getBeamNextPosition('d'))
                    'd' -> listOf(getBeamNextPosition('w'))
                    's' -> listOf(getBeamNextPosition('a'))
                    else -> listOf(getBeamNextPosition('s'))
                }
            }
            '\\' -> {
                when (beamDirection) {
                    'w' -> listOf(getBeamNextPosition('a'))
                    'd' -> listOf(getBeamNextPosition('s'))
                    's' -> listOf(getBeamNextPosition('d'))
                    else -> listOf(getBeamNextPosition('w'))
                }
            }
            '|' -> {
                when (beamDirection) {
                    'a', 'd' -> listOf(getBeamNextPosition('w'), getBeamNextPosition('s'))
                    else -> listOf(getBeamNextPosition(beamDirection))
                }
            }
            '-' -> {
                when (beamDirection) {
                    'w', 's' -> listOf(getBeamNextPosition('a'), getBeamNextPosition('d'))
                    else -> listOf(getBeamNextPosition(beamDirection))
                }
            }
            else -> {
                listOf()
            }
        }
    }

    private fun getBeamNextPosition(beamDirection: Char): Triple<Int, Int, Char> {
        return when (beamDirection) {
            'w' -> {
                Triple(row - 1, column, beamDirection)
            }
            'd' -> {
                Triple(row, column + 1, beamDirection)
            }
            's' -> {
                Triple(row + 1, column, beamDirection)
            }
            else -> {
                Triple(row, column - 1, beamDirection)
            }
        }
    }

    override fun toString(): String {
        return "Node(row=$row, column=$column, char=$char, energized=$energized)"
    }
}
