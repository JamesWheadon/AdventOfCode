package year2022.day24

import readInput
import java.lang.reflect.Field
import kotlin.math.abs

fun getNodesAndBlizzards(input: List<String>): Pair<List<Node>, List<Blizzard>> {
    val blizzards = mutableListOf<Blizzard>()
    val nodes = input.flatMapIndexed { row, s ->
        s.mapIndexed { column, c ->
            if (c != '#') {
                val node = Node(column, row)
                if (c != '.') {
                    blizzards.add(Blizzard(node, c))
                }
                node
            } else {
                null
            }
        }
    }.filterNotNull()
    connectNodes(nodes)
    return Pair(nodes, blizzards)
}

private fun connectNodes(nodes: List<Node>) {
    nodes.forEach { node ->
        val left = nodes.firstOrNull { it.x == node.x - 1 && it.y == node.y }
        if (left != null) {
            node.left = left
        }
        val up = nodes.firstOrNull { it.x == node.x && it.y == node.y - 1 }
        if (up != null) {
            node.up = up
        }
        val right = nodes.firstOrNull { it.x == node.x + 1 && it.y == node.y }
        if (right != null) {
            node.right = right
        }
        val down = nodes.firstOrNull { it.x == node.x && it.y == node.y + 1 }
        if (down != null) {
            node.down = down
        }
    }
}

fun part1(input: List<String>): Int {
    val (blizzards, start, goal) = initialiseGrid(input)
    val possibilities = mutableListOf(Expedition(start, goal, blizzards, 0, start.distanceTo(goal)))
    var current = possibilities.toList()
    current = searchUntilGoal(current, goal, possibilities)
    return current.filter { it.node == goal }.minOf { it.g }
}

fun part2(input: List<String>): Int {
    val (blizzards, start, goal) = initialiseGrid(input)
    var possibilities = mutableListOf(Expedition(start, goal, blizzards, 0, start.distanceTo(goal)))
    var current = possibilities.filter { possibility -> possibility.f == possibilities.minOf { it.f } }
    val pair = searchOneWay(current, goal, possibilities, start)
    current = pair.first
    possibilities = pair.second
    val pair1 = searchOneWay(current, start, possibilities, goal)
    current = pair1.first
    possibilities = pair1.second
    current = searchUntilGoal(current, goal, possibilities)
    return current.filter { it.node == goal }.minOf { it.g }
}

private fun searchOneWay(
    current: List<Expedition>,
    goal: Node,
    possibilities: MutableList<Expedition>,
    start: Node,
): Pair<List<Expedition>, MutableList<Expedition>> {
    var current1 = current
    var possibilities1 = possibilities
    current1 = searchUntilGoal(current1, goal, possibilities1)
    val fastestToGoal = current1.filter { it.node == goal }.minByOrNull { it.g }!!
    possibilities1 = mutableListOf(
        Expedition(
            goal,
            start,
            fastestToGoal.blizzards,
            fastestToGoal.g,
            fastestToGoal.g + goal.distanceTo(start),
        ),
    )
    current1 = possibilities1.filter { possibility -> possibility.f == possibilities1.minOf { it.f } }
    return Pair(current1, possibilities1)
}

private fun searchUntilGoal(
    current: List<Expedition>,
    goal: Node,
    possibilities: MutableList<Expedition>,
): List<Expedition> {
    var current1 = current
    while (!current1.map { it.node }.contains(goal)) {
        possibilities.removeAll(current1)
        val newPossibilities = current1.flatMap { it.newMoves() }.toMutableList()
        val toRemove = mutableListOf<Expedition>()
        newPossibilities.forEach { expedition ->
            if (!toRemove.contains(expedition)) {
                toRemove.addAll(newPossibilities.filter { it != expedition && it.node == expedition.node && it.g == expedition.g })
            }
            if (possibilities.any { it.node == expedition.node && it.g == expedition.g }) {
                toRemove.add(expedition)
            }
        }
        newPossibilities.removeAll(toRemove)
        possibilities.addAll(newPossibilities)
        val minF = possibilities.minOf { it.f }
        current1 = possibilities.filter { possibility -> possibility.f == minF }
    }
    return current1
}

private fun initialiseGrid(input: List<String>): Triple<Blizzards, Node, Node> {
    val gridInfo = getNodesAndBlizzards(input)
    val nodes = gridInfo.first
    val blizzards = Blizzards(gridInfo.second)
    val start = nodes.minByOrNull { it.y }!!
    val goal = nodes.maxByOrNull { it.y }!!
    println("start is $start, goal is $goal")
    return Triple(blizzards, start, goal)
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("src/main/kotlin/year2022/day24/Day24_test")
    check(part1(testInput) == 18)
    check(part2(testInput) == 54)

    val input = readInput("src/main/kotlin/year2022/day24/Day24")
    println(part1(input))
    println(part2(input))
}

data class Node(var x: Int, var y: Int) {
    fun distanceTo(goal: Node): Int {
        return abs(x - goal.x) + abs(y - goal.y)
    }

    fun startAgain(direction: Field?): Node {
        return if (this::class.java.declaredFields.first { it == direction }[this] == null) {
            this
        } else {
            val next = this::class.java.declaredFields.first { it == direction }[this] as Node
            next.startAgain(direction)
        }
    }

    var left: Node? = null
    var up: Node? = null
    var right: Node? = null
    var down: Node? = null
}

class Blizzards(initialBlizzards: List<Blizzard>) {
    val blizzardPositions = mutableMapOf(0 to initialBlizzards)
    val blizzardNodes = mutableMapOf(0 to initialBlizzards.map { it.node })

    fun addToBlizzardPositions() {
        val maxMinute = blizzardPositions.keys.maxOrNull()!!
        blizzardPositions[maxMinute + 1] = blizzardPositions[maxMinute]?.map { it.nextPosition() }!!
        blizzardNodes[maxMinute + 1] = blizzardPositions[maxMinute + 1]?.map { it.node }!!
    }
}

class Blizzard(val node: Node, private val direction: Char) {
    fun nextPosition(): Blizzard {
        var current: Node = node
        current = when (direction) {
            '<' -> {
                current.left ?: current.startAgain(current::class.java.declaredFields.first { it.name == "right" })
            }

            '^' -> {
                current.up ?: current.startAgain(current::class.java.declaredFields.first { it.name == "down" })
            }

            '>' -> {
                current.right ?: current.startAgain(current::class.java.declaredFields.first { it.name == "left" })
            }

            else -> {
                current.down ?: current.startAgain(current::class.java.declaredFields.first { it.name == "up" })
            }
        }
        return Blizzard(current, direction)
    }
}

class Expedition(
    var node: Node,
    private val goal: Node,
    val blizzards: Blizzards,
    val g: Int,
    val f: Int,
) {
    fun newMoves(): MutableList<Expedition> {
        val possible = mutableListOf<Expedition>()
        while (!blizzards.blizzardPositions.containsKey(g + 1)) {
            blizzards.addToBlizzardPositions()
        }
        val nextBlizzardNodes = blizzards.blizzardNodes[g + 1]?.filter { it.distanceTo(node) <= 1 }!!
        if (node.left != null) {
            addNewPossibility(nextBlizzardNodes, possible, node.left!!)
        }
        if (node.up != null) {
            addNewPossibility(nextBlizzardNodes, possible, node.up!!)
        }
        if (node.right != null) {
            addNewPossibility(nextBlizzardNodes, possible, node.right!!)
        }
        if (node.down != null) {
            addNewPossibility(nextBlizzardNodes, possible, node.down!!)
        }
        addNewPossibility(nextBlizzardNodes, possible, node)
        return possible
    }

    private fun addNewPossibility(
        nextBlizzardNodes: List<Node>,
        possible: MutableList<Expedition>,
        node: Node,
    ) {
        if (!nextBlizzardNodes.contains(node)) {
            possible.add(Expedition(node, goal, blizzards, g + 1, g + 1 + node.distanceTo(goal)))
        }
    }

    override fun toString(): String {
        return "Expedition(node=$node, g=$g, f=$f)"
    }
}
