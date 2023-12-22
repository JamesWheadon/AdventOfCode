package year2022.day12

import readInput

fun part1(input: List<String>): Int {
    var current = SearchNode(Node('a'), 0)
    var search = mutableListOf<SearchNode>()
    val searched = mutableListOf<Node>()
    val grid = mutableListOf<MutableList<Node>>()
    input.forEach { row -> grid.add(row.map { Node(it) }.toMutableList()) }
    grid.forEachIndexed { row, nodes ->
        nodes.forEachIndexed { column, node ->
            if (node.value == 'S') {
                current = SearchNode(node, 0)
            }
            extracted(node, row, grid, column)
        }
    }
    while (current.node.value != 'E') {
        search = searchNodes(current, search, searched)
        current = search.removeFirst()
    }
    return current.depth
}

fun part2(input: List<String>): Int {
    val starts = mutableListOf<SearchNode>()
    var search = mutableListOf<SearchNode>()
    var searched = mutableListOf<Node>()
    val grid = mutableListOf<MutableList<Node>>()
    input.forEach { row -> grid.add(row.map { Node(it) }.toMutableList()) }
    grid.forEachIndexed { row, nodes ->
        nodes.forEachIndexed { column, node ->
            if (node.value == 'S' || node.value == 'a') {
                starts.add(SearchNode(node, 0))
            }
            extracted(node, row, grid, column)
        }
    }
    val results = mutableListOf<Int>()
    starts.forEach { start ->
        var current = start
        var complete = true
        while (current.node.value != 'E') {
            search = searchNodes(current, search, searched)
            if (search.isEmpty()) {
                complete = false
                break
            }
            current = search.removeFirst()
        }
        if (complete) {
            results.add(current.depth)
        }
        search = mutableListOf()
        searched = mutableListOf()
    }
    return results.minOrNull()!!
}

private fun extracted(
    node: Node,
    row: Int,
    grid: MutableList<MutableList<Node>>,
    column: Int,
) {
    val nodeCode = when (node.value) {
        'S' -> {
            'a'.code
        }

        'E' -> {
            'z'.code
        }

        else -> {
            node.value.code
        }
    }
    if (row > 0) {
        val upCode = getCode(grid[row - 1][column])
        if (upCode <= nodeCode + 1) {
            node.connected.add(grid[row - 1][column])
        }
    }
    if (row < grid.size - 1) {
        val downCode = getCode(grid[row + 1][column])
        if (downCode <= nodeCode + 1) {
            node.connected.add(grid[row + 1][column])
        }
    }
    if (column > 0) {
        val leftCode = getCode(grid[row][column - 1])
        if (leftCode <= nodeCode + 1) {
            node.connected.add(grid[row][column - 1])
        }
    }
    if (column < grid[0].size - 1) {
        val rightCode = getCode(grid[row][column + 1])
        if (rightCode <= nodeCode + 1) {
            node.connected.add(grid[row][column + 1])
        }
    }
}

private fun getCode(
    node: Node,
) = when (node.value) {
    'S' -> {
        'a'.code
    }

    'E' -> {
        'z'.code
    }

    else -> {
        node.value.code
    }
}

private fun searchNodes(
    current: SearchNode,
    search: MutableList<SearchNode>,
    searched: MutableList<Node>,
): MutableList<SearchNode> {
    var search1 = search
    current.node.connected.forEach {
        search1.add(SearchNode(it, current.depth + 1))
    }
    searched.add(current.node)
    search1 = search1.filter { !searched.contains(it.node) }.toMutableList().sortedBy { it.depth }.toMutableList()
    return search1
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("src/main/kotlin/year2022/day12/Day12_test")
    check(part1(testInput) == 31)
    check(part2(testInput) == 29)

    val input = readInput("src/main/kotlin/year2022/day12/Day12")
    println(part1(input))
    println(part2(input))
}

class Node(val value: Char) {
    val connected = mutableListOf<Node>()
}

class SearchNode(val node: Node, val depth: Int)
