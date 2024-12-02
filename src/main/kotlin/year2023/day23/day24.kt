package year2023.day23

import readInput

fun part1(input: List<String>): Int {
    val nodes = input.mapIndexed { row, s ->
        s.mapIndexed { column, c ->
            Node(column, row, c)
        }
    }.flatten().filter { it.symbol != '#' }
    nodes.forEach { node ->
        val adjacent = listOfNotNull(
            nodes.firstOrNull { it.x == node.x && it.y == node.y - 1 },
            nodes.firstOrNull { it.x == node.x && it.y == node.y + 1 },
            nodes.firstOrNull { it.x == node.x - 1 && it.y == node.y },
            nodes.firstOrNull { it.x == node.x + 1 && it.y == node.y }
        )
        when (node.symbol) {
            '.' -> {
                node.neighbours = adjacent
            }
            '>' -> {
                node.neighbours = listOfNotNull(
                    nodes.firstOrNull { it.x == node.x + 1 && it.y == node.y }
                )
            }
            '<' -> {
                node.neighbours = listOfNotNull(
                    nodes.firstOrNull { it.x == node.x - 1 && it.y == node.y }
                )
            }
            '^' -> {
                node.neighbours = listOfNotNull(
                    nodes.firstOrNull { it.x == node.x && it.y == node.y - 1 }
                )
            }
            else -> {
                node.neighbours = listOfNotNull(
                    nodes.firstOrNull { it.x == node.x && it.y == node.y + 1 }
                )
            }
        }
    }
    val start = nodes.first { it.y == 0 }
    val end = nodes.first { it.y == input.lastIndex }
    var routes = listOf(listOf(start))
    while (routes.any { !endReached(it) }) {
        routes = routes.map { route ->
            if (route.last().neighbours.size == 1) {
                val neighbour = route.last().neighbours.first()
                if (route.size == 1 || route[route.lastIndex - 1] != neighbour ) {
                    val newRoute = mutableListOf(neighbour)
                    newRoute.addAll(0, route)
                    listOf(newRoute)
                } else {
                    listOf(route)
                }
            } else {
                     route.last().neighbours.filter { it != route[route.lastIndex - 1] }.map {
                        val newRoute = mutableListOf(it)
                        newRoute.addAll(0, route)
                        newRoute
                    }
            }
        }.flatten()
    }
    return routes.filter { it.last() == end }.maxOf { it.size } - 1
}

fun endReached(route: List<Node>): Boolean {
    return route.last().neighbours.all { route.contains(it) }
}

fun part2(input: List<String>): Int {
    val nodes = input.mapIndexed { row, s ->
        s.mapIndexed { column, c ->
            Node(column, row, c)
        }
    }.flatten().filter { it.symbol != '#' }
    nodes.forEach { node ->
        val adjacent = listOfNotNull(
            nodes.firstOrNull { it.x == node.x && it.y == node.y - 1 },
            nodes.firstOrNull { it.x == node.x && it.y == node.y + 1 },
            nodes.firstOrNull { it.x == node.x - 1 && it.y == node.y },
            nodes.firstOrNull { it.x == node.x + 1 && it.y == node.y }
        )
        node.neighbours = adjacent
    }
    val start = nodes.first { it.y == 0 }
    val end = nodes.first { it.y == input.lastIndex }
    val junctions = nodes.filter { it.neighbours.size > 2 }.map { Junction(it) }
    junctions.forEach { it.getNearestJunctions(junctions) }
    junctions.forEach { println("$it, ${it.shortestToAdjacent}") }
    val startJunction = junctions.first { it.startDistance != 0 }
    var routes = listOf(listOf(startJunction))
    while (routes.any { !it.last().isRouteEnd(it) }) {
        routes = routes.map { route ->
            if (!route.last().isRouteEnd(route)) {
                route.last().shortestToAdjacent.filter { !route.contains(it.key) }.map {
                    val newRoute = mutableListOf(it.key)
                    newRoute.addAll(0, route)
                    newRoute
                }
            } else {
                listOf(route)
            }
        }.flatten()
    }
    val endJunction = junctions.first { it.endDistance != 0 }
    val endRoutes = routes.filter { it.last() == endJunction }
    return endRoutes.maxOf { route ->
        startJunction.startDistance + endJunction.endDistance + route.windowed(2)
            .sumOf { it[0].shortestToAdjacent[it[1]]!! } + route.size
    }
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2023/day23/Day23_test")
    check(part1(testInput) == 94)
    check(part2(testInput) == 154)

    val input = readInput("src/main/kotlin/year2023/day23/Day23")
    println(part1(input))
    println(part2(input))
}

class Node(val x: Int, val y: Int, val symbol: Char) {
    var neighbours = listOf<Node>()

    override fun toString(): String {
        return "Node(x=$x, y=$y, symbol=$symbol, neighbours=${neighbours.size})"
    }
}

class Junction(val node: Node) {
    val shortestToAdjacent: MutableMap<Junction, Int> = mutableMapOf()
    var startDistance = 0
    var endDistance = 0

    fun getNearestJunctions(junctions: List<Junction>) {
        node.neighbours.forEach { neighbour ->
            val route = mutableListOf(node, neighbour)
            while (route.last().neighbours.size == 2) {
                route.add(route.last().neighbours.first { !route.contains(it) })
            }
            if (route.last().neighbours.size == 1) {
                if (route.last().y == 0) {
                    startDistance = route.size - 2
                } else {
                    endDistance = route.size - 1
                }
            } else {
                shortestToAdjacent[junctions.first { it.node == route.last() }] = route.size - 2
            }
        }
    }

    override fun toString(): String {
        return "Junction(node=$node)"
    }

    fun isRouteEnd(route: List<Junction>): Boolean {
        return route.containsAll(shortestToAdjacent.keys)
    }
}
