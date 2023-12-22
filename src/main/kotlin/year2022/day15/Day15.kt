package year2022.day15

import readInput
import kotlin.math.abs

fun createSensorFromCoordList(coords: List<Int>): Sensor {
    return Sensor(
        coords[0],
        coords[1],
        (abs(coords[0] - coords[2]) + abs(coords[1] - coords[3])),
        Beacon(coords[2], coords[3]),
    )
}

fun createNewRanges(rangePair: Pair<IntRange, IntRange>, step: Int, max: Int): MutableList<Pair<IntRange, IntRange>> {
    val newRanges = mutableListOf<Pair<IntRange, IntRange>>()
    for (y in rangePair.second.first..rangePair.second.last step step) {
        for (x in rangePair.first.first..rangePair.first.last step step) {
            var xMax = x + step - 1
            if (xMax > max) {
                xMax = max
            }
            var yMax = y + step - 1
            if (yMax > max) {
                yMax = max
            }
            newRanges.add(Pair(x..xMax, y..yMax))
        }
    }
    return newRanges
}

fun part1(input: List<String>, row: Int): Int {
    val sensors = input.map { sensorInfo ->
        createSensorFromCoordList(
            sensorInfo.replace(':', ',')
                .filter { it.isDigit() || it == ',' || it == '-' }
                .split(",")
                .map { it.toInt() },
        )
    }
    val intervals = sensors.mapNotNull { sensor -> sensor.getRangeCoveredOnRow(row) }
    val minX = sensors.minOf { sensor -> sensor.x - sensor.distanceToNearest }
    val maxX = sensors.maxOf { sensor -> sensor.x + sensor.distanceToNearest }
    var covered = 0
    for (x in minX..maxX) {
        if (intervals.any { it.contains(x) } && sensors.filter { it.beacon.y == row }.none { it.beacon.x == x }) {
            covered++
        }
    }
    return covered
}

fun part2(input: List<String>, max: Int): Long {
    val sensors = input.map { sensorInfo ->
        createSensorFromCoordList(
            sensorInfo.replace(':', ',')
                .filter { it.isDigit() || it == ',' || it == '-' }
                .split(",")
                .map { it.toInt() },
        )
    }
    var step = 4096
    var ranges = createNewRanges(Pair(0..max, 0..max), step, max)
    step /= 2
    while (true) {
        println("step: $step, number of ranges: ${ranges.size}")
        sensors.forEach { sensor ->
            ranges = ranges.filter { !sensor.containsRanges(it.first, it.second) }.toMutableList()
        }
        if (ranges.size == 1 && ranges[0].first.first == ranges[0].first.last) {
            break
        }
        ranges = ranges.map { rangePair -> createNewRanges(rangePair, step, max) }.flatten().toMutableList()
        step /= 2
    }
    println("printing ranges")
    ranges.forEach { println(it) }
    return ranges.first().first.first * 4000000L + ranges.first().second.first
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("src/main/kotlin/year2022/day15/Day15_test")
    check(part1(testInput, 10) == 26)
    check(part2(testInput, 20) == 56000011L)

    val input = readInput("src/main/kotlin/year2022/day15/Day15")
    println(part1(input, 2000000))
    println(part2(input, 4000000))
}

class Sensor(val x: Int, private val y: Int, val distanceToNearest: Int, val beacon: Beacon) {

    fun getRangeCoveredOnRow(row: Int): IntRange? {
        return if ((y <= row && y + distanceToNearest >= row) || (y > row && y - distanceToNearest <= row)) {
            x - (distanceToNearest - abs(y - row))..x + (distanceToNearest - abs(y - row))
        } else {
            null
        }
    }

    fun containsRanges(xRange: IntRange, yRange: IntRange): Boolean {
        return pointCovered(xRange.first, yRange.first) &&
            pointCovered(xRange.first, yRange.last) &&
            pointCovered(xRange.last, yRange.first) &&
            pointCovered(xRange.last, yRange.last)
    }

    private fun pointCovered(pointX: Int, pointY: Int): Boolean {
        return abs(x - pointX) + abs(y - pointY) <= distanceToNearest
    }
}

data class Beacon(val x: Int, val y: Int)
