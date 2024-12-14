package year2024.day14

import readInput
import java.io.File
import kotlin.math.abs

val pattern = "p=([\\-0-9]+),([\\-0-9]+) v=([\\-0-9]+),([\\-0-9]+)".toRegex()

fun part1(input: List<String>, width: Int, height: Int): Int {
    val robots = input.map { robot ->
        val find = pattern.find(robot)!!.groups
        Robot(find[1]!!.value.toInt(), find[2]!!.value.toInt(), find[3]!!.value.toInt(), find[4]!!.value.toInt())
    }.map { robot ->
        robot.copy(
            x = ((robot.x + 100 * robot.vX) % width + width) % width,
            y = ((robot.y + 100 * robot.vY) % height + height) % height
        )
    }
    return robots.count { robot -> robot.x < width / 2 && robot.y < height / 2 } *
            robots.count { robot -> robot.x > width / 2 && robot.y < height / 2 } *
            robots.count { robot -> robot.x < width / 2 && robot.y > height / 2 } *
            robots.count { robot -> robot.x > width / 2 && robot.y > height / 2 }
}

fun part2(input: List<String>, width: Int, height: Int) {
    val robots = input.map { robot ->
        val find = pattern.find(robot)!!.groups
        Robot(find[1]!!.value.toInt(), find[2]!!.value.toInt(), find[3]!!.value.toInt(), find[4]!!.value.toInt())
    }
    var i = 0
    while (true) {
        val moveRobots = robots.map { robot ->
            robot.copy(
                x = ((robot.x + i * robot.vX) % width + width) % width,
                y = ((robot.y + i * robot.vY) % height + height) % height
            )
        }
        if (moveRobots.count { robot -> moveRobots.filter { it != robot }.minOf { abs(it.x - robot.x) + abs(it.y - robot.y) } == 1} > robots.size / 2) {
            val graph = mutableListOf<String>()
            for (j in 0..<height) {
                var row = ""
                for (k in 0..<width) {
                    row += if (moveRobots.any { robot -> robot.x == j && robot.y == k }) {
                        "#"
                    } else {
                        "."
                    }
                }
                graph.add(row)
            }
            File("move$i.txt").printWriter().use { out ->
                graph.forEach { row ->
                    out.println(row)
                }
            }
        }
        i += 1
    }
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2024/day14/Day14_test")
    check(part1(testInput, 11, 7) == 12)
//    check(part2(testInput) == 0)

    val input = readInput("src/main/kotlin/year2024/day14/Day14")
    println(part1(input, 101, 103))
    println(part2(input, 101, 103))
}

data class Robot(val x: Int, val y: Int, val vX: Int, val vY: Int)
