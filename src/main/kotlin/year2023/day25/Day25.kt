package year2023.day25

import readInput

fun part1(input: List<String>): Int {
    val components = mutableListOf<Component>()
    val connections = mutableListOf<Connection>()
    input.forEach { row ->
        val root = row.split(": ")[0]
        val rowConnections = row.split(": ")[1].split(" ")
        val rootComponent = if (components.any { it.name == root }) {
            components.first { it.name == root }
        } else {
            val rC = Component(root)
            components.add(rC)
            rC
        }
        rowConnections.forEach { con ->
            val conComponent = if (components.any { it.name == con }) {
                components.first { it.name == con }
            } else {
                val cC = Component(con)
                components.add(cC)
                cC
            }
            val connection = Connection(rootComponent, conComponent)
            rootComponent.connections.add(connection)
            conComponent.connections.add(connection)
            connections.add(connection)
        }
    }
    println(components.size)
    while (true) {
        val routes = mutableListOf<List<Connection>>()
        for (i in 1..1000) {
            if (i % 500 == 0) {
                println(i)
            }
            val first = components.random()
            val second = components.filter { com -> com != first && !first.connections.map { if (it.first == first) it.second else it.first }.contains(com) }.random()
            routes.add(first.getRouteToComponent(second))
        }
        val mostCommon = routes.flatten().groupingBy { it }.eachCount().toMutableMap()
        val toRemove = mutableListOf<Connection>()
        while (toRemove.size < 3) {
            val next = mostCommon.maxBy { it.value }
            toRemove.add(next.key)
            mostCommon.remove(next.key)
        }
        val group = components.first().getGroup(toRemove[0], toRemove[1], toRemove[2])
        if (group.size != components.size) {
            return group.size * (components.size - group.size)
        }
    }
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2023/day25/Day25_test")
    check(part1(testInput) == 54)

    val input = readInput("src/main/kotlin/year2023/day25/Day25")
    println(part1(input))
}

class Component(val name: String) {
    val connections = mutableListOf<Connection>()

    fun getRouteToComponent(component: Component): List<Connection> {
        var routes = connections.map { mutableListOf(it) }
        while (routes.none { it.last().second == component || it.last().first == component }) {
            routes = routes.map { route ->
                if (route.size == 1) {
                    if (this == route.last().first) {
                        route.last().second.connections.filter { it != route.last() }.map { new ->
                            val newRoute = mutableListOf(new)
                            newRoute.addAll(0, route)
                            newRoute
                        }
                    } else {
                        route.last().first.connections.filter { it != route.last() }.map { new ->
                            val newRoute = mutableListOf(new)
                            newRoute.addAll(0, route)
                            newRoute
                        }
                    }
                } else {
                    val secondLast = route[route.lastIndex - 1]
                    if (secondLast.first == route.last().first || secondLast.second == route.last().first) {
                        route.last().second.connections.filter { it != route.last() }.map { new ->
                            val newRoute = mutableListOf(new)
                            newRoute.addAll(0, route)
                            newRoute
                        }
                    } else {
                        route.last().first.connections.filter { it != route.last() }.map { new ->
                            val newRoute = mutableListOf(new)
                            newRoute.addAll(0, route)
                            newRoute
                        }
                    }
                }
            }.flatten()
        }
        return routes.first { it.last().second == component || it.last().first == component }
    }

    fun getGroup(connection: Connection, connection1: Connection, connection2: Connection): List<Component> {
        var group = mutableListOf(this)
        while (group.any {
                it.getPossibleConnectionsNotInGroup(group, connection, connection1, connection2).isNotEmpty()
            }) {
            group.addAll(group.map { it.getPossibleConnectionsNotInGroup(group, connection, connection1, connection2) }.flatten().distinct())
            group = group.distinct().toMutableList()
        }
        return group
    }

    private fun getPossibleConnectionsNotInGroup(
        group: MutableList<Component>,
        connection: Connection,
        connection1: Connection,
        connection2: Connection
    ): List<Component> {
        return connections.filter { it != connection && it != connection1 && it != connection2 }.map { con ->
            if (this == con.first) {
                con.second
            } else {
                con.first
            }
        }.filter { !group.contains(it) }
    }

    override fun toString(): String {
        return "Component(name='$name')"
    }
}

data class Connection(val first: Component, val second: Component)
