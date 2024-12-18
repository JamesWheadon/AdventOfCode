package year2024.day18

import readInput
import java.util.*
import kotlin.math.abs

fun part1(input: List<String>, size: Int, obs: Int): Int {
    val fallen = input.subList(0, obs)
    val nodes = mutableListOf<Node>()
    (0..size).map { x ->
        (0..size).map { y ->
            if (!fallen.contains("$x,$y")) {
                nodes.add(Node(x, y))
            }
        }
    }
    nodes.forEach { node -> node.connected.addAll(nodes.filter { other -> abs(other.x - node.x) + abs(other.y - node.y) == 1 }) }
    val start = nodes.first { it.x == 0 && it.y == 0 }
    val goal = nodes.first { it.x == size && it.y == size }
    val visited = mutableSetOf<Node>()
    val queue = PriorityQueue { t1: Move, t2: Move -> (t1.move + t1.minLeft) - (t2.move + t2.minLeft) }
    queue.add(Move(start, 0, size + size))
    while (true) {
        var current = queue.poll()
        while (visited.contains(current.node)) {
            current = queue.poll()
        }
        if (current.node == goal) {
            return current.move
        }
        queue.addAll(current.node.connected.map { node -> Move(node, current.move + 1, size * 2 - node.x - node.y) })
        visited.add(current.node)
    }
}

fun part2(input: List<String>, size: Int): String {
    val nodes = mutableListOf<Node>()
    (0..size).map { x ->
        (0..size).map { y ->
            nodes.add(Node(x, y))
        }
    }
    nodes.forEach { node -> node.connected.addAll(nodes.filter { other -> abs(other.x - node.x) + abs(other.y - node.y) == 1 }) }
    val start = nodes.first { it.x == 0 && it.y == 0 }
    val goal = nodes.first { it.x == size && it.y == size }
    for (i in input.indices) {
        val blockedNode = nodes.first { node -> "${node.x},${node.y}" == input[i] }
        blockedNode.connected.forEach { node -> node.connected.remove(blockedNode) }
        nodes.remove(blockedNode)
        val visited = mutableSetOf<Node>()
        val queue = PriorityQueue { t1: Move, t2: Move -> (t1.move + t1.minLeft) - (t2.move + t2.minLeft) }
        queue.add(Move(start, 0, size + size))
        while (true) {
            var current = queue.poll() ?: return input[i]
            while (visited.contains(current.node)) {
                current = queue.poll() ?: return input[i]
            }
            if (current.node == goal) {
                break
            }
            queue.addAll(current.node.connected.map { node ->
                Move(
                    node,
                    current.move + 1,
                    size * 2 - node.x - node.y
                )
            })
            visited.add(current.node)
        }
    }
    return ""
}

data class Node(val x: Int, val y: Int) {
    val connected = mutableListOf<Node>()
}

data class Move(val node: Node, val move: Int, val minLeft: Int)

fun main() {
    val testInput = readInput("src/main/kotlin/year2024/day18/Day18_test")
    check(part1(testInput, 6, 12) == 22)
    check(part2(testInput, 6) == "6,1")

    val input = readInput("src/main/kotlin/year2024/day18/Day18")
    println(part1(input, 70, 1024))
    println(part2(input, 70))
}
