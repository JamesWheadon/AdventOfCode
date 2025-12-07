package year2025.day05

import check
import readInput
import kotlin.math.max
import kotlin.math.min

fun part1(input: List<String>): Int {
    val ranges = input.takeWhile { it.isNotEmpty() }.map { it.split("-")[0].toLong()..it.split("-")[1].toLong() }
    return input.takeLastWhile { it.isNotEmpty() }.count { ranges.any { range -> range.contains(it.toLong()) } }
}

fun part2(input: List<String>): Long {
    var ranges = input.takeWhile { it.isNotEmpty() }.map { Range(it.split("-")[0].toLong(),it.split("-")[1].toLong()) }
    while (true) {
        val startSize = ranges.size
        ranges = ranges.fold(mutableListOf()) { all, range ->
            all.firstOrNull { it.overlaps(range) }?.let {
                all.remove(it)
                all.add(range.intersection(it))
            } ?: all.add(range)
            all
        }
        if (ranges.size == startSize) {
            break
        }
    }
    return ranges.sumOf { it.end - it.start + 1 }
}

data class Range(val start: Long, val end: Long) {
    fun overlaps(range: Range): Boolean = range.start in start..end || range.end in start..end || start in range.start .. range.end || end in range.start..range.end

    fun intersection(range: Range) = Range(min(start, range.start), max(end, range.end))
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2025/day05/Day05_test")
    check(part1(testInput), 3)
    check(part2(testInput), 14)

    val input = readInput("src/main/kotlin/year2025/day05/Day05")
    println(part1(input))
    println(part2(input))
}
