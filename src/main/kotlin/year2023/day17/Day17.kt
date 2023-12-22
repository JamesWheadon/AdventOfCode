package year2023.day17

import readInput
import kotlin.math.abs

fun part1(input: List<String>, lowerBound: Int = 0, upperBound: Int = 3): Int {
    val (nodes, startNode, goalNode) = getNodesStartAndGoal(input)
    val pathNodes = mutableListOf(PathNode(startNode, 0, getFEstimate(startNode, goalNode), null, 0, 's', lowerBound, upperBound))
    val minNodesFound: Map<Node, MutableMap<Int, MutableMap<Char, Int?>>> = nodes.associateWith {
        val map = mutableMapOf<Int, MutableMap<Char, Int?>>()
        for (i in 1..upperBound) {
            map[i] = mutableMapOf('a' to null, 'd' to null, 'w' to null, 's' to null)
        }
        map
    }
    for (i in 1..3) {
        for (c in listOf('w', 'a', 's', 'd')) {
            minNodesFound[startNode]?.get(i)?.set(c, 0)
        }
    }
    while (true) {
        val minF = pathNodes.minOf { it.f }
        val minPathNodes = pathNodes.filter { it.f == minF }
        if (minPathNodes.any { it.node == goalNode }) {
            return minF
        }
        pathNodes.removeAll(minPathNodes)
        val newPathNodes = minPathNodes.map {
            it.getNextMoves(goalNode)
        }.flatten()
        newPathNodes.forEach { newNode ->
            val current = minNodesFound[newNode.node]!![newNode.straightCount]!![newNode.direction]
            if (current == null || current > newNode.f) {
                minNodesFound[newNode.node]!![newNode.straightCount]!![newNode.direction] = newNode.f
                pathNodes.removeAll(pathNodes.filter { it.node == newNode.node && it.direction == newNode.direction && it.straightCount == newNode.straightCount })
                pathNodes.add(newNode)
            }
        }
    }
}

private fun getNodesStartAndGoal(input: List<String>): Triple<List<Node>, Node, Node> {
    val nodes = input.mapIndexed { row, s -> s.mapIndexed { column, c -> Node(row, column, c.digitToInt()) } }.flatten()
    nodes.forEach { node ->
        node.neighbours = listOf(
            'a' to nodes.firstOrNull { it.row == node.row && it.column == node.column - 1 },
            'd' to nodes.firstOrNull { it.row == node.row && it.column == node.column + 1 },
            'w' to nodes.firstOrNull { it.row == node.row - 1 && it.column == node.column },
            's' to nodes.firstOrNull { it.row == node.row + 1 && it.column == node.column },
        ).filter { it.second != null }.associate { Pair(it.first, it.second!!) }
    }
    val startNode = nodes.first { it.row == 0 && it.column == 0 }
    val goalNode = nodes.first { it.row == input.lastIndex && it.column == input[0].lastIndex }
    return Triple(nodes, startNode, goalNode)
}

fun getFEstimate(current: Node, goal: Node): Int = (abs(current.column - goal.column) + abs(current.row - goal.row))

fun main() {
    val testInput = readInput("src/main/kotlin/year2023/day17/Day17_test")
    check(part1(testInput) == 102)
    check(part1(testInput, 4, 10) == 94)

    val input = readInput("src/main/kotlin/year2023/day17/Day17")
    println(part1(input))
    println(part1(input, 4, 10))
}

class Node(val row: Int, val column: Int, val heatLoss: Int) {
    var neighbours: Map<Char, Node> = mapOf()
}

class PathNode(
    val node: Node,
    private val g: Int,
    val f: Int,
    private val parent: PathNode?,
    val straightCount: Int,
    val direction: Char,
    private val lowerBound: Int,
    private val upperBound: Int,
) {
    fun getNextMoves(goalNode: Node): List<PathNode> {
        return if (parent == null) {
            node.neighbours.map { (direction, node) ->
                PathNode(
                    node,
                    g + node.heatLoss,
                    g + node.heatLoss + getFEstimate(node, goalNode),
                    this,
                    1,
                    direction,
                    lowerBound,
                    upperBound,
                )
            }
        } else {
            val straightNode = node.neighbours[direction]
            if (straightCount < lowerBound) {
                node.neighbours.filter { it.value == straightNode }
                    .map { (direction, node) ->
                        PathNode(
                            node,
                            g + node.heatLoss,
                            g + node.heatLoss + getFEstimate(node, goalNode),
                            this,
                            if (node == straightNode) straightCount + 1 else 1,
                            direction,
                            lowerBound,
                            upperBound,
                        )
                    }
            } else if (straightCount < upperBound) {
                node.neighbours.filter { it.value != parent.node }
                    .map { (direction, node) ->
                        PathNode(
                            node,
                            g + node.heatLoss,
                            g + node.heatLoss + getFEstimate(node, goalNode),
                            this,
                            if (node == straightNode) straightCount + 1 else 1,
                            direction,
                            lowerBound,
                            upperBound,
                        )
                    }
            } else {
                node.neighbours.filter { it.value != parent.node && it.value != straightNode }
                    .map { (direction, node) ->
                        PathNode(
                            node,
                            g + node.heatLoss,
                            g + node.heatLoss + getFEstimate(node, goalNode),
                            this,
                            1,
                            direction,
                            lowerBound,
                            upperBound,
                        )
                    }
            }
        }
    }
}
