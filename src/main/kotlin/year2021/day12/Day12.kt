package year2021.day12

import readInput

fun findPaths(
    startNode: Node,
    endNode: Node,
    currentPath: MutableList<Node>,
    paths: MutableList<MutableList<Node>>,
) {
    startNode.connectedTo.forEach {
        val thisPath = currentPath.toMutableList()
        thisPath.add(it)
        if (it == endNode) {
            if (!paths.contains(thisPath)) {
                paths.add(thisPath)
            }
        } else {
            if (it.bigCave) {
                findPaths(it, endNode, thisPath, paths)
            } else {
                if (!currentPath.contains(it)) {
                    findPaths(it, endNode, thisPath, paths)
                }
            }
        }
    }
}

fun part1(input: List<String>): Int {
    val (startNode, endNode, paths) = setUp(input)
    val currentPath = mutableListOf(startNode)
    findPaths(startNode, endNode, currentPath, paths)
    return paths.size
}

private fun setUp(input: List<String>): Triple<Node, Node, MutableList<MutableList<Node>>> {
    val nodes = mutableListOf<Node>()
    val nodeNames = mutableListOf<String>()
    input.forEach { pair ->
        val node1: Node
        val node2: Node
        val name1 = pair.split("-")[0]
        val name2 = pair.split("-")[1]

        if (nodeNames.contains(name1)) {
            node1 = nodes.filter { it.name == name1 }[0]
        } else {
            node1 = Node(name1, name1.uppercase() == name1)
            nodes.add(node1)
            nodeNames.add(name1)
        }

        if (nodeNames.contains(name2)) {
            node2 = nodes.filter { it.name == name2 }[0]
        } else {
            node2 = Node(name2, name2.uppercase() == name2)
            nodes.add(node2)
            nodeNames.add(name2)
        }
        node1.connectTo(node2)
    }
    val startNode = nodes.filter { it.name == "start" }[0]
    val endNode = nodes.filter { it.name == "end" }[0]
    val paths = mutableListOf<MutableList<Node>>()
    return Triple(startNode, endNode, paths)
}

fun findPathsDoubleSmall(
    startNode: Node,
    endNode: Node,
    currentPath: MutableList<Node>,
    paths: MutableList<MutableList<Node>>,
) {
    startNode.connectedTo.forEach { node ->
        val thisPath = currentPath.toMutableList()
        thisPath.add(node)
        if (node == endNode) {
            if (!paths.contains(thisPath)) {
                paths.add(thisPath)
            }
        } else {
            val noRepeats =
                currentPath.filter { !it.bigCave }.distinct().size == currentPath.filter { !it.bigCave }.size
            if (node.bigCave || (noRepeats && node.name != "start") || !currentPath.contains(node)) {
                findPathsDoubleSmall(node, endNode, thisPath, paths)
            }
        }
    }
}

fun part2(input: List<String>): Int {
    val (startNode, endNode, paths) = setUp(input)
    val currentPath = mutableListOf(startNode)
    findPathsDoubleSmall(startNode, endNode, currentPath, paths)
    return paths.size
}

fun main() {
    val testInput1 = readInput("src/main/kotlin/year2021/day12/Day12_test1")
    check(part1(testInput1) == 10)
    check(part2(testInput1) == 36)

    val testInput2 = readInput("src/main/kotlin/year2021/day12/Day12_test2")
    check(part1(testInput2) == 19)
    check(part2(testInput2) == 103)

    val testInput3 = readInput("src/main/kotlin/year2021/day12/Day12_test3")
    check(part1(testInput3) == 226)
    check(part2(testInput3) == 3509)

    val input = readInput("src/main/kotlin/year2021/day12/Day12")
    println(part1(input))
    println(part2(input))
}

class Node(val name: String, val bigCave: Boolean) {
    val connectedTo = mutableListOf<Node>()

    fun connectTo(endNode: Node) {
        connectedTo.add(endNode)
        endNode.connectedTo.add(this)
    }
}
