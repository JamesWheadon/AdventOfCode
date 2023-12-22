package year2023.day11

import readInput
import kotlin.math.max
import kotlin.math.min

fun part1(input: List<String>): Int {
    val (emptyRows, emptyColumns, galaxies) = getGalaxiesAndGaps(input)
    return galaxies.mapIndexed { index, pair ->
        galaxies.subList(index + 1, galaxies.size).sumOf { galaxy ->
            val columnRange = min(galaxy.first, pair.first)..<max(galaxy.first, pair.first)
            val rowRange = min(galaxy.second, pair.second)..<max(galaxy.second, pair.second)
            columnRange.count() + rowRange.count() + emptyColumns.count { it in columnRange } + emptyRows.count { it in rowRange }
        }
    }.sum()
}

fun part2(input: List<String>, distance: Long): Long {
    val (emptyRows, emptyColumns, galaxies) = getGalaxiesAndGaps(input)
    return galaxies.mapIndexed { index, pair ->
        galaxies.subList(index + 1, galaxies.size).sumOf { galaxy ->
            val columnRange = min(galaxy.first, pair.first)..<max(galaxy.first, pair.first)
            val rowRange = min(galaxy.second, pair.second)..<max(galaxy.second, pair.second)
            columnRange.count() + rowRange.count() + emptyColumns.count { it in columnRange } * (distance - 1) + emptyRows.count { it in rowRange } * (distance - 1)
        }
    }.sum()
}

private fun getGalaxiesAndGaps(input: List<String>): Triple<List<Int>, Set<Int>, List<Pair<Int, Int>>> {
    val emptyRows = input.mapIndexed { index, s -> if (!s.contains("#")) index else -1 }.filter { it != -1 }
    val emptyColumns = input.map {
        it.toCharArray().toList().mapIndexed { index, c -> if (c == '.') index else -1 }.filter { index -> index != -1 }
            .toSet()
    }.reduce { acc, ints -> acc.intersect(ints) }
    val galaxies = input.mapIndexed { rowIndex, row ->
        row.mapIndexed { columnIndex, c -> if (c == '#') Pair(columnIndex, rowIndex) else null }.filterNotNull()
    }.flatten()
    return Triple(emptyRows, emptyColumns, galaxies)
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2023/day11/Day11_test")
    check(part1(testInput) == 374)
    check(part2(testInput, 10) == 1030L)
    check(part2(testInput, 100) == 8410L)

    val input = readInput("src/main/kotlin/year2023/day11/Day11")
    println(part1(input))
    println(part2(input, 1000000))
}
