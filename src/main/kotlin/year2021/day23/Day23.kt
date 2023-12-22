package year2021.day23

import readInput
import kotlin.math.absoluteValue

fun part1(input: List<String>): Int {
    val nodes = mutableListOf<Node>()
    val agents = mutableListOf<Agent>()
    input.forEachIndexed { y, s ->
        s.forEachIndexed { x, c ->
            if (y <= 2) {
                createNodes(c, x, nodes, y, agents)
            } else if (y > 4) {
                createNodes(c, x, nodes, 3, agents)
            }
        }
    }
    connectNodesAndCheckDone(nodes, agents)
    val map = Map(agents.sortedBy { it.type }.toMutableList())
    return map.minimumCost()
}

fun part2(input: List<String>): Int {
    val nodes = mutableListOf<Node>()
    val agents = mutableListOf<Agent>()
    input.forEachIndexed { y, s ->
        s.forEachIndexed { x, c ->
            createNodes(c, x, nodes, y, agents)
        }
    }
    connectNodesAndCheckDone(nodes, agents)
    val map = Map(agents)
    return map.minimumCost()
}

private fun createNodes(
    c: Char,
    x: Int,
    nodes: MutableList<Node>,
    y: Int,
    agents: MutableList<Agent>,
) {
    if (c == '.') {
        if (x == 3 || x == 5 || x == 7 || x == 9) {
            nodes.add(Node(y, x, null, false))
        } else {
            nodes.add(Node(y, x, null, true))
        }
    } else if (c == 'A' || c == 'B' || c == 'C' || c == 'D') {
        val node: Node = when (x) {
            3 -> {
                Node(y, x, 'A', true)
            }
            5 -> {
                Node(y, x, 'B', true)
            }
            7 -> {
                Node(y, x, 'C', true)
            }
            else -> {
                Node(y, x, 'D', true)
            }
        }
        nodes.add(node)
        agents.add(Agent(node, c))
    }
}

private fun connectNodesAndCheckDone(
    nodes: MutableList<Node>,
    agents: MutableList<Agent>,
) {
    nodes.forEach { node ->
        nodes.filter {
            it.x == node.x && (it.y - node.y).absoluteValue == 1 || it.y == node.y && (it.x - node.x).absoluteValue == 1
        }.forEach {
            node.connectedTo.add(it)
        }
    }
    agents.forEach { agent ->
        if (agent.position.roomType == agent.type) {
            agent.done =
                agents.none { it.position.x == agent.position.x && it.type != agent.type && it.position.y > agent.position.y }
        }
    }
}

fun main() {
    val testInput1 = readInput("src/main/kotlin/year2021/day23/Day23_test")
    check(part1(testInput1) == 12521)
    check(part2(testInput1) == 44169)

    val input = readInput("src/main/kotlin/year2021/day23/Day23")
    println(part1(input))
    println(part2(input))
}
