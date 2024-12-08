package year2024.day08

import readInput

fun part1(input: List<String>): Int {
    val antennae = getAntennae(input)
    val nodes = mutableSetOf<String>()
    antennae.entries.map { it.value }.forEach { groups ->
        for (i in groups.indices) {
            for (j in i + 1 ..< groups.size) {
                val first = groups[i]
                val second = groups[j]
                val xDiff = first.first - second.first
                val yDiff = first.second - second.second
                if (first.first + xDiff in input[0].indices && first.second + yDiff in input.indices) {
                    nodes.add("${first.first + xDiff},${first.second + yDiff}")
                }
                if (second.first - xDiff in input[0].indices && second.second - yDiff in input.indices) {
                    nodes.add("${second.first - xDiff},${second.second - yDiff}")
                }
            }
        }
    }
    return nodes.size
}

fun part2(input: List<String>): Int {
    val antennae = getAntennae(input)
    val nodes = mutableSetOf<String>()
    antennae.entries.map { it.value }.forEach { groups ->
        for (i in groups.indices) {
            for (j in i + 1 ..< groups.size) {
                val first = groups[i]
                val second = groups[j]
                var xDiff = first.first - second.first
                var yDiff = first.second - second.second
                val hCM = hCM(xDiff, yDiff)
                xDiff /= hCM
                yDiff /= hCM
                var k = 1
                nodes.add("${first.first},${first.second}")
                while (first.first + xDiff * k in input[0].indices && first.second + yDiff * k in input.indices) {
                    nodes.add("${first.first + xDiff * k},${first.second + yDiff * k}")
                    k += 1
                }
                k = 1
                while (first.first - xDiff * k in input[0].indices && first.second - yDiff * k in input.indices) {
                    nodes.add("${first.first - xDiff * k},${first.second - yDiff * k}")
                    k += 1
                }
            }
        }
    }
    return nodes.size
}

private fun getAntennae(input: List<String>): MutableMap<Char, MutableList<Pair<Int, Int>>> {
    val antennae = mutableMapOf<Char, MutableList<Pair<Int, Int>>>()
    input.forEachIndexed { col, s ->
        s.forEachIndexed { row, c ->
            if (c != '.') {
                if (antennae.containsKey(c)) {
                    antennae[c]!!.add(Pair(row, col))
                } else {
                    antennae[c] = mutableListOf(Pair(row, col))
                }
            }
        }
    }
    return antennae
}

private fun hCM(a: Int, b: Int): Int {
    var num1 = a
    var num2 = b
    while (num2 != 0) {
        val temp = num2
        num2 = num1 % num2
        num1 = temp
    }
    return num1
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2024/day08/Day08_test")
    check(part1(testInput) == 14)
    check(part2(testInput) == 34)

    val input = readInput("src/main/kotlin/year2024/day08/Day08")
    println(part1(input))
    println(part2(input))
}
