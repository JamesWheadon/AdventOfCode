package year2023.day22

import readInput

fun part1(input: List<String>): Int {
    val bricks = input.map {
        val start = it.split("~")[0].split(",").map { s -> s.toInt() }
        val end = it.split("~")[1].split(",").map { s -> s.toInt() }
        Brick(start[0]..end[0], start[1]..end[1], start[2]..end[2])
    }
    var changed = true
    while (changed) {
        changed = false
        bricks.forEach { brick ->
            val below = brick.z.first - 1
            if (below > 0) {
                val bricksBelow = bricks.filter { below in it.z }
                if (bricksBelow.none { it.x.intersect(brick.x).isNotEmpty() && it.y.intersect(brick.y).isNotEmpty() }) {
                    brick.moveDown()
                    changed = true
                }
            }
        }
    }
    bricks.forEach { brick ->
        brick.getSupportingBricks(bricks.filter { it != brick })
    }
    return bricks.filter { brick -> bricks.none { it.fallsIfRemoved(brick) } }.size
}

fun part2(input: List<String>): Int {
    val bricks = input.map {
        val start = it.split("~")[0].split(",").map { s -> s.toInt() }
        val end = it.split("~")[1].split(",").map { s -> s.toInt() }
        Brick(start[0]..end[0], start[1]..end[1], start[2]..end[2])
    }
    var changed = true
    while (changed) {
        changed = false
        bricks.forEach { brick ->
            val below = brick.z.first - 1
            if (below > 0) {
                val bricksBelow = bricks.filter { below in it.z }
                if (bricksBelow.none { it.x.intersect(brick.x).isNotEmpty() && it.y.intersect(brick.y).isNotEmpty() }) {
                    brick.moveDown()
                    changed = true
                }
            }
        }
    }
    bricks.forEach { brick ->
        brick.getSupportingBricks(bricks.filter { it != brick })
    }
    return bricks.sumOf {
        it.getFallingBricks(mutableListOf(it)) - 1
    }
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2023/day22/Day22_test")
    check(part1(testInput) == 5)
    check(part2(testInput) == 7)

    val input = readInput("src/main/kotlin/year2023/day22/Day22")
    println(part1(input))
    println(part2(input))
}

class Brick(val x: IntRange, val y: IntRange, var z: IntRange) {
    private lateinit var supporting: List<Brick>
    val supported: MutableList<Brick> = mutableListOf()

    fun moveDown() {
        z = z.first - 1..<z.last
    }

    fun getSupportingBricks(otherBricks: List<Brick>) {
        val bricksBelow = otherBricks.filter { z.first - 1 in it.z }
        supporting = bricksBelow.filter { x.intersect(it.x).isNotEmpty() && y.intersect(it.y).isNotEmpty() }
        supporting.forEach { it.supported.add(this) }
    }

    fun fallsIfRemoved(brick: Brick): Boolean {
        if (supporting.size > 1) {
            return false
        }
        return supporting.contains(brick)
    }

    fun fallsIfRemoved(bricks: List<Brick>): Boolean {
        return supporting.filter { !bricks.contains(it) }.isEmpty()
    }

    fun getFallingBricks(falling: MutableList<Brick>): Int {
        val new = mutableListOf<Brick>()
        supported.forEach {
            if (it.fallsIfRemoved(falling) && !falling.contains(it)) {
                falling.add(it)
                new.add(it)
            }
        }
        new.forEach { it.getFallingBricks(falling) }
        return falling.size
    }

    override fun toString(): String {
        return "Brick(x=$x, y=$y, z=$z)"
    }
}
