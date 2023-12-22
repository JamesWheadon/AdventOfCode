package year2023.day07

import readInput

fun part1(input: List<String>): Int {
    val order = listOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2')
    return input.map {
        val handRank = it.split(" ")
        handRank[0] to handRank[1].toInt()
    }.sortedWith(
        compareBy<Pair<String, Int>> { hand ->
            hand.first.toList().groupingBy { card -> card }.eachCount().maxOf { it.value }
        }.thenComparing { hand ->
            val cardCount = hand.first.toList().groupingBy { card -> card }.eachCount()
            checkForPair(cardCount)
        }.thenByDescending { hand ->
            hand.first.map { order.indexOf(it) }.joinToString("") { Integer.toHexString(it) }
        },
    ).mapIndexed { index, pair ->
        (index + 1) * pair.second
    }.sum()
}

fun part2(input: List<String>): Int {
    val order = listOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J')
    return input.map {
        val handRank = it.split(" ")
        handRank[0] to handRank[1].toInt()
    }.sortedWith(
        compareBy<Pair<String, Int>> { hand ->
            val cardCount = getCardCount(hand)
            cardCount.maxOf { it.value }
        }.thenComparing { hand ->
            val eachCount = getCardCount(hand)
            checkForPair(eachCount)
        }.thenByDescending { hand ->
            hand.first.map { order.indexOf(it) }.joinToString("") { Integer.toHexString(it) }
        },
    ).mapIndexed { index, pair ->
        (index + 1) * pair.second
    }.sum()
}

private fun checkForPair(cardCount: Map<Char, Int>) = if (cardCount.maxOf { it.value } == 3) {
    cardCount.values.contains(2).compareTo(true)
} else if (cardCount.maxOf { it.value } == 2) {
    (cardCount.values.count { it == 2 } == 2).compareTo(true)
} else {
    0
}

private fun getCardCount(hand: Pair<String, Int>): Map<Char, Int> {
    val mostCommon = hand.first.replace("J", "").maxByOrNull { char -> hand.first.count { c -> c == char } }
        ?: return hand.first.toList().groupingBy { card -> card }.eachCount()
    return hand.first.replace("J", mostCommon.toString()).toList().groupingBy { card -> card }.eachCount()
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2023/day07/Day07_test")
    check(part1(testInput) == 6440)
    check(part2(testInput) == 5905)

    val input = readInput("src/main/kotlin/year2023/day07/Day07")
    check(part1(input) == 247823654)
    check(part2(input) == 245461700)
    println(part1(input))
    println(part2(input))
}
