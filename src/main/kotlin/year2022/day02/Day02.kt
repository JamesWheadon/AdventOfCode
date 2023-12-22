package year2022.day02

import readInput

fun part1(input: List<String>): Int {
    var score = 0
    val wins = listOf("A Y", "B Z", "C X")
    val draws = listOf("A X", "B Y", "C Z")
    val points = mapOf("X" to 1, "Y" to 2, "Z" to 3)
    input.map { round ->
        if (wins.contains(round)) {
            score += 6
        } else if (draws.contains(round)) {
            score += 3
        }
        score += points[round.split(" ")[1]]!!
    }
    return score
}

fun part2(input: List<String>): Int {
    var score = 0
    val wins = mapOf("A" to 2, "B" to 3, "C" to 1)
    val draws = mapOf("A" to 1, "B" to 2, "C" to 3)
    val loses = mapOf("A" to 3, "B" to 1, "C" to 2)
    val movePoints = mapOf("X" to loses, "Y" to draws, "Z" to wins)
    val points = mapOf("X" to 0, "Y" to 3, "Z" to 6)
    input.map { round ->
        val result = round.split(" ")[1]
        score += points[result]!!
        score += movePoints[result]?.get(round.split(" ")[0])!!
    }
    return score
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("src/main/kotlin/year2022/day02/Day02_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readInput("src/main/kotlin/year2022/day02/Day02")
    println(part1(input))
    println(part2(input))
}
