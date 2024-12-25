package year2024.day25

import readInput

fun part1(input: List<String>): Int {
    val keys = mutableListOf<List<Int>>()
    val locks = mutableListOf<List<Int>>()
    var i = 0
    while (i < input.size) {
        val columns = input.subList(i, i + 7).map { it.map { c -> if (c == '#') 1 else 0 } }.reduce { acc, ints ->
            val result = mutableListOf<Int>()
            for (j in acc.indices) {
                result.add(acc[j] + ints[j])
            }
            result
        }
        if (input[i].contains("#")) {
            locks.add(columns)
        } else {
            keys.add(columns)
        }
        i += 8
    }
    return keys.sumOf { key ->
        locks.count { lock ->
            lock.mapIndexed { index, i ->
                i + key[index]
            }.max() <= 7
        }
    }
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2024/day25/Day25_test")
    check(part1(testInput) == 3)

    val input = readInput("src/main/kotlin/year2024/day25/Day25")
    println(part1(input))
}
