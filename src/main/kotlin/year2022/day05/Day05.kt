package year2022.day05

import readInput

fun getStacksAndMoves(input: List<String>): Pair<MutableList<MutableList<String>>, List<String>> {
    val stacks: MutableList<MutableList<String>>
    val setup = input.subList(0, input.indexOf("")).toMutableList()
    val moves = input.subList(input.indexOf("") + 1, input.size)
    val numStacks = setup.removeLast().removeSuffix(" ").substringAfterLast(" ").toInt()
    stacks = MutableList(numStacks) { mutableListOf() }
    setup.reversed().forEach { row ->
        row.chunked((row.length + 1) / numStacks).forEachIndexed { stack, item ->
            if (item.any { it.isLetter() }) {
                stacks[stack].add(item.filter { it.isLetter() })
            }
        }
    }
    return Pair(stacks, moves)
}

fun part1(input: List<String>): String {
    val (stacks: MutableList<MutableList<String>>, moves) = getStacksAndMoves(input)
    moves.forEach { move ->
        val numMoving = move.split(" ")[1].toInt()
        val startStack = move.split(" ")[3].toInt() - 1
        val endStack = move.split(" ")[5].toInt() - 1
        for (i in 1..numMoving) {
            stacks[endStack].add(stacks[startStack].removeLast())
        }
    }
    return stacks.joinToString("") { stack -> stack.last() }
}

fun part2(input: List<String>): String {
    val (stacks: MutableList<MutableList<String>>, moves) = getStacksAndMoves(input)
    moves.forEach { move ->
        val numMoving = move.split(" ")[1].toInt()
        val startStack = move.split(" ")[3].toInt() - 1
        val endStack = move.split(" ")[5].toInt() - 1
        stacks[endStack].addAll(
            stacks[startStack].subList(
                stacks[startStack].size - numMoving,
                stacks[startStack].size,
            ),
        )
        stacks[startStack] = stacks[startStack].subList(0, stacks[startStack].size - numMoving)
    }
    return stacks.joinToString("") { stack -> stack.last() }
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("src/main/kotlin/year2022/day05/Day05_test")
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")

    val input = readInput("src/main/kotlin/year2022/day05/Day05")
    println(part1(input))
    println(part2(input))
}
