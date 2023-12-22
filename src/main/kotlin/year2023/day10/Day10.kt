package year2023.day10

import readInput
import kotlin.math.abs

fun part1(input: List<String>): Int {
    val nodes = input.mapIndexed { y, s -> s.mapIndexed { x, c -> Node(x, y, c) } }.flatten()
    val start = nodes.first { it.c == 'S' }
    val visited = mutableListOf(start)
    var next = getConnectedToStart(nodes, start)
    var count = 1
    do {
        next = next.map { node ->
            getConnectedNodes(node, nodes, visited)
        }.flatten()
        visited.addAll(next)
        count++
    } while (next[0] != next[1])
    return count
}

fun part2(input: List<String>): Int {
    val nodes = input.mapIndexed { y, s -> s.mapIndexed { x, c -> Node(x, y, c) } }.flatten()
    val start = nodes.first { it.c == 'S' }
    start.loop = true
    val visited = mutableListOf(start)
    var next = getConnectedToStart(nodes, start)
    getStartSymbol(next, start)
    start.next = next
    do {
        next = next.map { node ->
            getConnectedNodes(node, nodes, visited)
        }.flatten()
        visited.addAll(next)
    } while (next[0] != next[1])
    getConnectedNodes(next[0], nodes, visited)
    visited.forEach { it.loop = true }
    getLoopNodesInsideDirection(visited, nodes)
    val otherNodes = nodes.filter { !it.loop }.toMutableList()
    val groups = mutableListOf(mutableListOf(otherNodes.removeAt(0)))
    while (otherNodes.isNotEmpty()) {
        val otherNodeSize = otherNodes.size
        groups.forEach { group ->
            otherNodes.filter { nextToGroup(it, group) }.forEach {
                group.add(it)
                otherNodes.remove(it)
            }
        }
        if (otherNodes.size == otherNodeSize) {
            groups.add(mutableListOf(otherNodes.removeAt(0)))
        }
    }
    return groups.filter { group ->
        val (groupNode, loopNode) = loopNextToGroup(group, visited)
        when {
            loopNode.interiorDirection.contains("s") && groupNode.y == loopNode.y + 1 -> {
                true
            }

            loopNode.interiorDirection.contains("w") && groupNode.y == loopNode.y - 1 -> {
                true
            }

            loopNode.interiorDirection.contains("d") && groupNode.x == loopNode.x + 1 -> {
                true
            }

            loopNode.interiorDirection.contains("a") && groupNode.x == loopNode.x - 1 -> {
                true
            }

            else -> {
                false
            }
        }
    }.sumOf { it.size }
}

private fun getLoopNodesInsideDirection(
    visited: MutableList<Node>,
    nodes: List<Node>
) {
    val yLimits = listOf(visited.minOf { it.y }, visited.maxOf { it.y })
    val xLimits = listOf(visited.minOf { it.x }, visited.maxOf { it.x })
    val loopStart = nodes.first { it.loop && (it.y in yLimits || it.x in xLimits) }
    when {
        loopStart.y == yLimits[0] -> {
            loopStart.interiorDirection += "s"
        }

        loopStart.y == yLimits[1] -> {
            loopStart.interiorDirection += "w"
        }

        loopStart.x == xLimits[0] -> {
            loopStart.interiorDirection += "d"
        }

        else -> {
            loopStart.interiorDirection += "a"
        }
    }
    loopStart.directionFound = true
    var loop = loopStart.next
    val directions = mapOf(
        '|' to listOf("a", "d"),
        '-' to listOf("w", "s"),
        'L' to listOf("sa", "wd"),
        'J' to listOf("wa", "sd"),
        'F' to listOf("wa", "sd"),
        '7' to listOf("sa", "wd"),
    )
    while (loop.isNotEmpty()) {
        loop.filter { !it.directionFound }.forEach { node ->
            val directionNode = node.next.first { it.directionFound }
            when (node.y) {
                directionNode.y -> {
                    node.interiorDirection = directions[node.c]!!.first {
                        it.contains(directionNode.interiorDirection.replace("[ad]".toRegex(), ""))
                    }
                }

                else -> {
                    node.interiorDirection = directions[node.c]!!.first {
                        it.contains(directionNode.interiorDirection.replace("[ws]".toRegex(), ""))
                    }
                }
            }
            node.directionFound = true
        }
        loop = loop.map { it.next }.flatten().filter { !it.directionFound }
    }
}

fun loopNextToGroup(group: MutableList<Node>, visited: MutableList<Node>): Pair<Node, Node> {
    for (node in group) {
        val nextToLoop = visited.firstOrNull { abs(it.x - node.x) + abs(it.y - node.y) == 1 }
        if (nextToLoop != null) {
            return Pair(node, nextToLoop)
        }
    }
    return Pair(group.first(), visited.first())
}

fun nextToGroup(node: Node, group: MutableList<Node>): Boolean {
    return group.any { abs(it.x - node.x) + abs(it.y - node.y) == 1 }
}

private fun getConnectedToStart(
    nodes: List<Node>,
    start: Node,
): List<Node> {
    val nextToStart = nodes.filter {
        it.x == start.x && it.y == start.y + 1 && it.c in listOf('L', 'J', '|') ||
            it.x == start.x && it.y == start.y - 1 && it.c in listOf('F', '7', '|') ||
            it.x == start.x + 1 && it.y == start.y && it.c in listOf('J', '7', '-') ||
            it.x == start.x - 1 && it.y == start.y && it.c in listOf('F', 'L', '-')
    }
    return nextToStart
}

private fun getStartSymbol(nextToStart: List<Node>, start: Node) {
    if (nextToStart[0].x == nextToStart[1].x) {
        start.c = '-'
    } else if (nextToStart[0].y == nextToStart[1].y) {
        start.c = '|'
    } else {
        val up = nextToStart.any { it.y == start.y - 1 }
        val left = nextToStart.any { it.x == start.x - 1 }
        if (up) {
            if (left) {
                start.c = 'J'
            } else {
                start.c = 'L'
            }
        } else {
            if (left) {
                start.c = '7'
            } else {
                start.c = 'F'
            }
        }
    }
}

private fun getConnectedNodes(
    node: Node,
    nodes: List<Node>,
    visited: MutableList<Node>,
): List<Node> {
    when (node.c) {
        '|' -> {
            node.next = nodes.filter { it.x == node.x && abs(it.y - node.y) == 1 }
        }

        '-' -> {
            node.next = nodes.filter { abs(it.x - node.x) == 1 && it.y == node.y }
        }

        'L' -> {
            node.next =
                nodes.filter { (it.x == node.x && it.y == node.y - 1) || (it.x == node.x + 1 && it.y == node.y) }
        }

        'J' -> {
            node.next =
                nodes.filter { (it.x == node.x && it.y == node.y - 1) || (it.x == node.x - 1 && it.y == node.y) }
        }

        '7' -> {
            node.next =
                nodes.filter { (it.x == node.x && it.y == node.y + 1) || (it.x == node.x - 1 && it.y == node.y) }
        }

        'F' -> {
            node.next =
                nodes.filter { (it.x == node.x && it.y == node.y + 1) || (it.x == node.x + 1 && it.y == node.y) }
        }
    }
    return node.next.filter { !visited.contains(it) }
}

data class Node(
    val x: Int,
    val y: Int,
    var c: Char,
    var loop: Boolean = false,
    var interiorDirection: String = "",
    var next: List<Node> = listOf(),
    var directionFound: Boolean = false,
) {
    override fun toString(): String {
        return "Node(x=$x, y=$y, c=$c, in=$interiorDirection)"
    }
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2023/day10/Day10_test")
    val testInputPart21 = readInput("src/main/kotlin/year2023/day10/Day10_test_part2_1")
    val testInputPart22 = readInput("src/main/kotlin/year2023/day10/Day10_test_part2_2")
    val testInputPart23 = readInput("src/main/kotlin/year2023/day10/Day10_test_part2_3")
    check(part1(testInput) == 8)
    check(part2(testInputPart21) == 4)
    check(part2(testInputPart22) == 8)
    check(part2(testInputPart23) == 10)

    val input = readInput("src/main/kotlin/year2023/day10/Day10")
    println(part1(input))
    println(part2(input))
}
