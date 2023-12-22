package year2021.day19

import readInput

fun part1(input: List<String>): Int {
    val map = getMap(input)
    return map.findAllBeacons()
}

fun part2(input: List<String>): Int {
    val map = getMap(input)
    return map.findFurthestBeacons()
}

private fun getMap(input: List<String>): Map {
    val scanners = mutableListOf<Scanner>()
    input.forEach {
        if (it.isNotEmpty()) {
            if (it.contains("scanner")) {
                scanners.add(Scanner())
            } else {
                val position = it.split(",").map { coord -> coord.toInt() }
                scanners.last().beacons.add(Beacon(position[0], position[1], position[2]))
            }
        }
    }
    return Map(scanners)
}

fun main() {
    val testInput1 = readInput("src/main/kotlin/year2021/day19/Day19_test")
    check(part1(testInput1) == 79)
    check(part2(testInput1) == 3621)

    val input = readInput("src/main/kotlin/year2021/day19/Day19")
    println(part1(input))
    println(part2(input))
}
