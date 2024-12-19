package year2024.day19

import readInput

fun part1(input: List<String>): Int {
    val towels = input[0].split(", ")
    return input.subList(2, input.size).count { pattern ->
        val searched = mutableSetOf<String>()
        patternFromTowels(pattern, towels, searched)
    }
}

fun part2(input: List<String>): Long {
    val towels = input[0].split(", ")
    return input.subList(2, input.size).sumOf { pattern ->
        val searched = mutableMapOf<String, MutableSet<String>>()
        patternFromTowels(pattern, towels, searched)
        getPossible("", searched, pattern, mutableMapOf())
    }
}

fun getPossible(current: String, combos: MutableMap<String, MutableSet<String>>, start: String, cached: MutableMap<String, Long>): Long {
    return combos[current]?.sumOf { combo ->
        if (combo == start) {
            1L
        } else {
            val count = cached[combo] ?: getPossible(combo, combos, start, cached)
            cached[combo] = count
            count
        }
    } ?: 0L
}

fun patternFromTowels(pattern: String, towels: List<String>, searched: MutableSet<String>): Boolean {
    if (pattern in searched) {
        return false
    }
    val remaining = towels.filter { pattern.indexOf(it) == 0 }.map { pattern.removePrefix(it) }
    searched.add(pattern)
    return if (remaining.isEmpty()) {
        false
    } else if (remaining.any { it.isEmpty() }) {
        true
    } else {
        remaining.any { patternFromTowels(it, towels, searched) }
    }
}

fun patternFromTowels(pattern: String, towels: List<String>, searched: MutableMap<String, MutableSet<String>>) {
    val remaining = towels.filter { pattern.indexOf(it) == 0 }.map { pattern.removePrefix(it) }
    remaining.forEach {
        if (it in searched) {
            searched[it]!!.add(pattern)
        } else {
            searched[it] = mutableSetOf(pattern)
            patternFromTowels(it, towels, searched)
        }
    }
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2024/day19/Day19_test")
    check(part1(testInput) == 6)
    check(part2(testInput) == 16L)

    val input = readInput("src/main/kotlin/year2024/day19/Day19")
    println(part1(input))
    println(part2(input))
}
