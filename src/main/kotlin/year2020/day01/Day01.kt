package year2020.day01

import readInput

fun part1(input: List<String>): Int {
    return input.mapIndexed { index, s ->
        val int = s.toInt()
        input.map { it.toInt() }.subList(index + 1, input.size).sumOf { f ->
            if (int + f == 2020) {
                int * f
            } else {
                0
            }
        }
    }.sum()
}

fun part2(input: List<String>): Int {
    input.forEachIndexed { index, s ->
        val first = s.toInt()
        input.subList(index + 1, input.size).forEachIndexed { i, s1 ->
            val second = s1.toInt()
            input.subList(i + 1, input.size).forEach { third ->
                if (first + second + third.toInt() == 2020) {
                    return first * second * third.toInt()
                }
            }
        }
    }
    return 0
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2020/day01/Day01_test")
    check(part1(testInput) == 514579)
    check(part2(testInput) == 241861950)

    val input = readInput("src/main/kotlin/year2020/day01/Day01")
    println(part1(input))
    println(part2(input))
}
