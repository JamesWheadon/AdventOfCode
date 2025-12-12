package year2025.day09

import check
import readInput
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun part1(input: List<String>): Long {
    val points = input.map { Pair(it.split(",")[0].toLong(), it.split(",")[1].toLong()) }
    val allPoints = points.mapIndexed { index, pair -> points.subList(index + 1, points.size).map { Pair(pair, it) } }
        .flatten()
    return allPoints.maxOf { (abs(it.first.first - it.second.first) + 1) * (abs(it.first.second - it.second.second) + 1) }
}

fun part2(input: List<String>): Long {
    val points = input.map { Point(it.split(",")[0].toLong(), it.split(",")[1].toLong()) }
    val xMin = points.minOf { it.x }
    val lines = (points.windowed(2, 1) + listOf(listOf(points.last(), points.first()))).map { line ->
        val xRange = min(line.first().x, line.last().x)..max(line.first().x, line.last().x)
        val yRange = min(line.first().y, line.last().y)..max(line.first().y, line.last().y)
        Line(xRange, yRange)
    }
    return points.mapIndexed { index, point ->
        points.subList(index + 1, points.size).map { other ->
            Rectangle(Point(min(point.x, other.x), min(point.y, other.y)), Point(max(point.x, other.x), max(point.y, other.y)))
        }
    }
        .flatten()
        .sortedByDescending { it.area() }
        .first { (topLeft, bottomRight) ->
            val xRangeInner = (topLeft.x + 1)..(bottomRight.x - 1)
            val yRangeInner = (topLeft.y + 1)..(bottomRight.y - 1)
            val xRange = topLeft.x..bottomRight.x
            val yRange = topLeft.y..bottomRight.y
            if (
                points.any { it.x in xRangeInner && it.y in yRangeInner } ||
                lines.any {
                    (it.xRange.containsAll(xRange) && it.yRange.intersect(yRangeInner).isNotEmpty()) ||
                            (it.yRange.containsAll(yRange) && it.xRange.intersect(xRangeInner).isNotEmpty())
                }
            ) {
                false
            } else {
                var crossed = 0
                var x = (topLeft.x + bottomRight.x) / 2
                val y = (topLeft.y + bottomRight.y) / 2
                while (x >= xMin) {
                    crossed += lines.count { it.xRange.contains(x) && it.yRange.contains(y) }
                    x--
                }
                crossed % 2 == 1
            }
        }.area()
}

data class Point(val x: Long, val y: Long)
data class Line(val xRange: LongRange, val yRange: LongRange)
data class Rectangle(val topLeft: Point, val bottomRight: Point) {
    fun area() = (bottomRight.x - topLeft.x + 1) * (bottomRight.y - topLeft.y + 1)
}

private fun LongRange.containsAll(other: LongRange) = other.all { contains(it) }

fun main() {
    val testInput = readInput("src/main/kotlin/year2025/day09/Day09_test")
    check(part1(testInput), 50)
    check(part2(testInput), 24)

    val input = readInput("src/main/kotlin/year2025/day09/Day09")
    println(part1(input))
    println(part2(input))
}
