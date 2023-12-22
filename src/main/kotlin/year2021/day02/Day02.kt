package year2021.day02

import readInput

fun part1(input: List<String>): Int {
    var forward = 0
    var depth = 0
    for (i in input.indices) {
        val direction = input[i].split(" ")[0]
        val distance = input[i].split(" ")[1].toInt()
        if (direction == "forward") {
            forward += distance
        } else {
            if (direction == "up") {
                depth -= distance
            } else {
                depth += distance
            }
        }
    }
    return forward * depth
}

fun part2(input: List<String>): Int {
    var forward = 0
    var depth = 0
    var aim = 0
    for (i in input.indices) {
        val direction = input[i].split(" ")[0]
        val distance = input[i].split(" ")[1].toInt()
        if (direction == "forward") {
            forward += distance
            depth += (distance * aim)
        } else {
            if (direction == "up") {
                aim -= distance
            } else {
                aim += distance
            }
        }
    }
    return forward * depth
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2021/day02/Day02_test")
    check(part1(testInput) == 150)
    check(part2(testInput) == 900)

    val input = readInput("src/main/kotlin/year2021/day02/Day02")
    println(part1(input))
    println(part2(input))
}
