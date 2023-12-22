package year2023.day15

import readInput

fun part1(input: List<String>): Int {
    return input.first().split(",").sumOf { symbols ->
        getHash(symbols)
    }
}

fun part2(input: List<String>): Long {
    val resultMap = (0..255).map { it to mutableListOf<String>() }.toMap()
    input.first().split(",").forEach { symbols ->
        val text = symbols.filter { it.isLetter() }
        val code = getHash(text)
        if (symbols.contains("=")) {
            val position = resultMap[code]!!.indexOfFirst { it.substring(0, it.length - 2) == text }
            if (position == -1) {
                resultMap[code]!!.add(symbols)
            } else {
                resultMap[code]!![position] = symbols
            }
        } else {
            resultMap[code]!!.removeIf { it.substring(0, it.length - 2) == text }
        }
    }
    return resultMap.map { (it.key + 1).toLong() * it.value.mapIndexed { index, s -> (index + 1) * s.last().digitToInt().toLong() }.sum() }.sum()
}

private fun getHash(symbols: String): Int {
    var score = 0
    symbols.forEach {
        score += it.code
        score = (score * 17) % 256
    }
    return score
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2023/day15/Day15_test")
    check(part1(testInput) == 1320)
    check(part2(testInput) == 145L)

    val input = readInput("src/main/kotlin/year2023/day15/Day15")
    println(part1(input))
    println(part2(input))
}
