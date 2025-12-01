package year2020.day03

import readInput

fun part1(input: List<String>): Int {
    var x = 3 % input[0].length
    var y = 1
    var trees = 0
    while (y < input.size) {
        if (input[y][x] == '#') {
            trees += 1
        }
        x = (x + 3) % input[0].length
        y += 1
    }
    return trees
}

/*
Right 1, down 1.
Right 3, down 1. (This is the slope you already checked.)
Right 5, down 1.
Right 7, down 1.
Right 1, down 2.
 */
fun part2(input: List<String>): Long {
    return listOf(1 to 1, 3 to 1, 5 to 1, 7 to 1, 1 to 2).map { (right, down) ->
        var x = right % input[0].length
        var y = down
        var trees = 0L
        while (y < input.size) {
            if (input[y][x] == '#') {
                trees += 1L
            }
            x = (x + right) % input[0].length
            y += down
        }
        trees
    }.reduce { acc, i -> acc * i }
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2020/day03/Day03_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 336L)

    val input = readInput("src/main/kotlin/year2020/day03/Day03")
    println(part1(input))
    println(part2(input))
}
