package year2024.day20

import readInput
import java.util.PriorityQueue
import kotlin.math.abs

fun bothParts(input: List<String>, limit: Int, shortcut: Int): Int {
    val (start, end) = getNodes(input)
    val path = getPath(start, end)
    return path.sumOf { move ->
        path.count { other ->
            abs(move.node.x - other.node.x) + abs(move.node.y - other.node.y) <= shortcut &&
                    move.cost - other.cost >= limit + abs(move.node.x - other.node.x) + abs(move.node.y - other.node.y)
        }
    }
}

private fun getNodes(input: List<String>): Pair<Node, Node> {
    var start = Node(0, 0)
    var end = Node(0, 0)
    val nodes = input.mapIndexed { row, s ->
        s.mapIndexed { col, c ->
            if (c != '#') {
                val node = Node(col, row)
                if (c == 'S') start = node
                if (c == 'E') end = node
                node
            } else {
                null
            }
        }
    }.flatten().filterNotNull()
    nodes.forEach { node ->
        node.connected.addAll(
            nodes.filter { other -> abs(node.x - other.x) + abs(node.y - other.y) == 1 }
        )
    }
    return Pair(start, end)
}

private fun getPath(start: Node, end: Node): MutableList<Move> {
    val queue = PriorityQueue<Move> { m1, m2 -> m2.cost - m1.cost }
    val visited = mutableSetOf<Node>()
    queue.add(Move(start, 0, null))
    val endMove: Move?
    while (true) {
        var current = queue.poll()
        if (current.node == end) {
            endMove = current
            break
        }
        while (current.node in visited) {
            current = queue.poll()
        }
        queue.addAll(current.node.connected.filter { node -> node !in visited }
            .map { node -> Move(node, current.cost + 1, current) })
        visited.add(current.node)
    }
    val path = mutableListOf<Move>()
    var current = endMove
    while (current != null) {
        path.add(current)
        current = current.parent
    }
    return path
}

data class Node(val x: Int, val y: Int) {
    val connected = mutableListOf<Node>()
}

data class Move(val node: Node, val cost: Int, val parent: Move?)

fun main() {
    val testInput = readInput("src/main/kotlin/year2024/day20/Day20_test")
    check(bothParts(testInput, 20, 2) == 5)
    check(bothParts(testInput, 50, 20) == 285)

    val input = readInput("src/main/kotlin/year2024/day20/Day20")
    println(bothParts(input, 100, 2))
    println(bothParts(input, 100, 20))
}
