package year2024.day13

import readInput
import kotlin.math.abs
import kotlin.math.min

fun part1(input: List<String>): Int {
    val machines = input.filter { it.isNotEmpty() }.windowed(3, 3).map { machine ->
        val first = machine[0].split(",")
        val second = machine[1].split(",")
        val third = machine[2].split(",")
        Triple(
            Pair(
                first[0].filter { it.isDigit() }.toInt(),
                first[1].filter { it.isDigit() }.toInt()
            ),
            Pair(
                second[0].filter { it.isDigit() }.toInt(),
                second[1].filter { it.isDigit() }.toInt()
            ),
            Pair(
                third[0].filter { it.isDigit() }.toInt(),
                third[1].filter { it.isDigit() }.toInt()
            )
        )
    }
    return machines.sumOf { machine ->
        var least = 401
        for (i in 0..100) {
            for (j in 0..100) {
                if (
                    machine.first.first * i + machine.second.first * j == machine.third.first &&
                    machine.first.second * i + machine.second.second * j == machine.third.second
                ) {
                    least = min(3 * i + j, least)
                }
            }
        }
        if (least == 401) {
            least = 0
        }
        least
    }
}

fun part2(input: List<String>): Long {
    val machines = input.filter { it.isNotEmpty() }.windowed(3, 3).map { machine ->
        val first = machine[0].split(",")
        val second = machine[1].split(",")
        val third = machine[2].split(",")
        Triple(
            Pair(
                first[0].filter { it.isDigit() }.toLong(),
                first[1].filter { it.isDigit() }.toLong()
            ),
            Pair(
                second[0].filter { it.isDigit() }.toLong(),
                second[1].filter { it.isDigit() }.toLong()
            ),
            Pair(
                10000000000000L + third[0].filter { it.isDigit() }.toLong(),
                10000000000000L + third[1].filter { it.isDigit() }.toLong()
            )
        )
    }
    return machines.sumOf { machine ->
        var a = 0L
        var b = 0L
        val denom = abs(machine.second.first * machine.first.second -
                machine.first.first * machine.second.second)
        if (
            (machine.second.first * machine.third.second -
                    machine.second.second * machine.third.first) %
            denom
            == 0L) {
            a = abs((machine.second.first * machine.third.second -
                        machine.second.second * machine.third.first) /
                    denom)
        }
        if (
            (machine.first.first * machine.third.second -
                    machine.first.second * machine.third.first) %
            denom
            == 0L) {
            b = abs((machine.first.first * machine.third.second -
                    machine.first.second * machine.third.first) /
                    denom)
        }
        a * 3 + b
    }
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2024/day13/Day13_test")
    check(part1(testInput) == 480)
    check(part2(testInput) == 875318608908L)

    val input = readInput("src/main/kotlin/year2024/day13/Day13")
    println(part1(input))
    println(part2(input))
}
