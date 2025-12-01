package year2020.day11

import check
import readInput
import kotlin.math.abs

fun part1(input: List<String>): Int {
    var seats = input.mapIndexed { row, seats ->
        seats.mapIndexed { column, seat ->
            when (seat) {
                'L' -> Seat(false, column, row)
                '#' -> Seat(true, column, row)
                else -> null
            }
        }
    }.flatten().filterNotNull()
    var occupied = -1
    while (seats.count { it.occupied } != occupied) {
        occupied = seats.count { it.occupied }
        val nextSeats = seats.map { seat ->
            val surrounding = seats.filter { it != seat && abs(it.x - seat.x) <= 1 && abs(it.y - seat.y) <= 1 }
            if (seat.occupied && surrounding.count { it.occupied } >= 4) {
                seat.copy(occupied = false)
            } else if (!seat.occupied && surrounding.none { it.occupied }) {
                seat.copy(occupied = true)
            } else {
                seat
            }
        }
        seats = nextSeats
    }
    return occupied
}

fun part2(input: List<String>): Int {
    var seats = input.mapIndexed { row, seats ->
        seats.mapIndexed { column, seat ->
            when (seat) {
                'L' -> Seat(false, column, row)
                '#' -> Seat(true, column, row)
                else -> null
            }
        }
    }.flatten().filterNotNull()
    val xRange = 0..input[0].length
    val yRange = 0..input.size
    var occupied = -1
    while (seats.count { it.occupied } != occupied) {
        occupied = seats.count { it.occupied }
        val nextSeats = seats.map { seat ->
            val surrounding = (-1..1).zipAllCombinations(-1..1).mapNotNull { direction ->
                if (direction.first == 0 && direction.second == 0) {
                    null
                } else {
                    var nextX = seat.x + direction.first
                    var nextY = seat.y + direction.second
                    var nextSeat: Seat? = null
                    while (nextX in xRange && nextY in yRange) {
                        seats.firstOrNull { it.x == nextX && it.y == nextY }?.let {
                            nextSeat = it
                        }
                        if (nextSeat != null) {
                            break
                        }
                        nextX += direction.first
                        nextY += direction.second
                    }
                    nextSeat
                }
            }
            if (seat.occupied && surrounding.count { it.occupied } >= 5) {
                seat.copy(occupied = false)
            } else if (!seat.occupied && surrounding.none { it.occupied }) {
                seat.copy(occupied = true)
            } else {
                seat
            }
        }
        seats = nextSeats
    }
    return occupied
}

data class Seat(val occupied: Boolean, val x: Int, val y: Int, val surrounding: List<Seat> = listOf())

fun <S, T> Iterable<S>.zipAllCombinations(other: Iterable<T>) = this.flatMap { thisIt ->
    other.map { otherIt ->
        thisIt to otherIt
    }
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2020/day11/Day11_test")
    check(part1(testInput), 37)
    check(part2(testInput), 26)

    val input = readInput("src/main/kotlin/year2020/day11/Day11")
    println(part1(input))
    println(part2(input))
}
