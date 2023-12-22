package year2022.day23

import readInput
import kotlin.math.abs

fun part1(input: List<String>): Int {
    val (directionOrder, elves) = getElves(input)
    for (i in 1..10) {
        moveElves(elves, directionOrder)
        elves.forEach { it.proposedMove = Pair(null, null) }
        directionOrder.add(directionOrder.removeFirst())
    }
    return (elves.maxOf { it.x } - elves.minOf { it.x } + 1) * (elves.maxOf { it.y } - elves.minOf { it.y } + 1) - elves.size
}

fun part2(input: List<String>): Int {
    val (directionOrder, elves) = getElves(input)
    var count = 0
    while (true) {
        moveElves(elves, directionOrder)
        count++
        if (elves.all { it.proposedMove.first == null }) {
            return count
        }
        elves.forEach { it.proposedMove = Pair(null, null) }
        directionOrder.add(directionOrder.removeFirst())
    }
}

private fun moveElves(
    elves: List<Elf>,
    directionOrder: MutableList<String>,
) {
    elves.filter { it.nearOtherElves(elves) }.forEach { elf ->
        elf.proposeMove(directionOrder, elves)
    }
    elves.filter { it.proposedMove.first != null }.forEach { elf ->
        if (elves.none { it != elf && it.proposedMove.first == elf.proposedMove.first && it.proposedMove.second == elf.proposedMove.second }) {
            elf.move()
        }
    }
}

private fun getElves(input: List<String>): Pair<MutableList<String>, List<Elf>> {
    val directionOrder = mutableListOf("N", "S", "W", "E")
    val elves = input.flatMapIndexed { column, s ->
        s.mapIndexed { row, c ->
            if (c == '#') {
                Elf(row, column)
            } else {
                null
            }
        }
    }.filterNotNull()
    return Pair(directionOrder, elves)
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("src/main/kotlin/year2022/day23/Day23_test")
    check(part1(testInput) == 110)
    check(part2(testInput) == 20)

    val input = readInput("src/main/kotlin/year2022/day23/Day23")
    println(part1(input))
    println(part2(input))
}

data class Elf(var x: Int, var y: Int) {
    var proposedMove: Pair<Int?, Int?> = Pair(null, null)

    fun proposeMove(directionOrder: MutableList<String>, elves: List<Elf>) {
        directionOrder.forEach { direction ->
            if (proposedMove.first == null) {
                getProposedMove(direction, elves)
            }
        }
    }

    private fun getProposedMove(direction: String, elves: List<Elf>) {
        when (direction) {
            "N" -> {
                if (elves.none { it.y - y == -1 && abs(it.x - x) <= 1 }) {
                    proposedMove = Pair(x, y - 1)
                }
            }

            "S" -> {
                if (elves.none { it.y - y == 1 && abs(it.x - x) <= 1 }) {
                    proposedMove = Pair(x, y + 1)
                }
            }

            "W" -> {
                if (elves.none { it.x - x == -1 && abs(it.y - y) <= 1 }) {
                    proposedMove = Pair(x - 1, y)
                }
            }

            else -> {
                if (elves.none { it.x - x == 1 && abs(it.y - y) <= 1 }) {
                    proposedMove = Pair(x + 1, y)
                }
            }
        }
    }

    fun move() {
        x = proposedMove.first!!
        y = proposedMove.second!!
    }

    fun nearOtherElves(elves: List<Elf>): Boolean {
        return elves.filter { it != this }.any { abs(it.y - y) <= 1 && abs(it.x - x) <= 1 }
    }
}
