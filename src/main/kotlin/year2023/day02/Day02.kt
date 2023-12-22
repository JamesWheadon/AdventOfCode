package year2023.day02

import readInput

fun part1(input: List<String>): Int {
    val allowedValues = mapOf("red" to 12, "green" to 13, "blue" to 14)
    return input.filter { game ->
        val (_, rounds) = getIdAndRounds(game)
        !rounds.map { round ->
            round.split(",").map { colour ->
                val limit = allowedValues[colour.filter { it.isLetter() }]
                limit!! < colour.filter { it.isDigit() }.toInt()
            }.reduce { acc, b -> acc || b }
        }.reduce { acc, b -> acc || b }
    }.sumOf { game -> getIdAndRounds(game).first }
}

fun getIdAndRounds(game: String): Pair<Int, List<String>> {
    val split = game.split(":")
    val id = split[0].filter { it.isDigit() }.toInt()
    val rounds = split[1].split(";")
    rounds.forEach { it.replace(" ", "") }
    return Pair(id, rounds)
}

fun part2(input: List<String>): Int {
    return input.sumOf { game ->
        val (_, rounds) = getIdAndRounds(game)
        val colours = mutableMapOf("red" to 0, "green" to 0, "blue" to 0)
        rounds.map { round ->
            round.split(",").map { colour ->
                val amount = colour.filter { it.isDigit() }.toInt()
                if (amount > colours[colour.filter { it.isLetter() }]!!) {
                    colours[colour.filter { it.isLetter() }] = amount
                }
            }
        }
        colours.entries.map { it.value }.reduce { acc, i -> acc * i }
    }
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2023/day02/Day02_test")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("src/main/kotlin/year2023/day02/Day02")
    println(part1(input))
    println(part2(input))
}
