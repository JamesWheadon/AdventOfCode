package year2021.day22

import readInput
import kotlin.math.max
import kotlin.math.min

fun part1(input: List<String>): Int {
    val grid = MutableList(101) { MutableList(101) { MutableList(101) { false } } }
    input.forEach {
        val command = it.split(" ")[0]
        val xRange = getXRange(it)
        val yRange = getYRange(it)
        val zRange = getZRange(it)
        for (x in max(xRange.first, -50)..min(xRange.second, 50)) {
            for (y in max(yRange.first, -50)..min(yRange.second, 50)) {
                for (z in max(zRange.first, -50)..min(zRange.second, 50)) {
                    grid[x + 50][y + 50][z + 50] = command.length == 2
                }
            }
        }
    }
    return grid.flatten().flatten().count { it }
}

fun part2(input: List<String>): Long {
    var cuboidsOn = mutableListOf<Cuboid>()
    input.forEach { row ->
        val nextCuboids = mutableListOf<Cuboid>()
        val command = row.split(" ")[0]
        val xRange = Pair(
            row.split("=")[1].split(",")[0].split("..")[0].toInt(),
            row.split("=")[1].split(",")[0].split("..")[1].toInt(),
        )
        val yRange = Pair(
            row.split("=")[2].split(",")[0].split("..")[0].toInt(),
            row.split("=")[2].split(",")[0].split("..")[1].toInt(),
        )
        val zRange = Pair(
            row.split("=")[3].split("..")[0].toInt(),
            row.split("=")[3].split("..")[1].toInt(),
        )
        cuboidsOn.forEach {
            nextCuboids.addAll(it.cuboidOff(xRange, yRange, zRange))
        }
        if (command.length == 2) {
            val element = Cuboid(xRange, yRange, zRange)
            nextCuboids.add(element)
        }
        cuboidsOn = nextCuboids
    }
    var total = 0L
    cuboidsOn.forEach {
        total += it.size()
    }
    return total
}

private fun getXRange(it: String) = Pair(
    it.split("=")[1].split(",")[0].split("..")[0].toInt(),
    it.split("=")[1].split(",")[0].split("..")[1].toInt(),
)

private fun getYRange(it: String) = Pair(
    it.split("=")[2].split(",")[0].split("..")[0].toInt(),
    it.split("=")[2].split(",")[0].split("..")[1].toInt(),
)

private fun getZRange(it: String) = Pair(
    it.split("=")[3].split("..")[0].toInt(),
    it.split("=")[3].split("..")[1].toInt(),
)

fun main() {
    val testInput1 = readInput("src/main/kotlin/year2021/day22/Day22_test_part1")
    val testInput2 = readInput("src/main/kotlin/year2021/day22/Day22_test_part2")
    check(part1(testInput1) == 590784)
    check(part2(testInput2) == 2758514936282235)

    val input = readInput("src/main/kotlin/year2021/day22/Day22")
    println(part1(input))
    println(part2(input))
}
