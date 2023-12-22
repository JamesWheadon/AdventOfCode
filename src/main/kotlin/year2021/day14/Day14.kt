package year2021.day14

import readInput

fun part1(input: List<String>): Int {
    var polymer = input[0]
    val pairs = mutableListOf<Pair<String, String>>()
    input.subList(2, input.size).forEach {
        pairs.add(Pair(it.split(" -> ")[0], it.split(" -> ")[1]))
    }
    for (i in 0..<10) {
        var newPolymer = ""
        polymer.windowed(2).forEach { pair ->
            newPolymer += (pair[0] + pairs.filter { it.first == pair }[0].second)
        }
        newPolymer += polymer.last()
        polymer = newPolymer
    }
    val counts = mutableListOf<Int>()
    polymer.toSet().forEach { letter -> counts.add(polymer.filter { it == letter }.length) }
    return counts.maxOrNull()!! - counts.minOrNull()!!
}

fun part2(input: List<String>): Long {
    val polymer = input[0]
    val pairs = mutableListOf<Pair<String, String>>()
    val characters = mutableListOf<Char>()
    val counts = mutableListOf<Long>()
    input.subList(2, input.size).forEach {
        pairs.add(Pair(it.split(" -> ")[0], it.split(" -> ")[1]))
    }
    polymer.forEach { char ->
        val index = characters.indexOf(char)
        if (index == -1) {
            characters.add(char)
            counts.add(1L)
        } else {
            counts[index] += 1L
        }
    }
    var list = mutableListOf<Pair<String, Long>>()
    polymer.windowed(2).distinct().forEach { pair ->
        list.add(Pair(pair, polymer.windowed(2).count { it == pair }.toLong()))
    }
    for (i in 0..<40) {
        val newList = mutableListOf<Pair<String, Long>>()
        val newPairs = mutableListOf<String>()
        val newCounts = mutableListOf<Long>()
        list.forEach { pair ->
            val newChar = pairs.filter { it.first == pair.first }[0].second
            val numberNew = pair.second
            val pair1 = pair.first[0] + newChar
            val pair2 = newChar + pair.first[1]
            listOf(pair1, pair2).forEach { j ->
                if (newPairs.indexOf(j) == -1) {
                    newPairs.add(j)
                    newCounts.add(numberNew)
                } else {
                    newCounts[newPairs.indexOf(j)] += numberNew
                }
            }
            val index = characters.indexOf(newChar[0])
            if (index == -1) {
                characters.add(newChar[0])
                counts.add(numberNew)
            } else {
                counts[index] += numberNew
            }
        }
        newPairs.forEachIndexed { index, s ->
            newList.add(Pair(s, newCounts[index]))
        }
        list = newList
    }
    return counts.maxOrNull()!! - counts.minOrNull()!!
}

fun main() {
    val testInput1 = readInput("src/main/kotlin/year2021/day14/Day14_test")
    check(part1(testInput1) == 1588)
    check(part2(testInput1) == 2188189693529)

    val input = readInput("src/main/kotlin/year2021/day14/Day14")
    println(part1(input))
    println(part2(input))
}
