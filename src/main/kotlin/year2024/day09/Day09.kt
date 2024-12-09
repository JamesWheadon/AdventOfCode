package year2024.day09

import readInput

fun part1(input: List<String>): Long {
    val blocks = getBlocks(input)
    while (blocks.indexOf(-1) < blocks.indexOfLast { it > 0 } && blocks.indexOf(-1) != -1) {
        val lastDigit = blocks.indexOfLast { it > 0 }
        val firstDot = blocks.indexOf(-1)
        blocks[firstDot] = blocks[lastDigit]
        blocks[lastDigit] = -1
    }
    return blocks.mapIndexed { index, i ->
        if (i > 0) {
            index * i.toLong()
        } else {
            0
        }
    }.sum()
}

fun part2(input: List<String>): Long {
    val blocks = getBlocks(input)
    var next = blocks.max()
    while (next > 0) {
        val index = blocks.indexOfFirst { it == next }
        val count = blocks.count{ it == next }
        for (i in 0..<index) {
            if (blocks.subList(i, i + count).distinct() == listOf(-1)) {
                for (j in 0 ..< count) {
                    blocks[index + j] = -1
                    blocks[i + j] = next
                }
                break
            }
        }
        next -= 1
    }
    return blocks.mapIndexed { index, i ->
        if (i > 0) {
            index * i.toLong()
        } else {
            0
        }
    }.sum()
}

private fun getBlocks(input: List<String>) = input[0].flatMapIndexed { index, c ->
    val count = c.digitToInt()
    if (index % 2 == 0) {
        List(count) { index / 2 }
    } else {
        List(count) { -1 }
    }
}.toMutableList()

fun main() {
    val testInput = readInput("src/main/kotlin/year2024/day09/Day09_test")
    check(part1(testInput) == 1928L)
    check(part2(testInput) == 2858L)

    val input = readInput("src/main/kotlin/year2024/day09/Day09")
    println(part1(input))
    println(part2(input))
}
