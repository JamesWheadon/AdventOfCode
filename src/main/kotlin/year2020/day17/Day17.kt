package year2020.day17

import check
import readInput
import kotlin.math.abs

fun part1(input: List<String>): Int {
    var cubes = input.mapIndexed { y, s ->
        s.mapIndexed { x, c ->
            Cube(x, y, 0, c == '#')
        }
    }.flatten()
    for (i in 0..5) {
        cubes = cubes + cubes.flatMap { it.surrounding(cubes) }.distinct()
        cubes = cubes.map { it.cycle(cubes) }
    }
    return cubes.count { it.active }
}

fun part2(input: List<String>): Int {
    var cubes = input.mapIndexed { y, s ->
        s.mapIndexed { x, c ->
            HyperCube(x, y, 0, 0, c == '#')
        }
    }.flatten()
    for (i in 0..5) {
        cubes = cubes + cubes.flatMap { it.surrounding(cubes) }.distinct()
        cubes = cubes.map { it.cycle(cubes) }
    }
    return cubes.count { it.active }
}

data class Cube(val x: Int, val y: Int, val z: Int, val active: Boolean) {
    fun surrounding(cubes: List<Cube>): List<Cube> {
        val neighbours = mutableListOf<Cube>()
        for (x1 in x - 1 .. x + 1) {
            for (y1 in y - 1 .. y + 1) {
                for (z1 in z - 1 .. z + 1) {
                    if (cubes.none { it.x == x1 && it.y == y1 && it.z == z1 }) {
                        neighbours.add(Cube(x1, y1, z1, false))
                    }
                }
            }
        }
        return neighbours
    }

    fun cycle(cubes: List<Cube>): Cube {
        val neighbours = cubes.filter { abs(it.x - x) <= 1 && abs(it.y - y) <= 1 && abs(it.z - z) <= 1 && it != this }
        return if (active) {
            if (neighbours.count { it.active } !in 2..3) {
                this.copy(active = false)
            } else {
                this
            }
        } else {
            if (neighbours.count { it.active } == 3) {
                this.copy(active = true)
            } else {
                this
            }
        }
    }
}

data class HyperCube(val x: Int, val y: Int, val z: Int, val w: Int, val active: Boolean) {
    fun surrounding(cubes: List<HyperCube>): List<HyperCube> {
        val neighbours = mutableListOf<HyperCube>()
        for (x1 in x - 1 .. x + 1) {
            for (y1 in y - 1 .. y + 1) {
                for (z1 in z - 1 .. z + 1) {
                    for (w1 in w - 1 .. w + 1) {
                        if (cubes.none { it.x == x1 && it.y == y1 && it.z == z1 && it.w == w1 }) {
                            neighbours.add(HyperCube(x1, y1, z1, w1, false))
                        }
                    }
                }
            }
        }
        return neighbours
    }

    fun cycle(cubes: List<HyperCube>): HyperCube {
        val neighbours = cubes.filter { abs(it.x - x) <= 1 && abs(it.y - y) <= 1 && abs(it.z - z) <= 1 && abs(it.w - w) <= 1 && it != this }
        return if (active) {
            if (neighbours.count { it.active } !in 2..3) {
                this.copy(active = false)
            } else {
                this
            }
        } else {
            if (neighbours.count { it.active } == 3) {
                this.copy(active = true)
            } else {
                this
            }
        }
    }
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2020/day17/Day17_test")
    check(part1(testInput), 112)
    check(part2(testInput), 848)

    val input = readInput("src/main/kotlin/year2020/day17/Day17")
    println(part1(input))
    println(part2(input))
}
