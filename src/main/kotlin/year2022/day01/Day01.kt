package year2022.day01

import readInput

fun getElfCalories(input: List<String>): MutableList<Int> {
    val calories = input.map { calories -> if (calories != "") calories.toInt() else 0 }
    val elfCalories = mutableListOf<Int>()
    var elfTotal = 0
    calories.forEach {
        if (it == 0) {
            elfCalories.add(elfTotal)
            elfTotal = 0
        } else {
            elfTotal += it
        }
    }
    elfCalories.add(elfTotal)
    return elfCalories
}

fun part1(input: List<String>): Int {
    val elfCalories = getElfCalories(input)
    return elfCalories.maxOrNull()!!
}

fun part2(input: List<String>): Int {
    val elfCalories = getElfCalories(input)
    return elfCalories.sortedDescending().subList(0, 3).sum()
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("src/main/kotlin/year2022/day01/Day01_test")
    check(part1(testInput) == 24000)
    check(part2(testInput) == 45000)

    val input = readInput("src/main/kotlin/year2022/day01/Day01")
    println(part1(input))
    println(part2(input))
}
