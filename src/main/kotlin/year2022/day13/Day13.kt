package year2022.day13

import readInput

fun String.addCharAtIndex(char: Char, index: Int) =
    StringBuilder(this).apply { insert(index, char) }.toString()

fun comparePackets(firstStatic: String, secondStatic: String): Boolean {
    var first = firstStatic
    var second = secondStatic
    var firstIndex = 0
    var secondIndex = 0
    while (firstIndex != first.length) {
        val firstChar = first[firstIndex]
        val secondChar = second[secondIndex]
        if (firstChar != secondChar) {
            println("issue is at index $firstIndex, chars are $firstChar and $secondChar")
            val end = checkEndCondition(firstChar, secondChar)
            if (end.first == 1) {
                return end.second
            }
            if (firstChar.isDigit() && secondChar.isDigit()) {
                return checkBothChars(firstChar, first, firstIndex, secondChar, second, secondIndex)
            }
            val triple = checkDigit(firstChar, first, firstIndex, secondIndex)
            first = triple.first
            firstIndex = triple.second
            secondIndex = triple.third
            val secondTriple = checkDigit(secondChar, second, secondIndex, firstIndex)
            second = secondTriple.first
            secondIndex = secondTriple.second
            firstIndex = secondTriple.third
        } else {
            firstIndex++
            secondIndex++
        }
    }
    return false
}

private fun checkEndCondition(firstChar: Char, secondChar: Char): Pair<Int, Boolean> {
    if (firstChar == ',' && secondChar == '0') {
        println("there is a 10 that wasn't well handled, valid packets")
        return Pair(1, true)
    }
    if (secondChar == ',' && firstChar == '0') {
        println("there is a 10 that wasn't well handled, invalid packets")
        return Pair(1, false)
    }
    if (firstChar == ']') {
        println("first list is shorter, valid packets")
        return Pair(1, true)
    }
    if (secondChar == ']') {
        println("first list is longer, invalid packets")
        return Pair(1, false)
    }
    return Pair(0, true)
}

private fun checkBothChars(
    firstChar: Char,
    first: String,
    firstIndex: Int,
    secondChar: Char,
    second: String,
    secondIndex: Int,
): Boolean {
    var firstNumber = firstChar.digitToInt()
    if (first[firstIndex + 1] == '0') {
        firstNumber = 10
    }
    var secondNumber = secondChar.digitToInt()
    if (second[secondIndex + 1] == '0') {
        secondNumber = 10
    }
    return if (firstNumber < secondNumber) {
        println("first value is lower than second, valid packets")
        true
    } else {
        println("first value is greater than second, invalid packets")
        false
    }
}

private fun checkDigit(
    firstChar: Char,
    first: String,
    firstIndex: Int,
    secondIndex: Int,
): Triple<String, Int, Int> {
    var first1 = first
    var firstIndex1 = firstIndex
    var secondIndex1 = secondIndex
    if (firstChar.isDigit()) {
        first1 = first1.addCharAtIndex('[', firstIndex1)
        firstIndex1++
        while (first1[firstIndex1].isDigit()) {
            firstIndex1++
        }
        first1 = first1.addCharAtIndex(']', firstIndex1)
        firstIndex1 = 0
        secondIndex1 = 0
    }
    return Triple(first1, firstIndex1, secondIndex1)
}

fun part1(input: List<String>): Int {
    var correct = 0
    input.filter { it != "" }.chunked(2).forEachIndexed { packetIndex, packets ->
        println(packets)
        if (comparePackets(packets[0], packets[1])) {
            correct += (packetIndex + 1)
        }
    }
    return correct
}

fun part2(input: List<String>): Int {
    val packets = input.filter { it != "" }.toMutableList()
    packets.addAll(listOf("[[2]]", "[[6]]"))
    var moving = true
    while (moving) {
        moving = false
        for (i in 0..<packets.size - 1) {
            if (!comparePackets(packets[i], packets[i + 1])) {
                val temp = packets[i + 1]
                packets[i + 1] = packets[i]
                packets[i] = temp
                moving = true
            }
        }
    }
    return (packets.indexOf("[[2]]") + 1) * (packets.indexOf("[[6]]") + 1)
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("src/main/kotlin/year2022/day13/Day13_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 140)

    val input = readInput("src/main/kotlin/year2022/day13/Day13")
    println(part1(input))
    println(part2(input))
}
