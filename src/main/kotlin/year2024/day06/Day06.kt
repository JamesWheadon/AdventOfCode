package year2024.day06

import readInput

fun part1(input: List<String>): Int {
    var x = 0
    var y = input.indexOfFirst { row -> row.indexOf('^').also { x = it } != -1 }
    var dx = 0
    var dy = -1
    val visited = mutableSetOf<String>()
    while (y in 1..<input.size - 1 && x in 1 ..<input[0].length - 1) {
        visited.add("$x,$y")
        while (input[y + dy][x + dx] == '#') {
            val tempDx = -dy
            dy = dx
            dx = tempDx
        }
        x += dx
        y += dy
    }
    return visited.size + 1
}

fun part2(input: List<String>): Int {
    var t = 0
    var x = 0
    var y = input.indexOfFirst { row -> row.indexOf('^').also { x = it } != -1 }
    var dx = 0
    var dy = -1
    val visited = mutableSetOf<String>()
    while (y in 1..<input.size - 1 && x in 1 ..<input[0].length - 1) {
        visited.add("$x,$y")
        while (input[y + dy][x + dx] == '#') {
            val tempDx = -dy
            dy = dx
            dx = tempDx
        }
        if (!visited.contains("${x + dx},${y + dy}")) {
            var turnX = x
            var turnY = y
            var turnDx = dx
            var turnDy = dy
            val turnVisited = mutableSetOf<String>()
            while (turnY in 1..<input.size - 1 && turnX in 1..<input[0].length - 1) {
                turnVisited.add("$turnX,$turnY,$turnDx,$turnDy")
                while (input[turnY + turnDy][turnX + turnDx] == '#' || (turnY + turnDy == y + dy && turnX + turnDx == x + dx)) {
                    val tempDx = -turnDy
                    turnDy = turnDx
                    turnDx = tempDx
                }
                turnY += turnDy
                turnX += turnDx
                if (turnVisited.contains("$turnX,$turnY,$turnDx,$turnDy")) {
                    t += 1
                    break
                }
            }
        }
        x += dx
        y += dy
    }
    return t
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2024/day06/Day06_test")
    check(part1(testInput) == 41)
    check(part2(testInput) == 6)

    val input = readInput("src/main/kotlin/year2024/day06/Day06")
    println(part1(input))
    println(part2(input))
}
