package year2025.day10

import check
import readInput

fun part1(input: List<String>): Int {
    return input.sumOf { machine ->
        val goal = machine.split(" ").first().filter { it == '#' || it == '.' }.map { if (it == '#') 1 else 0 }
        val buttons = machine.split(" ")
            .drop(1)
            .dropLast(1)
            .map { button -> Button(button.filter { it.isDigit() }.map { it.digitToInt() }) }
        getPresses(
            buttons,
            goal
        )
    }
}

private fun getPresses(
    buttons: List<Button>,
    goal: List<Int>
): Int {
    var currentState = setOf(emptySet<Button>())
    var presses = 0
    while (currentState.none { state ->
            goal.mapIndexed { index, c ->
                state.filter { it.indexes.contains(index) }.size % 2 == c % 2
            }.fold(true) { acc, b -> acc && b }
        }) {
        currentState = currentState + currentState.flatMap { state ->
            buttons.filter { !state.contains(it) }.map {
                state.plus(it)
            }
        }.toSet()
        presses++
    }
    return presses
}

private fun allPossibilities(buttons: List<Button>): Set<Set<Button>> {
    val possible = buttons.map { setOf(it) }.toMutableSet()
    do {
        val largest = possible.maxOf { it.size }
        val largestSets = possible.filter { it.size == largest }
        possible.addAll(largestSets.flatMap { state ->
            buttons.filter { !state.contains(it) }.map {
                state.plus(it)
            }
        }.distinct())
    } while (possible.none { it.size == buttons.size })
    return possible
}

fun part2(input: List<String>): Int {
    return input.sumOf { machine ->
        val goal = machine.split(" ").last().removePrefix("{").removeSuffix("}").split(",").map { it.toInt() }
        val buttons = machine.split(" ")
            .drop(1)
            .dropLast(1)
            .map { button -> Button(button.filter { it.isDigit() }.map { it.digitToInt() }) }
        getSolution(goal, buttons, mutableMapOf(), allPossibilities(buttons))
    }
}

fun getSolution(
    goal: List<Int>,
    buttons: List<Button>,
    knownScores: MutableMap<List<Int>, Int?>,
    allPossibilities: Set<Set<Button>>
): Int {
    if (goal in knownScores.keys) {
        return knownScores[goal] ?: 10000
    }
    return getPresses(buttons, allPossibilities, goal)
        .mapNotNull { presses ->
            var nextGoal = goal.mapIndexed { index, buttonGoal ->
                buttonGoal - presses.filter { it.indexes.contains(index) }.size
            }
            if (nextGoal.any { it < 0 }) {
                null
            } else if (nextGoal.all { it == 0 }) {
                presses.size
            } else {
                var speedUp = 1
                if (nextGoal.all { it % 2 == 0 }) {
                    nextGoal = nextGoal.map { it / 2 }
                    speedUp *= 2
                }
                speedUp * getSolution(nextGoal, buttons, knownScores, allPossibilities) + presses.size
            }
        }.minOrNull().also { knownScores[goal] = it } ?: 10000
}

private fun getPresses(
    buttons: List<Button>,
    allPossibilities: Set<Set<Button>>,
    goal: List<Int>
): List<Set<Button>> {
    if (goal.all { it % 2 == 0 }) {
        return buttons.map { setOf(it, it) }
    }
    return allPossibilities.filter { state ->
        goal.mapIndexed { index, c ->
            state.filter { it.indexes.contains(index) }.size % 2 == c % 2
        }.reduce { acc, b -> acc && b }
    }
}

data class Button(val indexes: List<Int>)

fun main() {
    val testInput = readInput("src/main/kotlin/year2025/day10/Day10_test")
    check(part1(testInput), 7)
    check(part2(testInput), 33)

    val input = readInput("src/main/kotlin/year2025/day10/Day10")
    println(part1(input))
    println(part2(input))
}
