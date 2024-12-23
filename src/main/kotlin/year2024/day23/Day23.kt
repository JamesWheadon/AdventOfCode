package year2024.day23

import readInput

fun part1(input: List<String>): Int {
    val computers = mutableListOf<Computer>()
    input.forEach { connection ->
        val first: Computer = computers.firstOrNull { it.name == connection.split("-")[0] } ?: run {
            val element = Computer(connection.split("-")[0])
            computers.add(element)
            element
        }
        val second: Computer = computers.firstOrNull { it.name == connection.split("-")[1] } ?: run {
            val element = Computer(connection.split("-")[1])
            computers.add(element)
            element
        }
        first.connected.add(second)
        second.connected.add(first)
    }
    return computers.map { computer ->
        val games = mutableSetOf<Set<Computer>>()
        computer.connected.forEach { other ->
            other.connected.toSet().intersect(computer.connected).forEach { third ->
                games.add(setOf(computer, other, third))
            }
        }
        games
    }.flatten()
        .toSet()
        .count { it.size == 3 && it.any { computer -> computer.name.first() == 't' } }
}

fun part2(input: List<String>): String {
    val computers = mutableListOf<Computer>()
    input.forEach { connection ->
        val first: Computer = computers.firstOrNull { it.name == connection.split("-")[0] } ?: run {
            val element = Computer(connection.split("-")[0])
            computers.add(element)
            element
        }
        val second: Computer = computers.firstOrNull { it.name == connection.split("-")[1] } ?: run {
            val element = Computer(connection.split("-")[1])
            computers.add(element)
            element
        }
        first.connected.add(second)
        second.connected.add(first)
    }
    val seen = mutableSetOf<Set<Computer>>()
    var largest = mutableSetOf<Computer>()
    computers.forEach { computer ->
        computer.connected.forEach { other ->
            var games = listOf(mutableSetOf(computer, other))
            while (games.isNotEmpty()) {
                games = games.flatMap { game ->
                    if (game in seen) {
                        emptyList()
                    } else {
                        seen.add(game)
                        game.map { it.connected }.reduce { acc, computers ->
                            acc.intersect(computers).toMutableSet()
                        }.map {
                            val largerGame = game.toMutableSet()
                            largerGame.add(it)
                            if (largerGame.size > largest.size) {
                                largest = largerGame
                            }
                            largerGame
                        }
                    }
                }
            }
        }
    }
    return largest.map { it.name }.sorted().joinToString(",")
}

data class Computer(val name: String) {
    val connected = mutableSetOf<Computer>()
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2024/day23/Day23_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == "co,de,ka,ta")

    val input = readInput("src/main/kotlin/year2024/day23/Day23")
    println(part1(input))
    println(part2(input))
}
