package year2025.day08

import check
import readInput
import kotlin.math.abs

fun part1(input: List<String>, amount: Int): Int {
    val junctions = input.map { Junction(it.split(",")[0].toInt(), it.split(",")[1].toInt(), it.split(",")[2].toInt()) }
    val pairs = mutableListOf<Pair<Junction, Junction>>()
    for (i in junctions.indices) {
        for (junction in junctions.subList(i + 1, junctions.size)) {
            pairs.add(junctions[i] to junction)
        }
    }
    pairs.sortBy { abs(it.first.x - it.second.x).squared() + abs(it.first.y - it.second.y).squared() + abs(it.first.z - it.second.z).squared() }
    return pairs.asSequence().take(amount).fold(mutableListOf<MutableList<Junction>>()) { acc, pair ->
        val circuits = acc.filter { it.contains(pair.first) || it.contains(pair.second) }
        if (circuits.isEmpty()) {
            acc.add(mutableListOf(pair.first, pair.second))
        } else {
            acc.removeAll(circuits)
            acc.add(circuits.flatten().plus(listOf(pair.first, pair.second)).distinct().toMutableList())
        }
        acc
    }.plus(listOf(mutableListOf(junctions[0]), mutableListOf(junctions[0]))).sortedByDescending { it.size }.take(3)
        .fold(1) { acc, circuit -> acc * circuit.size }
}

fun part2(input: List<String>): Any {
    val junctions = input.map { Junction(it.split(",")[0].toInt(), it.split(",")[1].toInt(), it.split(",")[2].toInt()) }
    val pairs = mutableListOf<Pair<Junction, Junction>>()
    for (i in junctions.indices) {
        for (junction in junctions.subList(i + 1, junctions.size)) {
            pairs.add(junctions[i] to junction)
        }
    }
    pairs.sortBy { abs(it.first.x - it.second.x).squared() + abs(it.first.y - it.second.y).squared() + abs(it.first.z - it.second.z).squared() }
    pairs.fold(mutableListOf<MutableList<Junction>>()) { acc, pair ->
        val circuits = acc.filter { it.contains(pair.first) || it.contains(pair.second) }
        if (circuits.isEmpty()) {
            acc.add(mutableListOf(pair.first, pair.second))
        } else {
            acc.removeAll(circuits)
            acc.add(circuits.flatten().plus(listOf(pair.first, pair.second)).distinct().toMutableList())
        }
        if (acc.size == 1 && acc.first().size == junctions.size) {
            return pair.first.x.toLong() * pair.second.x.toLong()
        }
        acc
    }
    return 0
}

data class Junction(val x: Int, val y: Int, val z: Int)

private fun Int.squared(): Long {
    return this.toLong() * this.toLong()
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2025/day08/Day08_test")
    check(part1(testInput, 10), 40)
    check(part2(testInput), 25272L)

    val input = readInput("src/main/kotlin/year2025/day08/Day08")
    println(part1(input, 1000))
    println(part2(input))
}
