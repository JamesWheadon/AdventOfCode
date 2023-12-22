package year2021.day17

import readInput
import kotlin.math.abs

fun part1(input: List<String>): Int {
    val (xRange, yRange) = getRanges(input)
    val possibleX = getPossibleX(xRange)
    val possibleY = mutableListOf<Int>()
    for (i in 0..maxOf(abs(yRange.first), abs(yRange.second))) {
        var distance = 0
        for (t in 0..<possibleX.minOrNull()!!) {
            distance += i - t
        }
        for (t in possibleX.minOrNull()!!..100000) {
            distance += (i - t)
            if (distance >= yRange.first && distance <= yRange.second) {
                possibleY.add(i)
                break
            }
            if (distance < yRange.first) {
                break
            }
        }
    }
    val maxY = possibleY.maxOrNull()!!
    return maxY * (maxY + 1) / 2
}

private fun getPossibleX(xRange: Pair<Int, Int>): MutableList<Int> {
    val possibleX = mutableListOf<Int>()
    for (i in 0..xRange.second) {
        val finalX = i * (i + 1) / 2
        if (finalX >= xRange.first && finalX <= xRange.second) {
            possibleX.add(i)
        }
    }
    return possibleX
}

fun part2(input: List<String>): Int {
    val (xRange, yRange) = getRanges(input)
    val possibleInitial = mutableListOf<Pair<Int, Int>>()
    for (i in 0..xRange.second) {
        val finalDistance = i * (i + 1) / 2
        if (finalDistance >= xRange.first) {
            var minSteps = i
            val pair = checkDistanceInRange(finalDistance, xRange, i, minSteps)
            minSteps = pair.first
            val stopsInRange = pair.second
            if (!stopsInRange) {
                getPossibleInitial(i, xRange, yRange, possibleInitial)
            } else {
                getPossibleInitial(yRange, minSteps, possibleInitial, i)
            }
        }
    }
    return possibleInitial.distinct().size
}

private fun getPossibleInitial(
    i: Int,
    xRange: Pair<Int, Int>,
    yRange: Pair<Int, Int>,
    possibleInitial: MutableList<Pair<Int, Int>>,
) {
    val possibleSteps = mutableListOf<Int>()
    var distance = 0
    for (t in 0..i) {
        distance += (i - t)
        if (distance >= xRange.first && distance <= xRange.second) {
            possibleSteps.add(t)
        }
        if (distance > xRange.second) {
            break
        }
    }
    possibleSteps.forEach {
        for (j in yRange.first..abs(yRange.first)) {
            val positionAtStep = j + j * it - (it * (it + 1)) / 2
            if (positionAtStep >= yRange.first && positionAtStep <= yRange.second) {
                possibleInitial.add(Pair(i, j))
            }
        }
    }
}

private fun checkDistanceInRange(
    finalDistance: Int,
    xRange: Pair<Int, Int>,
    i: Int,
    minSteps: Int,
): Pair<Int, Boolean> {
    var stopsInRange = false
    var minSteps1 = minSteps
    if (finalDistance <= xRange.second) {
        stopsInRange = true
        var distance = 0
        for (t in 0..i) {
            distance += (i - t)
            if (distance >= xRange.first && distance <= xRange.second) {
                minSteps1 = t
                break
            }
        }
    }
    return Pair(minSteps1, stopsInRange)
}

private fun getPossibleInitial(
    yRange: Pair<Int, Int>,
    minSteps: Int,
    possibleInitial: MutableList<Pair<Int, Int>>,
    i: Int,
) {
    for (j in -maxOf(abs(yRange.first), abs(yRange.second))..maxOf(
        abs(yRange.first),
        abs(yRange.second),
    )) {
        var distance = 0
        for (t in 0..<minSteps) {
            distance += j - t
        }
        for (t in minSteps..100000) {
            distance += (j - t)
            if (distance >= yRange.first && distance <= yRange.second) {
                possibleInitial.add(Pair(i, j))
                break
            }
            if (distance < yRange.first) {
                break
            }
        }
    }
}

private fun getRanges(input: List<String>): Pair<Pair<Int, Int>, Pair<Int, Int>> {
    val xRangeStrings = input[0].split(" ")[2].split("=")[1].split("..")
    val xRange = Pair(xRangeStrings[0].toInt(), xRangeStrings[1].removeSuffix(",").toInt())
    val yRangeStrings = input[0].split(" ")[3].split("=")[1].split("..")
    val yRange = Pair(yRangeStrings[0].toInt(), yRangeStrings[1].toInt())
    return Pair(xRange, yRange)
}

fun main() {
    val testInput1 = readInput("src/main/kotlin/year2021/day17/Day17_test")
    check(part1(testInput1) == 45)
    check(part2(testInput1) == 112)

    val input = readInput("src/main/kotlin/year2021/day17/Day17")
    println(part1(input))
    println(part2(input))
}
