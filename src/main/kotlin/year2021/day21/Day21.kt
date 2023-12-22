package year2021.day21

import readInput

data class UniversePermutation(val space: Int, val score: Int, var numTimes: Long, val turn: Int)

class Game(private val players: List<Player>) {

    fun findUniverses() {
        while (players[0].universeSpaces.isNotEmpty() || players[1].universeSpaces.isNotEmpty()) {
            players.forEach { player ->
                player.universeTurn()
                val winningTurns = player.universeSpaces.filter { it.score >= 21 }
                player.universes += winningTurns.sumOf { it.numTimes } * players.first { it != player }.universeSpaces.sumOf { it.numTimes }
                player.universeSpaces.removeAll(winningTurns)
            }
        }
    }
}

class Player(initialSpace: Int) {

    var universes = 0L
    private var space = initialSpace
    var score = 0
    var universeSpaces = mutableListOf(UniversePermutation(initialSpace, score, 1, 0))
    private val permutations =
        listOf(Pair(3, 1), Pair(4, 3), Pair(5, 6), Pair(6, 7), Pair(7, 6), Pair(8, 3), Pair(9, 1))

    fun move(subList: List<Int>) {
        subList.forEach {
            space += it
            while (space > 10) {
                space -= 10
            }
        }
        score += space
    }

    fun universeTurn() {
        val newUniverseSpaces = universeSpaces.filter { it.score >= 21 }.toMutableList()
        permutations.forEach { perm ->
            universeSpaces.filter { it.score < 21 }.forEach {
                val newSpace = newSpace(it.space, perm.first)
                val newScore = it.score + newSpace
                val newTurn = it.turn + 1
                val sameSpaceAndScore =
                    newUniverseSpaces.filter { universe -> universe.space == newSpace && universe.score == newScore && universe.turn == newTurn }
                if (sameSpaceAndScore.isNotEmpty()) {
                    sameSpaceAndScore.first().numTimes += (it.numTimes * perm.second)
                } else {
                    val newUniverse = UniversePermutation(newSpace, newScore, it.numTimes * perm.second, newTurn)
                    newUniverseSpaces.add(newUniverse)
                }
            }
        }
        universeSpaces = newUniverseSpaces
    }

    private fun newSpace(space: Int, move: Int): Int {
        var newSpace = space + move
        while (newSpace > 10) {
            newSpace -= 10
        }
        return newSpace
    }
}
fun part1(input: List<String>): Int {
    val rolls = (1..100).toList()
    var turn = 0
    var rollIndex = 0
    val playerOne = Player(input[0].split(" ").last().toInt())
    val playerTwo = Player(input[1].split(" ").last().toInt())
    while (playerOne.score < 1000 && playerTwo.score < 1000) {
        val turnRolls = mutableListOf<Int>()
        for (i in 0..<3) {
            turnRolls.add(rolls[rollIndex])
            rollIndex += 1
            if (rollIndex == rolls.size) {
                rollIndex = 0
            }
        }
        if (turn % 2 == 0) {
            playerOne.move(turnRolls)
        } else {
            playerTwo.move(turnRolls)
        }
        turn += 1
    }
    return turn * 3 * minOf(playerOne.score, playerTwo.score)
}

fun part2(input: List<String>): Long {
    val playerOne = Player(input[0].split(" ").last().toInt())
    val playerTwo = Player(input[1].split(" ").last().toInt())
    val game = Game(listOf(playerOne, playerTwo))
    game.findUniverses()
    return maxOf(playerOne.universes, playerTwo.universes)
}

fun main() {
    val testInput1 = readInput("src/main/kotlin/year2021/day21/Day21_test")
    check(part1(testInput1) == 739785)
    check(part2(testInput1) == 444356092776315L)

    val input = readInput("src/main/kotlin/year2021/day21/Day21")
    println(part1(input))
    println(part2(input))
}
