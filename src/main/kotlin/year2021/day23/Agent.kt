package year2021.day23

import kotlin.math.absoluteValue

data class Agent(var position: Node, val type: Char) {

    var done = false
    val moveCost = when (type) {
        'A' -> {
            1
        }

        'B' -> {
            10
        }

        'C' -> {
            100
        }

        else -> {
            1000
        }
    }

    fun costEstimate(): Int {
        var iteration = 0
        var nearNodes = mutableListOf(position)
        var nextGen = mutableListOf<Node>()
        while (nearNodes.none { it.roomType == type }) {
            nearNodes.forEach { nextGen.addAll(it.connectedTo) }
            nearNodes = nextGen
            nextGen = mutableListOf()
            iteration += 1
        }
        return iteration * moveCost
    }

    override fun toString(): String {
        return "Agent(position=$position, type=$type)"
    }

    fun costToNode(node: Node): Int {
        var distanceToNode = (node.x - position.x).absoluteValue
        distanceToNode += if (node.y == 1) {
            position.y - 1
        } else if (node.x == position.x) {
            node.y - position.x
        } else {
            (node.y - 1 + position.y - 1)
        }
        return distanceToNode * moveCost
    }
}
