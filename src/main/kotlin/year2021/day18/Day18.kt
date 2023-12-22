package year2021.day18

import readInput

fun joinTrees(sum: Tree, line: Tree): Tree {
    val newTree = Tree()
    val root = Node(null, null)
    root.children.add(sum.root)
    root.children.add(line.root)
    sum.root.parent = root
    line.root.parent = root
    newTree.root = root
    newTree.findDepths()
    return newTree
}

fun part1(input: List<String>): Int {
    val sum = createTree(input)
    return sum.getMagnitude()
}

private fun createTree(input: List<String>): Tree {
    var sum = Tree()
    input.forEach {
        val line = Tree()
        val root = Node(null, null)
        line.root = root
        it.drop(1).forEach { symbol ->
            if (symbol.isDigit()) {
                line.addDigit(symbol.digitToInt())
            } else if (symbol == '[') {
                line.addNode()
            }
        }
        sum = (if (it == input[0]) line else joinTrees(sum, line))
        sum.reduce()
    }
    return sum
}

fun part2(input: List<String>): Int {
    val magnitudes = mutableListOf<Int>()
    input.forEach { first ->
        input.filter { it != first }.forEach { second ->
            val sum = createTree(mutableListOf(first, second))
            magnitudes.add(sum.getMagnitude())
        }
    }
    return magnitudes.maxOrNull()!!
}

fun main() {
    val testInput1 = readInput("src/main/kotlin/year2021/day18/Day18_test")
    check(part1(testInput1) == 4140)
    check(part2(testInput1) == 3993)

    val input = readInput("src/main/kotlin/year2021/day18/Day18")
    println(part1(input))
    println(part2(input))
}
