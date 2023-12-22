package year2023.day08

import readInput
import kotlin.math.max
import kotlin.math.min

fun part1(input: List<String>): Int {
    val instructions = input[0].toList()
    val nodes = input.subList(2, input.size).associate { row ->
        val split = row.split("=")
        val lRNodes = split[1].split(", ").map { node -> node.filter { it.isLetter() } }
        split[0].filter { it.isLetter() } to mapOf('L' to lRNodes[0], 'R' to lRNodes[1])
    }
    var current = "AAA"
    var instruction = 0
    var turns = 0
    while (current != "ZZZ") {
        current = nodes[current]!![instructions[instruction]]!!
        turns++
        instruction++
        if (instruction == instructions.size) {
            instruction = 0
        }
    }
    return turns
}

fun part2(input: List<String>): Long {
    val instructions = input[0].toList()
    val nodes = input.subList(2, input.size).associate { row ->
        val split = row.split("=")
        val lRNodes = split[1].split(", ").map { node -> node.filter { it.isLetterOrDigit() } }
        split[0].filter { it.isLetterOrDigit() } to mapOf('L' to lRNodes[0], 'R' to lRNodes[1])
    }
    return nodes.keys.filter { it.last() == 'A' }.map { start ->
        var currentNode = start
        var instruction = 0
        var turns = 0
        while (currentNode.last() != 'Z') {
            currentNode = nodes[currentNode]!![instructions[instruction]]!!
            turns++
            instruction++
            if (instruction == instructions.size) {
                instruction = 0
            }
        }
        turns.toLong()
    }.reduce { acc, i -> findLCM(acc, i) }
}

fun findLCM(a: Long, b: Long): Long {
    val larger = max(a, b)
    val smaller = min(a, b)
    var lcm = larger
    while (lcm % smaller != 0L) {
        lcm += larger
    }
    return lcm
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2023/day08/Day08_test")
    val testInputPart2 = readInput("src/main/kotlin/year2023/day08/Day08_test_part2")
    check(part1(testInput) == 2)
    check(part2(testInputPart2) == 6L)

    val input = readInput("src/main/kotlin/year2023/day08/Day08")
    println(part1(input))
    println(part2(input))
}
