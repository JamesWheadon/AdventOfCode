package year2020.day14

import check
import readInput

fun part1(input: List<String>): Long {
    val memory = mutableMapOf<Int, Long>()
    var mask = ""
    input.forEach { line ->
        if (line.startsWith("mask")) {
            mask = line.split(" = ")[1]
        } else {
            val address = line.split(" = ")[0].filter { it.isDigit() }.toInt()
            val number = line.split(" = ")[1].toLong()
            memory[address] = number.toString(2).padStart(36, '0').zip(mask).map { chars ->
                if (chars.second != 'X') {
                    chars.second
                } else {
                    chars.first
                }
            }.joinToString(separator = "").toLong(2)
        }
    }
    return memory.values.sum()
}

fun part2(input: List<String>): Long {
    val memory = mutableMapOf<Long, Long>()
    var mask = ""
    input.forEach { line ->
        if (line.startsWith("mask")) {
            mask = line.split(" = ")[1]
        } else {
            val address = line.split(" = ")[0].filter { it.isDigit() }.toInt().toString(2).padStart(36, '0')
            val number = line.split(" = ")[1].toLong()
            var maskedAddresses = listOf("")
            mask.zip(address).forEach { c ->
                maskedAddresses = when (c.first) {
                    '1' -> maskedAddresses.map { it + '1' }
                    '0' -> maskedAddresses.map { it + c.second }
                    else -> maskedAddresses.map { it + '1' } + maskedAddresses.map { it + '0' }
                }
            }
            maskedAddresses.forEach {
                memory[it.toLong(2)] = number
            }
        }
    }
    return memory.values.sum()
}

fun main() {
    check(part1(readInput("src/main/kotlin/year2020/day14/Day14_part1_test")), 165)
    check(part2(readInput("src/main/kotlin/year2020/day14/Day14_part2_test")), 208)

    val input = readInput("src/main/kotlin/year2020/day14/Day14")
    println(part1(input))
    println(part2(input))
}
