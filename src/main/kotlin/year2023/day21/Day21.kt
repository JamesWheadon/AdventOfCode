package year2023.day21

import readInput

fun part1(input: List<String>, steps: Int): Int {
    val nodes = input.mapIndexed { y, s ->
        s.mapIndexed { x, c ->
            when (c) {
                '.' -> {
                    Node(x, y, false)
                }

                'S' -> {
                    Node(x, y, true)
                }

                else -> {
                    null
                }
            }
        }
    }.flatten().filterNotNull()
    nodes.forEach { node ->
        node.neighbours = listOfNotNull(
            nodes.firstOrNull { it.x == node.x && it.y == node.y + 1 },
            nodes.firstOrNull { it.x == node.x && it.y == node.y - 1 },
            nodes.firstOrNull { it.x == node.x + 1 && it.y == node.y },
            nodes.firstOrNull { it.x == node.x - 1 && it.y == node.y },
        )
    }
    var nodesAtStep = nodes.filter { it.start }.toSet()
    for (i in 1..steps) {
        nodesAtStep = nodesAtStep.map { it.neighbours }.flatten().toSet()
    }
    return nodesAtStep.size
}

fun part2(input: List<String>): Long {
    val extra = input.map { it.replace("S", ".") }
    val grid = listOf(extra, extra, extra, extra, input, extra, extra, extra, extra).flatten().map { it.repeat(9) }
    val nodeGrid = grid.mapIndexed { y, s ->
        s.mapIndexed { x, c ->
            when (c) {
                '.' -> {
                    Node(x, y, false)
                }

                'S' -> {
                    Node(x, y, true)
                }

                else -> {
                    null
                }
            }
        }
    }
    nodeGrid.forEachIndexed { row, nodeRow ->
        nodeRow.forEachIndexed { column, node ->
            if (node != null) {
                val neighbours = mutableListOf<Node?>()
                if (row < nodeGrid.lastIndex) {
                    neighbours.add(nodeGrid[row + 1][column])
                }
                if (row > 0) {
                    neighbours.add(nodeGrid[row - 1][column])
                }
                if (column > 0) {
                    neighbours.add(nodeGrid[row][column - 1])
                }
                if (column < nodeRow.lastIndex) {
                    neighbours.add(nodeGrid[row][column + 1])
                }
                node.neighbours = neighbours.filterNotNull()
            }
        }
    }
    val nodes = nodeGrid.flatten().filterNotNull()
    val starts = nodes.filter { it.start }.sortedBy { it.x }
    var j = 0
    var k = starts.lastIndex
    while (j != k) {
        starts[j].start = false
        starts[k].start = false
        j++
        k--
    }
    var nodesAtStep = nodes.filter { it.start }.toSet()
    println(nodesAtStep)
    println(nodesAtStep.map { it.neighbours.size })
    var stepSizes = mutableListOf<Int>()
    val stepsOfInterest = listOf(65, 131 + 65, 131 + 131 + 65)
    for (i in 1..stepsOfInterest.max()) {
        nodesAtStep = nodesAtStep.map { it.neighbours }.flatten().toSet()
        if (i in stepsOfInterest) {
            println(nodesAtStep.size)
        }
        stepSizes.add(nodesAtStep.size)
    }
    println(stepSizes)
    return 0L
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2023/day21/Day21_test")
    check(part1(testInput, 6) == 16)

    val input = readInput("src/main/kotlin/year2023/day21/Day21")
//    println(part1(input, 64))
    println(part2(input))
    println(14529L * 202300L * 202300L + 14604 * 202300L + 3648L)

    //661363565714936 too high
    //515127916124540 too low
}

class Node(val x: Int, val y: Int, var start: Boolean) {
    var neighbours: List<Node> = listOf()
}
