package year2024.day21

import readInput

val keypadDirs = mapOf(
    'A' to mapOf(
        'A' to "A",
        '0' to "<A",
        '1' to "^<<A",
        '2' to "<^A",
        '3' to "^A",
        '4' to "^^<<A",
        '5' to "<^^A",
        '6' to "^^A",
        '7' to "^^^<<A",
        '8' to "<^^^A",
        '9' to "^^^A"
    ),
    '0' to mapOf(
        'A' to ">A",
        '0' to "A",
        '1' to "^<A",
        '2' to "^A",
        '3' to "^>A",
        '4' to "^^<A",
        '5' to "^^A",
        '6' to "^^>A",
        '7' to "^^^<A",
        '8' to "^^^A",
        '9' to "^^^>A"
    ),
    '1' to mapOf(
        'A' to ">>vA",
        '0' to ">vA",
        '1' to "A",
        '2' to ">A",
        '3' to ">>A",
        '4' to "^A",
        '5' to "^>A",
        '6' to "^>>A",
        '7' to "^^A",
        '8' to "^^>A",
        '9' to "^^>>A"
    ),
    '2' to mapOf(
        'A' to "v>A",
        '0' to "vA",
        '1' to "<A",
        '2' to "A",
        '3' to ">A",
        '4' to "<^A",
        '5' to "^A",
        '6' to "^>A",
        '7' to "<^^A",
        '8' to "^^A",
        '9' to "^^>A"
    ),
    '3' to mapOf(
        'A' to "vA",
        '0' to "<vA",
        '1' to "<<A",
        '2' to "<A",
        '3' to "A",
        '4' to "<<^A",
        '5' to "<^A",
        '6' to "^A",
        '7' to "<<^^A",
        '8' to "<^^A",
        '9' to "^^A"
    ),
    '4' to mapOf(
        'A' to ">>vvA",
        '0' to ">vvA",
        '1' to "vA",
        '2' to ">vA",
        '3' to ">>vA",
        '4' to "A",
        '5' to ">A",
        '6' to ">>A",
        '7' to "^A",
        '8' to "^>A",
        '9' to "^>>A"
    ),
    '5' to mapOf(
        'A' to "vv>A",
        '0' to "vvA",
        '1' to "<vA",
        '2' to "vA",
        '3' to "v>A",
        '4' to "<A",
        '5' to "A",
        '6' to ">A",
        '7' to "<^A",
        '8' to "^A",
        '9' to "^>A"
    ),
    '6' to mapOf(
        'A' to "vvA",
        '0' to "<vvA",
        '1' to "<<vA",
        '2' to "<vA",
        '3' to "vA",
        '4' to "<<A",
        '5' to "<A",
        '6' to "A",
        '7' to "<<^A",
        '8' to "<^A",
        '9' to "^A"
    ),
    '7' to mapOf(
        'A' to ">>vvA",
        '0' to ">vvA",
        '1' to "vvA",
        '2' to "vv>A",
        '3' to "vv>>A",
        '4' to "vA",
        '5' to "v>A",
        '6' to "v>>A",
        '7' to "A",
        '8' to ">A",
        '9' to ">>A"
    ),
    '8' to mapOf(
        'A' to "vvv>A",
        '0' to "vvvA",
        '1' to "<vvA",
        '2' to "vvA",
        '3' to "vv>A",
        '4' to "<vA",
        '5' to "vA",
        '6' to "v>A",
        '7' to "<A",
        '8' to "A",
        '9' to ">A"
    ),
    '9' to mapOf(
        'A' to "vvvA",
        '0' to "<vvvA",
        '1' to "<<vvA",
        '2' to "<vvA",
        '3' to "vvA",
        '4' to "<<vvA",
        '5' to "<vvA",
        '6' to "vA",
        '7' to "<<A",
        '8' to "<A",
        '9' to "A"
    )
)
val numpadDirs = mapOf(
    'A' to mapOf('A' to "A", '^' to "<A", '<' to "v<<A", 'v' to "<vA", '>' to "vA"),
    '^' to mapOf('A' to ">A", '^' to "A", '<' to "v<A", 'v' to "vA", '>' to "v>A"),
    '<' to mapOf('A' to ">>^A", '^' to ">^A", '<' to "A", 'v' to ">A", '>' to ">>A"),
    'v' to mapOf('A' to "^>A", '^' to "^A", '<' to "<A", 'v' to "A", '>' to ">A"),
    '>' to mapOf('A' to "^A", '^' to "<^A", '<' to "<<A", 'v' to "<A", '>' to "A")
)

fun bothParts(input: List<String>, robots: Int = 2): Long {
    return input.sumOf { code ->
        var possibleKeypad = ("A$code").windowed(2).map { move ->
            keypadDirs[move[0]]!![move[1]]!!
        }.groupingBy { it }.eachCount().map { it.key to it.value.toLong() }.toMap()
        for (i in 1..robots) {
            val nextRobot = mutableMapOf<String, Long>()
            possibleKeypad.forEach { (move, count) ->
                "A$move".windowed(2).forEach { step ->
                    val stepResult = numpadDirs[step[0]]!![step[1]]!!
                    if (stepResult in nextRobot) {
                        nextRobot[stepResult] = nextRobot[stepResult]!! + count
                    } else {
                        nextRobot[stepResult] = count
                    }
                }
            }
            possibleKeypad = nextRobot
        }
        possibleKeypad.map { (pattern, count) ->
            pattern.length * count
        }.sum() * code.filter { it.isDigit() }.toLong()
    }
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2024/day21/Day21_test")
    check(bothParts(testInput) == 126384L)

    val input = readInput("src/main/kotlin/year2024/day21/Day21")
    println(bothParts(input))
    println(bothParts(input, 25))
}
