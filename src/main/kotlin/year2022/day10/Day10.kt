package year2022.day10

import readInput
import kotlin.math.abs

fun part1(input: List<String>): Int {
    var cycle = 0
    var register = 1
    var total = 0
    input.forEach {
        if (it == "noop") {
            cycle++
            if ((cycle - 20) % 40 == 0) {
                total += cycle * register
            }
        } else {
            cycle++
            if ((cycle - 20) % 40 == 0) {
                total += cycle * register
            }
            cycle++
            if ((cycle - 20) % 40 == 0) {
                total += cycle * register
            }
            register += it.split(" ")[1].toInt()
        }
        if (cycle >= 220) {
            return total
        }
    }
    return total
}

fun part2(input: List<String>) {
    val display = MutableList(6) { MutableList(40) { '.' } }
    var cycle = 0
    var register = 1
    input.forEach {
        if (it == "noop") {
            if (abs(register - (cycle % 40)) <= 1) {
                println("cycle: $cycle, row is ${cycle / 40}")
                display[cycle / 40][cycle % 40] = '#'
            }
            cycle++
        } else {
            if (abs(register - (cycle % 40)) <= 1) {
                println("cycle: $cycle, row is ${cycle / 40}")
                display[cycle / 40][cycle % 40] = '#'
            }
            cycle++
            if (abs(register - (cycle % 40)) <= 1) {
                println("cycle: $cycle, row is ${cycle / 40}")
                display[cycle / 40][cycle % 40] = '#'
            }
            cycle++
            register += it.split(" ")[1].toInt()
        }
        if (cycle >= 240) {
            println(cycle)
            display.forEach { displayRow -> println(displayRow.joinToString("")) }
        }
    }
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("src/main/kotlin/year2022/day10/Day10_test")
    check(part1(testInput) == 13140)
    part2(testInput)

    val input = readInput("src/main/kotlin/year2022/day10/Day10")
    println(part1(input))
    part2(input)
}
