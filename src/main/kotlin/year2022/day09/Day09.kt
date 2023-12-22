package year2022.day09

import readInput
import kotlin.math.abs

fun part1(input: List<String>): Int {
    val visited = MutableList(1) { Pair(0, 0) }
    var headX = 0
    var headY = 0
    var tailX = 0
    var tailY = 0
    input.forEach { move ->
        val direction = move.split(" ")[0]
        val amount = move.split(" ")[1].toInt()
        for (i in 0..<amount) {
            val pair = moveHead(direction, headX, headY)
            headX = pair.first
            headY = pair.second
            if (abs(headY - tailY) > 1 || abs(headX - tailX) > 1) {
                val pair1 = pair(headY, tailY, headX, tailX)
                tailX = pair1.first
                tailY = pair1.second
                if (visited.none { it.first == tailX && it.second == tailY }) {
                    visited.add(Pair(tailX, tailY))
                }
            }
        }
    }
    return visited.size
}

fun part2(input: List<String>): Int {
    val visited = MutableList(1) { Pair(0, 0) }
    val knots = MutableList(10) { MutableList(2) { 0 } }
    input.forEach { move ->
        val direction = move.split(" ")[0]
        val amount = move.split(" ")[1].toInt()
        for (i in 0..<amount) {
            moveHead(direction, knots[0][0], knots[0][1])
            for (j in 0..<9) {
                if (abs(knots[j][1] - knots[j + 1][1]) > 1 || abs(knots[j][0] - knots[j + 1][0]) > 1) {
                    pair(knots[j][1], knots[j + 1][1], knots[j][0], knots[j + 1][0])
                }
            }
            if (visited.none { it.first == knots[9][0] && it.second == knots[9][1] }) {
                visited.add(Pair(knots[9][0], knots[9][1]))
            }
        }
    }
    return visited.size
}

private fun pair(headY: Int, tailY: Int, headX: Int, tailX: Int): Pair<Int, Int> {
    var tailY1 = tailY
    var tailX1 = tailX
    when {
        abs(headY - tailY1) >= 1 && abs(headX - tailX1) >= 1 -> {
            tailX1 += (headX - tailX1) / abs(headX - tailX1)
            tailY1 += (headY - tailY1) / abs(headY - tailY1)
        }

        abs(headY - tailY1) > 1 -> {
            tailY1 += (headY - tailY1) / abs(headY - tailY1)
        }

        abs(headX - tailX1) > 1 -> {
            tailX1 += (headX - tailX1) / abs(headX - tailX1)
        }
    }
    return Pair(tailX1, tailY1)
}

private fun moveHead(direction: String, headX: Int, headY: Int): Pair<Int, Int> {
    var headX1 = headX
    var headY1 = headY
    when (direction) {
        "R" -> {
            headX1++
        }

        "L" -> {
            headX1--
        }

        "U" -> {
            headY1++
        }

        else -> {
            headY1--
        }
    }
    return Pair(headX1, headY1)
}

fun main() {
// test if implementation meets criteria from the description, like:
    val testInput = readInput("src/main/kotlin/year2022/day09/Day09_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 1)

    val input = readInput("src/main/kotlin/year2022/day09/Day09")
    println(part1(input))
    println(part2(input))
}
