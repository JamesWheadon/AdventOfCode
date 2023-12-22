package year2022.day22

import readInput
import kotlin.math.sqrt

fun getNodes(input: List<String>): List<Node> {
    val nodes = mutableListOf<Node>()
    input.forEachIndexed { row, s ->
        s.forEachIndexed { column, c ->
            if (c == '.') {
                nodes.add(Node(column, row, true))
            } else if (c == '#') {
                nodes.add(Node(column, row, false))
            }
        }
    }
    return nodes
}

fun connectFlatNodes(input: List<String>): List<Node> {
    val nodes = getNodes(input)
    nodes.filter { it.open }.forEach { node ->
        connectNodes(
            node,
            nodes,
            "horizontal",
            nodes.filter { it.y == node.y }.minBy { it.x },
            nodes.filter { it.y == node.y }.maxBy { it.x },
            node.x - 1,
            node.y,
        )
        connectNodes(
            node,
            nodes,
            "vertical",
            nodes.filter { it.x == node.x }.minBy { it.y },
            nodes.filter { it.x == node.x }.maxBy { it.y },
            node.x,
            node.y - 1,
        )
    }
    return nodes
}

private fun connectNodes(
    node: Node,
    nodes: List<Node>,
    direction: String,
    limit: Node,
    opposite: Node,
    adjacentX: Int,
    adjacentY: Int,
) {
    if (node == limit) {
        if (opposite.open) {
            if (direction == "vertical") {
                node.up = opposite
                opposite.down = node
            } else {
                node.left = opposite
                opposite.right = node
            }
        }
    } else {
        connectAdjacent(nodes, adjacentX, adjacentY, direction, node)
    }
}

fun boardLinesToNodes(input: List<String>): MutableList<MutableList<Node>> {
    val nodes = mutableListOf<MutableList<Node>>()
    input.forEachIndexed { row, s ->
        val rowNodes = mutableListOf<Node>()
        s.forEachIndexed { column, c ->
            if (c == '.') {
                rowNodes.add(Node(column, row, true))
            } else {
                rowNodes.add(Node(column, row, false))
            }
        }
        nodes.add(rowNodes)
    }
    return nodes
}

fun getFaceNodes(nodes: List<Node>): List<Node> {
    nodes.filter { it.open }.forEach { node ->
        connectFaceNodes(node, nodes, nodes.filter { it.y == node.y }.minBy { it.x }, node.x - 1, node.y, "horizontal")
        connectFaceNodes(node, nodes, nodes.filter { it.x == node.x }.minBy { it.y }, node.x, node.y - 1, "vertical")
    }
    return nodes
}

fun connectFaceNodes(node: Node, nodes: List<Node>, limit: Node, adjacentX: Int, adjacentY: Int, direction: String) {
    if (node != limit) {
        connectAdjacent(nodes, adjacentX, adjacentY, direction, node)
    }
}

private fun connectAdjacent(nodes: List<Node>, adjacentX: Int, adjacentY: Int, direction: String, node: Node) {
    val adjacent = nodes.first { it.x == adjacentX && it.y == adjacentY }
    if (adjacent.open) {
        if (direction == "vertical") {
            node.up = adjacent
            adjacent.down = node
        } else {
            node.left = adjacent
            adjacent.right = node
        }
    }
}

fun part1(input: List<String>): Int {
    val nodes = connectFlatNodes(input.subList(0, input.lastIndex - 1))
    val instructions = input.last().replace("R", ",R,").replace("L", ",L,").split(",").filter { it != "" }
    val me = Me(0, nodes.filter { node -> node.y == nodes.minOf { it.y } }.minByOrNull { it.x }!!)
    return followInstructions(instructions, me)
}

private fun followInstructions(instructions: List<String>, me: MeInterface): Int {
    instructions.forEach { instruction ->
        if (instruction == "R" || instruction == "L") {
            me.turn(instruction)
        } else {
            for (i in 0..<instruction.toInt()) {
                me.move()
            }
        }
    }
    return me.password()
}

fun getFaces(input: List<MutableList<Node>>, sideLength: Int): List<List<Node>> {
    val faceStrings = input.map { it.chunked(sideLength) }
    val faces = mutableListOf<List<Node>>()
    for (i in 0..<faceStrings[0].size) {
        val face = mutableListOf<MutableList<Node>>()
        faceStrings.forEach {
            face.add(it[i].toMutableList())
        }
        faces.add(face.flatten())
    }
    return faces.toList()
}

fun connectFaces(faces: List<Face>) {
    connectAdjacentFaces(faces)
    while (faces.any { it.notConnected() }) {
        faces.forEach { face ->
            connectLeft(face)
            connectRight(face)
            connectUp(face)
            connectDown(face)
        }
    }
}

private fun connectAdjacentFaces(faces: List<Face>) {
    faces.forEach { face ->
        faces.filter { it != face }.forEach { otherFace ->
            if (face.centre.first == otherFace.centre.first + sqrt(face.nodes.size.toDouble()) && face.centre.second == otherFace.centre.second) {
                face.left = Pair(otherFace, 0)
                otherFace.right = Pair(face, 0)
            } else if (face.centre.second == otherFace.centre.second + sqrt(face.nodes.size.toDouble()) && face.centre.first == otherFace.centre.first) {
                face.up = Pair(otherFace, 0)
                otherFace.down = Pair(face, 0)
            }
        }
    }
}

private fun connectLeft(face: Face) {
    if (face.left.first == null) {
        if (face.down.first != null) {
            face.left = connectFaces(face.down, listOf("left", "down", "right", "up"))
        }
        if (face.up.first != null && face.left.first == null) {
            face.left = connectFaces(face.up, listOf("left", "up", "right", "down"))
        }
    }
}

private fun connectRight(face: Face) {
    if (face.right.first == null) {
        if (face.down.first != null) {
            face.right = connectFaces(face.down, listOf("right", "down", "left", "up"))
        }
        if (face.up.first != null && face.right.first == null) {
            face.right = connectFaces(face.up, listOf("right", "up", "left", "down"))
        }
    }
}

private fun connectUp(face: Face) {
    if (face.up.first == null) {
        if (face.right.first != null) {
            face.up = connectFaces(face.right, listOf("up", "right", "down", "left"))
        }
        if (face.left.first != null && face.up.first == null) {
            face.up = connectFaces(face.left, listOf("up", "left", "down", "right"))
        }
    }
}

private fun connectDown(face: Face) {
    if (face.down.first == null) {
        if (face.right.first != null) {
            face.down = connectFaces(face.right, listOf("down", "right", "up", "left"))
        }
        if (face.left.first != null && face.down.first == null) {
            face.down = connectFaces(face.left, listOf("down", "left", "up", "right"))
        }
    }
}

fun connectFaces(current: Pair<Face?, Int>, directionOrder: List<String>): Pair<Face?, Int> {
    val direction = directionOrder[current.second]
    val adjacentFaceField = Face::class.java.declaredFields.first { it.name == direction }
    adjacentFaceField.trySetAccessible()
    val adjacentFacePair = adjacentFaceField[current.first!!] as Pair<*, *>
    val adjacentFace = adjacentFacePair.first as Face?
    return if (adjacentFace != null) {
        Pair(adjacentFace, current.second + 1)
    } else {
        Pair(null, 0)
    }
}

fun part2(input: List<String>, sideLength: Int): Int {
    val faces = boardLinesToNodes(input.subList(0, input.lastIndex - 1)).chunked(sideLength)
        .flatMap { getFaces(it, sideLength).map { face -> Face(getFaceNodes(face)) } }
        .filter { !it.nodes.none { node -> node.open } }
    connectFaces(faces)
    val instructions = input.last().replace("R", ",R,").replace("L", ",L,").split(",").filter { it != "" }
    val startFace = faces.filter { face -> face.centre.second == faces.minOf { it.centre.second } }
        .minByOrNull { it.centre.first }!!
    val startNode: Node =
        startFace.nodes.filter { node -> node.y == startFace.nodes.minOf { it.y } }.minByOrNull { it.x }!!
    val me = CubeMe(0, startFace, startNode)
    return followInstructions(instructions, me)
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("src/main/kotlin/year2022/day22/Day22_test")
    check(part1(testInput) == 6032)
    check(part2(testInput, 4) == 5031)

    val input = readInput("src/main/kotlin/year2022/day22/Day22")
    println(part1(input))
    println(part2(input, 50))
}

class CubeMe(private var facing: Int, private var face: Face, private var node: Node) : MeInterface {
    override fun turn(instruction: String) {
        facing = MeUtil.turn(instruction, facing)
    }

    override fun move() {
        when (facing) {
            0 -> {
                move(node.right, node.x, face.nodes.maxOf { it.x }, face.right, node.y - face.nodes.minOf { it.y })
            }

            1 -> {
                move(node.down, node.y, face.nodes.maxOf { it.y }, face.down, node.x - face.nodes.minOf { it.x })
            }

            2 -> {
                move(node.left, node.x, face.nodes.minOf { it.x }, face.left, node.y - face.nodes.minOf { it.y })
            }

            else -> {
                move(node.up, node.y, face.nodes.minOf { it.y }, face.up, node.x - face.nodes.minOf { it.x })
            }
        }
    }

    private fun move(nextNode: Node?, coord: Int, limit: Int, nextFace: Pair<Face?, Int>, distance: Int) {
        if (nextNode != null) {
            node = nextNode
        } else if (coord == limit) {
            moveFace(nextFace, distance)
        }
    }

    private fun moveFace(nextFace: Pair<Face?, Int>, distance: Int) {
        val increase = if (nextFace.second == 0) {
            distance
        } else if ((face.up == nextFace && nextFace.first!!.left.first == face) ||
            (face.left == nextFace && nextFace.first!!.up.first == face) ||
            (face.down == nextFace && nextFace.first!!.right.first == face) ||
            (face.right == nextFace && nextFace.first!!.down.first == face)
        ) {
            distance
        } else {
            sqrt(nextFace.first!!.nodes.size.toDouble()).toInt() - distance - 1
        }
        moveToFace(nextFace, increase)
    }

    private fun moveToFace(nextFace: Pair<Face?, Int>, increase: Int) {
        when (face) {
            nextFace.first!!.up.first!! -> {
                val candidate = nextFace.first!!.nodes.first { node ->
                    node.y == nextFace.first!!.nodes.minOf { it.y } &&
                        node.x == nextFace.first!!.nodes.minOf { it.x } + increase
                }
                if (candidate.open) {
                    node = candidate
                    face = nextFace.first!!
                    facing = 1
                }
            }

            nextFace.first!!.right.first!! -> {
                val candidate = nextFace.first!!.nodes.first { node ->
                    node.x == nextFace.first!!.nodes.maxOf { it.x } &&
                        node.y == nextFace.first!!.nodes.minOf { it.y } + increase
                }
                if (candidate.open) {
                    node = candidate
                    face = nextFace.first!!
                    facing = 2
                }
            }

            nextFace.first!!.down.first!! -> {
                val candidate = nextFace.first!!.nodes.first { node ->
                    node.y == nextFace.first!!.nodes.maxOf { it.y } &&
                        node.x == nextFace.first!!.nodes.minOf { it.x } + increase
                }
                if (candidate.open) {
                    node = candidate
                    face = nextFace.first!!
                    facing = 3
                }
            }

            else -> {
                val candidate = nextFace.first!!.nodes.first { node ->
                    node.x == nextFace.first!!.nodes.minOf { it.x } &&
                        node.y == nextFace.first!!.nodes.minOf { it.y } + increase
                }
                if (candidate.open) {
                    node = candidate
                    face = nextFace.first!!
                    facing = 0
                }
            }
        }
    }

    override fun password(): Int {
        return 1000 * (node.y + 1) + 4 * (node.x + 1) + facing
    }

    override fun toString(): String {
        return "Me(facing=$facing, node=$node, face=$face)"
    }
}

class Me(private var facing: Int, private var node: Node) : MeInterface {
    override fun turn(instruction: String) {
        facing = MeUtil.turn(instruction, facing)
    }

    override fun move() {
        when (facing) {
            0 -> {
                if (node.right != null) {
                    node = node.right!!
                }
            }

            1 -> {
                if (node.down != null) {
                    node = node.down!!
                }
            }

            2 -> {
                if (node.left != null) {
                    node = node.left!!
                }
            }

            else -> {
                if (node.up != null) {
                    node = node.up!!
                }
            }
        }
    }

    override fun password(): Int {
        return 1000 * (node.y + 1) + 4 * (node.x + 1) + facing
    }

    override fun toString(): String {
        return "Me(facing=$facing, node=$node)"
    }
}

class Node(val x: Int, val y: Int, val open: Boolean) {
    var left: Node? = null
    var right: Node? = null
    var up: Node? = null
    var down: Node? = null
    override fun toString(): String {
        return "Node(x=$x, y=$y)"
    }
}

class Face(val nodes: List<Node>) {
    var left: Pair<Face?, Int> = Pair(null, 0)
    var right: Pair<Face?, Int> = Pair(null, 0)
    var up: Pair<Face?, Int> = Pair(null, 0)
    var down: Pair<Face?, Int> = Pair(null, 0)
    var centre = Pair(nodes.map { it.x }.average(), nodes.map { it.y }.average())

    fun notConnected(): Boolean {
        return up.first == null || right.first == null || down.first == null || left.first == null
    }

    override fun toString(): String {
        return "Face(left=${left.first?.centreToString()}, " +
            "right=${right.first?.centreToString()}, " +
            "up=${up.first?.centreToString()}, " +
            "down=${down.first?.centreToString()}, " +
            "center=$centre)"
    }

    private fun centreToString(): String {
        return "$centre"
    }
}

class MeUtil {
    companion object {
        fun turn(instruction: String, facing: Int): Int {
            var newFacing = facing
            if (instruction == "R") {
                newFacing++
                if (newFacing == 4) {
                    newFacing = 0
                }
            } else {
                newFacing--
                if (newFacing == -1) {
                    newFacing = 3
                }
            }
            return newFacing
        }
    }
}

interface MeInterface {

    fun move()

    fun turn(instruction: String)

    fun password(): Int
}
