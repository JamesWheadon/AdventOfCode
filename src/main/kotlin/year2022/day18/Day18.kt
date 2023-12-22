package year2022.day18

import readInput
import kotlin.math.abs

fun getLavaCubes(input: List<String>): List<Cube> {
    val cubes = input.map { Cube(it.split(",")[0].toInt(), it.split(",")[1].toInt(), it.split(",")[2].toInt()) }
    for (i in cubes.indices) {
        for (j in i + 1..cubes.lastIndex) {
            if (cubes[i].adjacent(cubes[j])) {
                cubes[i].connected.add(cubes[j])
                cubes[j].connected.add(cubes[i])
            }
        }
    }
    return cubes
}

fun part1(input: List<String>): Int {
    val cubes = getLavaCubes(input)
    return cubes.sumOf { 6 - it.connected.size }
}

fun part2(input: List<String>): Int {
    val cubes = getLavaCubes(input)
    var airCubes = mutableListOf<Cube>()
    for (x in -1..22) {
        for (y in -1..22) {
            for (z in -1..22) {
                if (cubes.find { it.x == x && it.y == y && it.z == z } == null) {
                    val airCube = Cube(x, y, z)
                    airCube.connected.addAll(cubes.filter { airCube.adjacent(it) })
                    airCubes.add(airCube)
                }
            }
        }
    }
    var size = 0
    while (size != airCubes.size) {
        size = airCubes.size
        airCubes =
            airCubes.filter { cube -> cube.connected.size + airCubes.filter { cube.adjacent(it) }.size == 6 }
                .toMutableList()
    }
    println(airCubes.size)
    airCubes.sortedBy { it.x }.forEach { println(it) }
    cubes.forEach { cube ->
        airCubes.forEach {
            if (cube.adjacent(it)) {
                cube.connected.add(it)
            }
        }
    }
    return cubes.sumOf { 6 - it.connected.size }
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("src/main/kotlin/year2022/day18/Day18_test")
    check(part1(testInput) == 64)
    check(part2(testInput) == 58)

    val input = readInput("src/main/kotlin/year2022/day18/Day18")
    println(part1(input))
    println(part2(input))
}

class Cube(val x: Int, val y: Int, val z: Int) {
    var connected = mutableListOf<Cube>()
    override fun toString(): String {
        return "Cube(x=$x, y=$y, z=$z)"
    }

    fun adjacent(cube: Cube): Boolean {
        return (cube.x == x && cube.y == y && abs(cube.z - z) == 1) ||
            (cube.x == x && abs(cube.y - y) == 1 && cube.z == z) ||
            (abs(cube.x - x) == 1 && cube.y == y && cube.z == z)
    }
}
