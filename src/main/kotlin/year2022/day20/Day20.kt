package year2022.day20

import readInput

fun part1(input: List<String>): Int {
    val movedValues = mutableListOf<EncryptedInt>()
    val values = input.map { EncryptedInt(it.toInt()) }.toMutableList()
    var startIndex = 0
    while (movedValues.size < values.size) {
        if (!movedValues.contains(values[startIndex])) {
            val value = values.removeAt(startIndex)
            if (value.value < 0) {
                lessThanZero(startIndex, value, values)
            } else {
                greaterThanZero(startIndex, value, values)
            }
            startIndex = 0
            movedValues.add(value)
        } else {
            startIndex++
        }
    }
    val index = values.indexOfFirst { it.value == 0 }
    return getScore(index, values.map { EncryptedLong(it.value.toLong()) }.toMutableList()).toInt()
}

private fun greaterThanZero(
    startIndex: Int,
    value: EncryptedInt,
    values: MutableList<EncryptedInt>,
) {
    if (startIndex + value.value > values.size) {
        var newIndex = startIndex + value.value - values.size
        while (newIndex > values.size) {
            newIndex -= values.size
        }
        values.add(newIndex, value)
    } else {
        values.add(startIndex + value.value, value)
    }
}

private fun lessThanZero(
    startIndex: Int,
    value: EncryptedInt,
    values: MutableList<EncryptedInt>,
) {
    if (startIndex + value.value <= 0) {
        var newIndex = startIndex + values.size + value.value
        while (newIndex < 0) {
            newIndex += values.size
        }
        values.add(newIndex, value)
    } else {
        values.add(startIndex + value.value, value)
    }
}

private fun getScore(
    index: Int,
    values: MutableList<EncryptedLong>,
): Long {
    var currentIndex = index
    var score = 0L
    var count = 0
    while (count <= 3000) {
        count++
        currentIndex++
        if (currentIndex == values.size) {
            currentIndex = 0
        }
        if (count % 1000 == 0) {
            score += values[currentIndex].value
        }
    }
    return score
}

fun part2(input: List<String>): Long {
    val values = input.map { EncryptedLong(it.toLong() * 811589153L) }.toMutableList()
    val movingValues = mutableListOf<EncryptedLong>()
    movingValues.addAll(values)
    for (i in 0..<10) {
        println(i)
        val movedValues = mutableListOf<EncryptedLong>()
        var startIndex = 0
        while (movedValues.size < values.size) {
            val value = movingValues.find { it == values[startIndex] }!!
            val valueIndex = movingValues.indexOf(value)
            if (!movedValues.contains(value)) {
                movingValues.remove(value)
                if (value.value < 0) {
                    var newIndex = valueIndex + value.value
                    newIndex -= (newIndex / movingValues.size - 1) * movingValues.size
                    movingValues.add(newIndex.toInt(), value)
                } else {
                    var newIndex = valueIndex + value.value
                    newIndex -= (newIndex / movingValues.size) * movingValues.size
                    movingValues.add(newIndex.toInt(), value)
                }
                startIndex = 0
                movedValues.add(value)
            } else {
                startIndex++
            }
        }
    }
    val index = movingValues.indexOfFirst { it.value == 0L }
    return getScore(index, movingValues)
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("src/main/kotlin/year2022/day20/Day20_test")
    check(part1(testInput) == 3)
    check(part2(testInput) == 1623178306L)

    val input = readInput("src/main/kotlin/year2022/day20/Day20")
    println(part1(input))
    println(part2(input))
}

class EncryptedInt(val value: Int)

class EncryptedLong(val value: Long)
