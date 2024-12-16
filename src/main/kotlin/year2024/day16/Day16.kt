package year2024.day16

import readInput
import java.util.*

fun part1(input: List<String>): Int {
    val (start, end) = getNodes(input)
    val pathStart = PathNode(start, '>')
    val visited = mutableSetOf<PathNode>()
    val queue = PriorityQueue { t1: Path, t2: Path -> t1.cost - t2.cost }
    queue.add(Path(pathStart, 0))
    while (true) {
        var current = queue.poll()
        while (visited.contains(current.pathNode)) {
            current = queue.poll()
        }
        if (current.pathNode.node == end) {
            return current.cost
        }
        getNextMoves(current, queue)
        visited.add(current.pathNode)
    }
}

fun part2(input: List<String>): Int {
    val (start, end) = getNodes(input)
    val pathStart = PathNode(start, '>')
    val visited = mutableMapOf<PathNode, Int>()
    val queue = PriorityQueue { t1: Path, t2: Path -> t1.cost - t2.cost }
    queue.add(Path(pathStart, 0))
    val ends = mutableListOf<Path>()
    while (true) {
        var current = queue.poll()
        while (visited.contains(current.pathNode) && visited[current.pathNode]!! < current.cost) {
            current = queue.poll()
        }
        if (current.pathNode.node == end) {
            ends.add(current)
        }
        if (ends.isNotEmpty() && current.cost > ends.first().cost) {
            break
        }
        getNextMoves(current, queue)
        visited[current.pathNode] = current.cost
    }
    val places = mutableSetOf<Node>()
    ends.forEach { path ->
        var current: Path? = path
        while (current != null) {
            places.add(current!!.pathNode.node)
            current = current!!.parent
        }
    }
    return places.size
}

private fun getNodes(input: List<String>): Pair<Node, Node> {
    lateinit var start: Node
    lateinit var end: Node
    val nodes = input.flatMapIndexed { row, s ->
        s.mapIndexed { col, c ->
            if (c != '#') {
                val node = Node(col, row)
                if (c == 'S') {
                    start = node
                } else if (c == 'E') {
                    end = node
                }
                node
            } else {
                null
            }
        }
    }.filterNotNull()
    nodes.forEach { node ->
        nodes.firstOrNull { other -> other.y == node.y && other.x == node.x + 1 }?.let { node.connections['>'] = it }
        nodes.firstOrNull { other -> other.y == node.y && other.x == node.x - 1 }?.let { node.connections['<'] = it }
        nodes.firstOrNull { other -> other.y == node.y - 1 && other.x == node.x }?.let { node.connections['^'] = it }
        nodes.firstOrNull { other -> other.y == node.y + 1 && other.x == node.x }?.let { node.connections['v'] = it }
    }
    return Pair(start, end)
}

private fun getNextMoves(current: Path, queue: PriorityQueue<Path>) {
    when (current.pathNode.direction) {
        '>' -> {
            current.pathNode.node.connections['>']?.let {
                queue.add(
                    Path(
                        PathNode(it, '>'),
                        current.cost + 1,
                        current
                    )
                )
            }
            queue.add(Path(PathNode(current.pathNode.node, 'v'), current.cost + 1000, current))
            queue.add(Path(PathNode(current.pathNode.node, '^'), current.cost + 1000, current))
        }

        '<' -> {
            current.pathNode.node.connections['<']?.let {
                queue.add(
                    Path(
                        PathNode(it, '<'),
                        current.cost + 1,
                        current
                    )
                )
            }
            queue.add(Path(PathNode(current.pathNode.node, 'v'), current.cost + 1000, current))
            queue.add(Path(PathNode(current.pathNode.node, '^'), current.cost + 1000, current))
        }

        '^' -> {
            current.pathNode.node.connections['^']?.let {
                queue.add(
                    Path(
                        PathNode(it, '^'),
                        current.cost + 1,
                        current
                    )
                )
            }
            queue.add(Path(PathNode(current.pathNode.node, '>'), current.cost + 1000, current))
            queue.add(Path(PathNode(current.pathNode.node, '<'), current.cost + 1000, current))
        }

        'v' -> {
            current.pathNode.node.connections['v']?.let {
                queue.add(
                    Path(
                        PathNode(it, 'v'),
                        current.cost + 1,
                        current
                    )
                )
            }
            queue.add(Path(PathNode(current.pathNode.node, '>'), current.cost + 1000, current))
            queue.add(Path(PathNode(current.pathNode.node, '<'), current.cost + 1000, current))
        }
    }
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2024/day16/Day16_test")
    check(part1(testInput) == 7036)
    check(part2(testInput) == 45)

    val input = readInput("src/main/kotlin/year2024/day16/Day16")
    println(part1(input))
    println(part2(input))
}

data class Node(val x: Int, val y: Int) {
    val connections = mutableMapOf<Char, Node>()
}

data class PathNode(val node: Node, val direction: Char)
data class Path(val pathNode: PathNode, val cost: Int, val parent: Path? = null)
