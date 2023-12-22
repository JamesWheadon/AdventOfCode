package year2021.day06

import readInput

fun part1(input: List<String>): Int {
    var ages = input[0].map { it.digitToIntOrNull() }.filterNotNull().toMutableList()
    for (i in 0..79) {
        val newFish = ages.count { it == 0 }
        val iterator = ages.listIterator()
        while (iterator.hasNext()) {
            val value = iterator.next()
            if (value == 0) {
                iterator.set(6)
            } else {
                iterator.set(value - 1)
            }
        }
        ages = ((ages + MutableList(newFish) { 8 }) as MutableList<Int>)
    }
    return ages.size
}

fun part2(input: List<String>): Long {
    val ages = input[0].map { it.digitToIntOrNull() }.filterNotNull().toMutableList()
    val numberEachAge = MutableList(9) { 0 }
    ages.forEach { numberEachAge[it] += 1 }
    val numberEachAgeLong = numberEachAge.map { it.toLong() }.toMutableList()
    for (i in 0..255) {
        val newFish = numberEachAgeLong.removeFirst()
        numberEachAgeLong[6] += newFish
        numberEachAgeLong.add(newFish)
    }
    return numberEachAgeLong.sum()
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2021/day06/Day06_test")
    check(part1(testInput) == 5934)
    check(part2(testInput) == 26984457539)

    val input = readInput("src/main/kotlin/year2021/day06/Day06")
    println(part1(input))
    println(part2(input))
}
