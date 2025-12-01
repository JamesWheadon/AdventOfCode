package year2020.day23

import check
import readInput

fun part1(input: Int): Int {
    val cups = input.toString().toCharArray().asList().map { it.digitToInt() }.toMutableList()
    for (i in 1..100) {
        val remove = cups.subList(1, 4).toList().also { cups.removeAll(it) }
        var next = cups.first() - 1
        while (!cups.contains(next)) {
            next = (10 + next - 1) % 10
        }
        cups.addAll(cups.indexOf(next) + 1, remove)
        cups.add(cups.removeFirst())
    }
    val oneIndex = cups.indexOf(1)
    return (cups.subList(oneIndex + 1, cups.size) + cups.subList(0, oneIndex)).joinToString("").toInt()
}

fun part2(input: Int): Long {
    val cups = (input.toString().toCharArray().asList().map { it.digitToInt() } + (10 .. 1000000)).map { Cup(it.toLong(), null) }
    cups.zipWithNext().forEach { it.first.next = it.second }
    cups.last().next = cups.first()
    val cupMap = cups.associateBy { it.value }

    var current = cups.first()
    for (i in 1..10000000) {
        val next = current.next!!
        val secondNext = current.next!!.next!!
        val thirdNext = current.next!!.next!!.next!!
        var destCupValue = if (current.value == 1L) 1000000L else current.value - 1
        while (destCupValue == next.value || destCupValue == secondNext.value || destCupValue == thirdNext.value) {
            destCupValue = if (destCupValue == 1L) 1000000L else destCupValue - 1
        }
        current.next = thirdNext.next
        val destCup = cupMap[destCupValue]!!
        thirdNext.next = destCup.next
        destCup.next = next
        current = current.next!!
    }

    return cupMap[1]!!.next!!.value * cupMap[1]!!.next!!.next!!.value
}

data class Cup(val value: Long, var next: Cup?)

fun main() {
    val testInput = readInput("src/main/kotlin/year2020/day23/Day23_test").first().toInt()
    check(part1(testInput), 67384529)
    check(part2(testInput), 149245887792)

    val input = readInput("src/main/kotlin/year2020/day23/Day23").first().toInt()
    println(part1(input))
    println(part2(input))
}
