package year2025.day11

import check
import readInput

fun part1(input: List<String>): Int {
    val outNode = Node("out")
    outNode.next.add(outNode)
    val nodes = input.map { Node(it.split(": ")[0]) } + outNode
    input.forEach { line ->
        val node = nodes.first { it.name == line.split(": ")[0] }
        node.next.addAll(nodes.filter { it.name in line.split(": ")[1].split(" ") })
    }
    var paths = nodes.filter { it.name == "you" }
    while (paths.any { it.name != "out" }) {
        paths = paths.flatMap { it.next }
    }
    return paths.size
}

fun part2(input: List<String>): Long {
    val outNode = Node("out")
    outNode.next.add(outNode)
    val nodes = input.map { Node(it.split(": ")[0]) } + outNode
    input.forEach { line ->
        val node = nodes.first { it.name == line.split(": ")[0] }
        node.next.addAll(nodes.filter { it.name in line.split(": ")[1].split(" ") })
    }
    var paths = listOf(Path(nodes.first { it.name == "svr" }, 1, false, false))
    while (paths.any { it.node.name != "out" }) {
        val (done, going) = paths.partition { it.node.name == "out" }
        paths = done + going.flatMap {
            it.node.next.map { node ->
                it.copy(
                    node = node,
                    throughDac = it.throughDac || node.name == "dac",
                    throughFft = it.throughFft || node.name == "fft"
                )
            }
        }
        paths = paths.fold(mutableListOf()) { acc, path ->
            acc.firstOrNull { it.node == path.node && it.throughDac == path.throughDac && it.throughFft == path.throughFft }?.let { it.amount += path.amount } ?: run { acc.add(path) }
            acc
        }
    }
    return paths.filter { it.throughDac && it.throughFft }.sumOf { it.amount }
}

data class Node(val name: String) {
    val next = mutableListOf<Node>()
}

data class Path(val node: Node, var amount: Long, val throughDac: Boolean, val throughFft: Boolean) {
}

fun main() {
    check(part1(readInput("src/main/kotlin/year2025/day11/Day11_test1")), 5)
    check(part2(readInput("src/main/kotlin/year2025/day11/Day11_test2")), 2)

    val input = readInput("src/main/kotlin/year2025/day11/Day11")
    println(part1(input))
    println(part2(input))
}
