package year2020.day12

import check
import readInput
import kotlin.math.abs

fun part1(input: List<String>): Int {
    var ship = Ship(0, 0, 'E')
    val directions = listOf('E', 'S', 'W', 'N')
    input.forEach { instruction ->
        when {
            instruction[0] == 'N' -> ship = ship.copy(nS = ship.nS + instruction.filter { it.isDigit() }.toInt())
            instruction[0] == 'S' -> ship = ship.copy(nS = ship.nS - instruction.filter { it.isDigit() }.toInt())
            instruction[0] == 'E' -> ship = ship.copy(eW = ship.eW + instruction.filter { it.isDigit() }.toInt())
            instruction[0] == 'W' -> ship = ship.copy(eW = ship.eW - instruction.filter { it.isDigit() }.toInt())
            instruction[0] == 'L' -> ship = ship.copy(d = directions[(4 + directions.indexOf(ship.d) - instruction.filter { it.isDigit() }.toInt() / 90) % 4])
            instruction[0] == 'R' -> ship = ship.copy(d = directions[(directions.indexOf(ship.d) + instruction.filter { it.isDigit() }.toInt() / 90) % 4])
            instruction[0] == 'F' -> when (ship.d) {
                'N' -> ship = ship.copy(nS = ship.nS + instruction.filter { it.isDigit() }.toInt())
                'S' -> ship = ship.copy(nS = ship.nS - instruction.filter { it.isDigit() }.toInt())
                'E' -> ship = ship.copy(eW = ship.eW + instruction.filter { it.isDigit() }.toInt())
                'W' -> ship = ship.copy(eW = ship.eW - instruction.filter { it.isDigit() }.toInt())
            }
        }
    }
    return abs(ship.eW) + abs(ship.nS)
}

fun part2(input: List<String>): Int {
    var wayPoint = Location(10, 1)
    var ship = Location(0, 0)
    input.forEach { instruction ->
        when {
            instruction[0] == 'N' -> wayPoint = wayPoint.copy(nS = wayPoint.nS + instruction.filter { it.isDigit() }.toInt())
            instruction[0] == 'S' -> wayPoint = wayPoint.copy(nS = wayPoint.nS - instruction.filter { it.isDigit() }.toInt())
            instruction[0] == 'E' -> wayPoint = wayPoint.copy(eW = wayPoint.eW + instruction.filter { it.isDigit() }.toInt())
            instruction[0] == 'W' -> wayPoint = wayPoint.copy(eW = wayPoint.eW - instruction.filter { it.isDigit() }.toInt())
            instruction[0] == 'L' -> {
                for (i in 0..<instruction.filter { it.isDigit() }.toInt() / 90) {
                    wayPoint = wayPoint.copy(eW = -wayPoint.nS, nS = wayPoint.eW)
                }
            }
            instruction[0] == 'R' -> {
                for (i in 0..<instruction.filter { it.isDigit() }.toInt() / 90) {
                    wayPoint = wayPoint.copy(eW = wayPoint.nS, nS = -wayPoint.eW)
                }
            }
            instruction[0] == 'F' -> {
                val eWMove = wayPoint.eW
                val nSMove = wayPoint.nS
                val times = instruction.filter { it.isDigit() }.toInt()
                ship = ship.copy(eW = ship.eW + eWMove * times, nS = ship.nS + nSMove * times)
            }
        }
    }
    return abs(ship.eW) + abs(ship.nS)
}

data class Ship(val eW: Int, val nS: Int, val d: Char)
data class Location(val eW: Int, val nS: Int)

fun main() {
    val testInput = readInput("src/main/kotlin/year2020/day12/Day12_test")
    check(part1(testInput), 25)
    check(part2(testInput), 286)

    val input = readInput("src/main/kotlin/year2020/day12/Day12")
    println(part1(input))
    println(part2(input))
}
