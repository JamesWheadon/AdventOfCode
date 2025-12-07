package year2025.day07

import check
import readInput

fun part1(input: List<String>): Int {
    var splits = 0
    var beams = listOf<Beam>()
    for (i in input.indices) {
        val newBeams = input[i].mapIndexed { index, c ->
            if (c == 'S') {
                Beam(index)
            } else {
                null
            }
        }.filterNotNull()
        val splitLocations = input[i].mapIndexed { index, c ->
            if (c == '^' && beams.any { it.x == index }) {
                index
            } else {
                null
            }
        }.filterNotNull()
        splits += splitLocations.size
        beams = newBeams + beams.filter { !splitLocations.contains(it.x) } + splitLocations.flatMap { listOf(Beam(it - 1), Beam(it + 1)) }
        beams = beams.distinct()
    }
    return splits
}

fun part2(input: List<String>): Long {
    var beams = listOf<Beam>()
    for (i in input.indices) {
        val newBeams = input[i].mapIndexed { index, c ->
            if (c == 'S') {
                Beam(index)
            } else {
                null
            }
        }.filterNotNull()
        val splitBeams = mutableListOf<Beam>()
        val splitLocations = input[i].mapIndexed { index, c ->
            if (c == '^') {
                beams.firstOrNull { it.x == index }?.let {
                    splitBeams.add(it)
                    listOf(Beam(index - 1, it.count), Beam(index + 1, it.count))
                } ?: listOf(null)
            } else {
                listOf(null)
            }
        }.flatten().filterNotNull()
        beams = beams + newBeams - splitBeams.toSet() + splitLocations
        beams = beams.fold(mutableListOf()) { acc, beam ->
            acc.firstOrNull { it.x == beam.x }?.let { it.count += beam.count } ?: acc.add(beam)
            acc
        }
    }
    return beams.sumOf { it.count }
}

data class Beam(val x: Int, var count: Long = 1)

fun main() {
    val testInput = readInput("src/main/kotlin/year2025/day07/Day07_test")
    check(part1(testInput), 21)
    check(part2(testInput), 40)

    val input = readInput("src/main/kotlin/year2025/day07/Day07")
    println(part1(input))
    println(part2(input))
}
