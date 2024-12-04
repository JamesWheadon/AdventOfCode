package year2024.day04

import readInput

fun part1(input: List<String>): Int {
    var t = 0
    val grid = List(3) { List(input[0].length + 6) { 'D' } } +  input.map {
        listOf('D', 'D', 'D').plus(it.toCharArray().toList()).plus( listOf('D', 'D', 'D'))
    } + List(3) { List(input[0].length + 6) { 'D' } }
    for (y in 3..grid.size - 4) {
        for (x in 3 .. grid[0].size - 4) {
            if (grid[y][x] == 'X') {
                for (i in -1..1) {
                    for (j in -1..1) {
                        if (grid[y + i][x + j] == 'M' && grid[y + 2 * i][x + 2 * j] == 'A' && grid[y + 3 * i][x + 3 * j] == 'S') {
                            t += 1
                        }
                    }
                }
            }
        }
    }
    return t
}

fun part2(input: List<String>): Int {
    var t = 0
    val grid = List(1) { List(input[0].length + 2) { 'D' } } +  input.map {
        listOf('D').plus(it.toCharArray().toList()).plus( listOf('D'))
    } + List(1) { List(input[0].length + 2) { 'D' } }
    for (y in 1..grid.size - 2) {
        for (x in 1 .. grid[0].size - 2) {
            if (grid[y][x] == 'A') {
                for (i in -1..1 step 2) {
                    if (grid[y + i][x + i] == 'M' && grid[y + i][x - i] == 'M' && grid[y - i][x + i] == 'S' && grid[y - i][x - i] == 'S') {
                        t += 1
                    }
                    if (grid[y + i][x + i] == 'M' && grid[y - i][x + i] == 'M' && grid[y + i][x - i] == 'S' && grid[y - i][x - i] == 'S') {
                        t += 1
                    }
                }
            }
        }
    }
    return t
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2024/day04/Day04_test")
    check(part1(testInput) == 18)
    check(part2(testInput) == 9)

    val input = readInput("src/main/kotlin/year2024/day04/Day04")
    println(part1(input))
    println(part2(input))
}
