package year2020.day24

import check
import readInput
import kotlin.math.abs

fun part1(input: List<String>): Int {
    return input.map { dirs ->
        val ne = dirs.windowed(2).count { it == "ne" }
        val nw = dirs.windowed(2).count { it == "nw" }
        val se = dirs.windowed(2).count { it == "se" }
        val sw = dirs.windowed(2).count { it == "sw" }
        val e = dirs.count { it == 'e' } - ne - se
        val w = dirs.count { it == 'w' } - nw - sw
        (e - w) + ((ne - sw) * 0.5) - ((nw - se) * 0.5) to (ne - sw) + (nw - se)
    }.groupingBy { it }.eachCount().filter { it.value % 2 == 1 }.count()
}

fun part2(input: List<String>): Int {
    var blackTiles = input.map { dirs ->
        val ne = dirs.windowed(2).count { it == "ne" }
        val nw = dirs.windowed(2).count { it == "nw" }
        val se = dirs.windowed(2).count { it == "se" }
        val sw = dirs.windowed(2).count { it == "sw" }
        val e = dirs.count { it == 'e' } - ne - se
        val w = dirs.count { it == 'w' } - nw - sw
        Tile((e - w) + ((ne - sw) * 0.5) - ((nw - se) * 0.5), (ne - sw) + (nw - se))
    }.groupingBy { it }.eachCount().filter { it.value % 2 == 1 }.map { it.key }.toSet()
    for (i in 1..100) {
        val whiteTiles = blackTiles.flatMap { it.surrounding() }.distinct().minus(blackTiles)
        val whiteFlips = whiteTiles.filter { white -> blackTiles.count { it.adjacent(white) } == 2 }.toSet()
        val blackFlips = blackTiles.filter { black -> !listOf(1, 2).contains(blackTiles.count { it.adjacent(black) }) }.toSet()
        blackTiles = blackTiles + whiteFlips - blackFlips
    }
    return blackTiles.size
}

data class Tile(val x: Double, val y: Int) {
    fun adjacent(other: Tile): Boolean {
        return other != this && abs(x - other.x) <= 1.0 && abs(y - other.y) <= 1
    }

    fun surrounding(): List<Tile> {
        return listOf(
            Tile(x - 1, y),
            Tile(x + 1, y),
            Tile(x - 0.5, y + 1),
            Tile(x - 0.5, y - 1),
            Tile(x + 0.5, y + 1),
            Tile(x + 0.5, y - 1)
        )
    }
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2020/day24/Day24_test")
    check(part1(testInput), 10)
    check(part2(testInput), 2208)

    val input = readInput("src/main/kotlin/year2020/day24/Day24")
    println(part1(input))
    println(part2(input))
}
