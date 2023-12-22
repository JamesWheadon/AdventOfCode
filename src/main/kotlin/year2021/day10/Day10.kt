package year2021.day10

import readInput

fun part1(input: List<String>): Int {
    var total = 0
    val scores = listOf(Pair(')', 3), Pair(']', 57), Pair('}', 1197), Pair('>', 25137))
    input.forEach loop@{
        val symbols = mutableListOf<Char>()
        it.forEach { symbol ->
            if (symbol == '(' || symbol == '[' || symbol == '{' || symbol == '<') {
                symbols.add(symbol)
            } else {
                val lastSymbol = symbols.last()
                if ((symbol == ')' && lastSymbol == '(') ||
                    (symbol == ']' && lastSymbol == '[') ||
                    (symbol == '}' && lastSymbol == '{') ||
                    (symbol == '>' && lastSymbol == '<')
                ) {
                    symbols.removeLast()
                } else {
                    total += scores.filter { score -> score.first == symbol }[0].second
                    return@loop
                }
            }
        }
    }
    return total
}

fun part2(input: List<String>): Long {
    val incompleteLines = mutableListOf<MutableList<Char>>()
    input.forEach loop@{
        val symbols = mutableListOf<Char>()
        it.forEach { symbol ->
            if (symbol == '(' || symbol == '[' || symbol == '{' || symbol == '<') {
                symbols.add(symbol)
            } else {
                val lastSymbol = symbols.last()
                if ((symbol == ')' && lastSymbol == '(') ||
                    (symbol == ']' && lastSymbol == '[') ||
                    (symbol == '}' && lastSymbol == '{') ||
                    (symbol == '>' && lastSymbol == '<')
                ) {
                    symbols.removeLast()
                } else {
                    return@loop
                }
            }
        }
        incompleteLines.add(symbols)
    }
    val scores = getScores(incompleteLines)
    return scores.sortedDescending()[(scores.size - 1) / 2]
}

private fun getScores(incompleteLines: MutableList<MutableList<Char>>): MutableList<Long> {
    val scores = mutableListOf<Long>()
    incompleteLines.forEach {
        var score = 0L
        for (i in it.indices.reversed()) {
            score *= 5
            when {
                it[i] == '(' -> {
                    score += 1
                }

                it[i] == '[' -> {
                    score += 2
                }

                it[i] == '{' -> {
                    score += 3
                }

                it[i] == '<' -> {
                    score += 4
                }
            }
        }
        scores.add(score)
    }
    return scores
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2021/day10/Day10_test")
    check(part1(testInput) == 26397)
    check(part2(testInput) == 288957L)

    val input = readInput("src/main/kotlin/year2021/day10/Day10")
    println(part1(input))
    println(part2(input))
}
