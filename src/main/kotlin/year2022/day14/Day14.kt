package year2022.day14

import readInput

fun part1(input: List<String>): Int {
    val (rocks, xMin, xMax) = getRocks(input)
    val yMax = rocks.maxOf { rock -> rock.maxOf { it.second } }
    val grid = fillInGrid(yMax, xMax, xMin, rocks)
    try {
        while (true) {
            val sandPosition = mutableListOf(0, 500 - xMin)
            traverseGrid(grid, sandPosition)
        }
    } catch (_: Exception) {
        // Exception thrown when process is finished
    }
    grid.forEach {
        println(it)
    }
    return grid.sumOf { row -> row.count { it == "o" } }
}

private fun fillInGrid(
    yMax: Int,
    xMax: Int,
    xMin: Int,
    rocks: List<List<Pair<Int, Int>>>,
): MutableList<MutableList<String>> {
    val grid = MutableList(yMax + 1) { MutableList(xMax - xMin + 1) { "." } }
    grid[0][500 - xMin] = "+"
    rocks.forEach { rock ->
        rock.windowed(2).forEach { line ->
            for (y in line[0].second..line[1].second) {
                addRocksToGrid(line, xMin, grid, y)
            }
            for (y in line[1].second..line[0].second) {
                addRocksToGrid(line, xMin, grid, y)
            }
        }
    }
    return grid
}

private fun addRocksToGrid(
    line: List<Pair<Int, Int>>,
    xMin: Int,
    grid: MutableList<MutableList<String>>,
    y: Int,
) {
    for (x in line[0].first - xMin..line[1].first - xMin) {
        grid[y][x] = "#"
    }
    for (x in line[1].first - xMin..line[0].first - xMin) {
        grid[y][x] = "#"
    }
}

fun part2(input: List<String>): Int {
    val (rocks, xMin, xMax) = getRocks(input)
    val yMax = rocks.maxOf { rock -> rock.maxOf { it.second } } + 2
    val grid = fillInGrid(yMax, xMax, xMin, rocks)
    for (x in xMin..xMax) {
        grid[yMax][x - xMin] = "#"
    }
    for (y in 0..<yMax) {
        if (grid[y][0] != ".") {
            for (yAdd in 0..<yMax) {
                grid[yAdd].add(0, ".")
                grid[yAdd].add(0, ".")
            }
            grid[yMax].add(0, "#")
            grid[yMax].add(0, "#")
            break
        }
    }
    for (y in 0..<yMax) {
        if (grid[y].last() != ".") {
            for (yAdd in 0..<yMax) {
                grid[yAdd].add(".")
                grid[yAdd].add(".")
            }
            grid[yMax].add("#")
            grid[yMax].add("#")
            break
        }
    }
    while (grid[0].contains("+")) {
        sandPour(yMax, grid)
    }
    grid.forEach {
        println(it)
    }
    return grid.sumOf { row -> row.count { it == "o" } }
}

private fun sandPour(
    yMax: Int,
    grid: MutableList<MutableList<String>>,
) {
    for (y in 0..<yMax) {
        if (grid[y][1] != ".") {
            for (yAdd in 0..<yMax) {
                grid[yAdd].add(0, ".")
            }
            grid[yMax].add(0, "#")
            break
        }
    }
    for (y in 0..<yMax) {
        if (grid[y][grid[y].size - 2] != ".") {
            for (yAdd in 0..<yMax) {
                grid[yAdd].add(".")
            }
            grid[yMax].add("#")
            break
        }
    }
    val sandPosition = mutableListOf(0, grid[0].indexOf("+"))
    traverseGrid(grid, sandPosition)
}

private fun getRocks(input: List<String>): Triple<List<List<Pair<Int, Int>>>, Int, Int> {
    val rocks =
        input.map { rock -> rock.split(" -> ").map { Pair(it.split(",")[0].toInt(), it.split(",")[1].toInt()) } }
    val xMin = rocks.minOf { rock -> rock.minOf { it.first } }
    val xMax = rocks.maxOf { rock -> rock.maxOf { it.first } }
    return Triple(rocks, xMin, xMax)
}

private fun traverseGrid(
    grid: MutableList<MutableList<String>>,
    sandPosition: MutableList<Int>,
) {
    var moving = true
    while (moving) {
        when {
            grid[sandPosition[0] + 1][sandPosition[1]] == "." -> {
                sandPosition[0]++
            }

            grid[sandPosition[0] + 1][sandPosition[1] - 1] == "." -> {
                sandPosition[0]++
                sandPosition[1]--
            }

            grid[sandPosition[0] + 1][sandPosition[1] + 1] == "." -> {
                sandPosition[0]++
                sandPosition[1]++
            }

            else -> {
                moving = false
            }
        }
    }
    grid[sandPosition[0]][sandPosition[1]] = "o"
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("src/main/kotlin/year2022/day14/Day14_test")
    check(part1(testInput) == 24)
    check(part2(testInput) == 93)

    val input = readInput("src/main/kotlin/year2022/day14/Day14")
    println(part1(input))
    println(part2(input))
}
