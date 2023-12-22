package year2021.day24

import readInput

fun part1(input: List<String>): Long {
    val blocks = input.chunked(18)
    val result = MutableList(14) { 0 }
    val buffer = mutableListOf<Pair<Int, Int>>()
    blocks.forEachIndexed { index, instructions ->
        if ("div z 26" in instructions) {
            val offset = instructions.last { it.startsWith("add x") }.split(" ").last().toInt()
            val (lastIndex, lastOffset) = buffer.removeFirst()
            val difference = offset + lastOffset
            if (difference >= 0) {
                result[lastIndex] = 9 - difference
                result[index] = 9
            } else {
                result[lastIndex] = 9
                result[index] = 9 + difference
            }
        } else {
            buffer.add(0, Pair(index, instructions.last { it.startsWith("add y") }.split(" ").last().toInt()))
        }
    }
    return result.joinToString("").toLong()
}

fun part2(input: List<String>): Long {
    val blocks = input.chunked(18)
    val result = MutableList(14) { 0 }
    val buffer = mutableListOf<Pair<Int, Int>>()
    blocks.forEachIndexed { index, instructions ->
        if ("div z 26" in instructions) {
            val offset = instructions.last { it.startsWith("add x") }.split(" ").last().toInt()
            val (lastIndex, lastOffset) = buffer.removeFirst()
            val difference = offset + lastOffset
            if (difference >= 0) {
                result[lastIndex] = 1
                result[index] = 1 + difference
            } else {
                result[lastIndex] = 1 - difference
                result[index] = 1
            }
        } else {
            buffer.add(0, Pair(index, instructions.last { it.startsWith("add y") }.split(" ").last().toInt()))
        }
    }
    return result.joinToString("").toLong()
}

fun main() {
    val input = readInput("src/main/kotlin/year2021/day24/Day24")
    println(part1(input))
    println(part2(input))
}
