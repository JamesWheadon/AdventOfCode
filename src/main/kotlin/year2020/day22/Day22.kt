package year2020.day22

import check
import readInput

fun part1(input: List<String>): Int {
    val player1 = input.takeWhile { it != "" }.drop(1).map { it.toInt() }.toMutableList()
    val player2 = input.asSequence().dropWhile { it != "" }.drop(1).takeWhile { it != "" }.drop(1).map { it.toInt() }
        .toMutableList()
    while (player1.isNotEmpty() && player2.isNotEmpty()) {
        val player1Card = player1.removeFirst()
        val player2Card = player2.removeFirst()
        if (player1Card > player2Card) {
            player1.addAll(listOf(player1Card, player2Card))
        } else {
            player2.addAll(listOf(player2Card, player1Card))
        }
    }
    return if (player1.isEmpty()) {
        player2.reversed().mapIndexed { index, i -> i * (index + 1) }.sum()
    } else {
        player1.reversed().mapIndexed { index, i -> i * (index + 1) }.sum()
    }
}

fun part2(input: List<String>): Int {
    val player1 = input.takeWhile { it != "" }.drop(1).map { it.toInt() }.toMutableList()
    val player2 = input.asSequence().dropWhile { it != "" }.drop(1).takeWhile { it != "" }.drop(1).map { it.toInt() }
        .toMutableList()
    val winner = try {
        playGame(player1, player2)
    } catch (e: Exception) {
        println(e)
        true
    }
    return if (winner) {
        player1.reversed().mapIndexed { index, i -> i * (index + 1) }.sum()
    } else {
        player2.reversed().mapIndexed { index, i -> i * (index + 1) }.sum()
    }
}

fun playGame(player1: MutableList<Int>, player2: MutableList<Int>): Boolean {
    val previous = mutableSetOf<Pair<List<Int>, List<Int>>>()
    while (player1.isNotEmpty() && player2.isNotEmpty()) {
        if (previous.contains(player1 to player2)) {
            return true
        }
        previous.add(player1 to player2)
        val player1Card = player1.removeFirst()
        val player2Card = player2.removeFirst()
        if (player1Card <= player1.size && player2Card <= player2.size) {
            if (playGame(player1.subList(0, player1Card).toMutableList(), player2.subList(0, player2Card).toMutableList())) {
                player1.addAll(listOf(player1Card, player2Card))
            } else {
                player2.addAll(listOf(player2Card, player1Card))
            }
        } else if (player1Card > player2Card) {
            player1.addAll(listOf(player1Card, player2Card))
        } else {
            player2.addAll(listOf(player2Card, player1Card))
        }
    }
    return player1.isNotEmpty()
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2020/day22/Day22_test")
    check(part1(testInput), 306)
    check(part2(testInput), 291)

    val input = readInput("src/main/kotlin/year2020/day22/Day22")
    println(part1(input))
    println(part2(input))
}
