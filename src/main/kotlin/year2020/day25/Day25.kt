package year2020.day25

import check
import readInput

fun part1(input: List<String>): Long {
    val subject = 7
    var loop = 0
    var value = 1L
    while (value != input[0].toLong()) {
        value = (value * subject) % 20201227
        loop += 1
    }
    val doorSubject = input[1].toLong()
    var doorValue = 1L
    for (i in 1..loop) {
        doorValue = (doorValue * doorSubject) % 20201227
    }
    return doorValue
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2020/day25/Day25_test")
    check(part1(testInput), 14897079)

    val input = readInput("src/main/kotlin/year2020/day25/Day25")
    println(part1(input))
}
