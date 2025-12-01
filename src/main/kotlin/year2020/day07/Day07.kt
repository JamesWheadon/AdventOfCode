package year2020.day07

import check
import readInput

fun part1(input: List<String>): Int {
    val outerBags = mutableSetOf<String>()
    var previousFound = -1
    while (previousFound != outerBags.size) {
        previousFound = outerBags.size
        input.filter {
            !outerBags.contains(it.split(" bags contain ")[0])
        }.filter {
            val contents = it.split(" bags contain ")[1]
            contents.contains("shiny gold") || outerBags.any { outer -> contents.contains(outer) }
        }.forEach {
            outerBags.add(it.split(" bags contain ")[0])
        }
    }
    return outerBags.size
}

fun part2(input: List<String>): Int {
    val bags = mutableMapOf("shiny gold" to 1)
    val bagRegex = "([1-9] [a-z]+ [a-z]+) bag".toRegex()
    var total = 0
    while (bags.isNotEmpty()) {
        println(bags)
        val nextBags = mutableMapOf<String, Int>()
        bags.forEach { bag ->
            bagRegex.findAll(input.first { it.startsWith(bag.key) })
                .forEach { matchResult ->
                    matchResult.groups.drop(1).forEach {
                        val numBags = bag.value * (it?.value?.filter { char -> char.isDigit() }?.toInt() ?: 1)
                        val bag1 = it?.value?.substringAfter(" ")?.substringBeforeLast(" bag") ?: ""
                        total += numBags
                        nextBags[bag1] = nextBags.getOrDefault(bag1, 0) + numBags
                    }
                }
        }
        bags.clear()
        bags.putAll(nextBags)
    }
    return total
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2020/day07/Day07_test")
    check(part1(testInput), 4)
    check(part2(testInput), 32)

    val input = readInput("src/main/kotlin/year2020/day07/Day07")
    println(part1(input))
    println(part2(input))
}
