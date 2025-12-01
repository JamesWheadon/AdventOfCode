package year2020.day16

import check
import readInput

fun part1(input: List<String>): Int {
    val pattern = "[a-z]+: ([0-9]+-[0-9]+) or ([0-9]+-[0-9]+)".toRegex()
    val ranges = input
        .takeWhile { it.isNotEmpty() }
        .mapNotNull { field ->
            pattern.find(field)?.groups?.drop(1)?.mapNotNull { group ->
                group?.value?.split("-")?.let { it[0].toInt()..it[1].toInt() }
            }
        }
    return input
        .takeLastWhile { it != "nearby tickets:" }
        .flatMap { it.split(",") }
        .map { it.toInt() }
        .filter { ticket ->
            ranges.all { fieldRanges ->
                fieldRanges.none { it.contains(ticket) }
            }
        }.sum()
}

fun part2(input: List<String>): Long {
    val pattern = "([a-z ]+): ([0-9]+-[0-9]+) or ([0-9]+-[0-9]+)".toRegex()
    val fields = input
        .takeWhile { it.isNotEmpty() }
        .mapNotNull { field ->
            val groups = pattern.find(field)?.groups
            groups?.drop(2)?.mapNotNull { group ->
                group?.value?.split("-")?.let { it[0].toInt()..it[1].toInt() }
            }?.let {
                groups[1]?.let { nameGroup -> Field(nameGroup.value, it) }
            }
        }
    fields.forEach { it.possibleIndices = fields.indices.toList() }
    val validTickets = input
        .takeLastWhile { it != "nearby tickets:" }
        .map { ticket -> ticket.split(",").map { it.toInt() } }
        .filter { ticket ->
            ticket.all { ticketField ->
                fields.any { field ->
                    field.ranges.any { it.contains(ticketField) }
                }
            }
        }
    validTickets.forEach { ticket ->
        fields.forEach { field ->
            field.possibleIndices = field.possibleIndices.filter { field.ranges.any { range -> range.contains(ticket[it]) } }
        }
    }
    while (fields.any { it.possibleIndices.size > 1 }) {
        val assignedIndices = fields.filter { it.possibleIndices.size == 1 }.map { it.possibleIndices.first() }
        fields.filter { it.possibleIndices.size > 1 }.forEach { it.possibleIndices = it.possibleIndices.minus(assignedIndices.toSet()) }
    }
    val ticket = input.dropWhile { it != "your ticket:" }[1].split(",").map { it.toLong() }
    return fields.filter { it.name.startsWith("departure") }.map { ticket[it.possibleIndices.first()] }.reduce { acc, i -> acc * i }
}

data class Field(val name: String, val ranges: List<IntRange>, var possibleIndices: List<Int> = listOf())

fun main() {
    val testInput = readInput("src/main/kotlin/year2020/day16/Day16_test")
    check(part1(testInput), 71)

    val input = readInput("src/main/kotlin/year2020/day16/Day16")
    println(part1(input))
    println(part2(input))
}
