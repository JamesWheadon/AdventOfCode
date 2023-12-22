package year2022.day11

import readInput

fun part1(input: List<String>): Int {
    val monkeys = MutableList((input.size + 1) / 7) { Monkey() }
    monkeyBusiness(input, monkeys)
    for (i in 1..<21) {
        monkeys.forEach { monkey ->
            monkey.items.forEach { item ->
                item.value = monkey.operation(item.value)
                item.value = item.value / 3
                if (monkey.test(item.value)) {
                    monkey.ifTrue.items.add(item)
                } else {
                    monkey.ifFalse.items.add(item)
                }
                monkey.inspected++
            }
            monkey.items = mutableListOf()
        }
    }
    return monkeys.maxOf { it.inspected }.toInt() * monkeys.map { it.inspected }
        .filter { monkey -> monkey != monkeys.maxOf { it.inspected } }.maxOrNull()!!.toInt()
}

fun part2(input: List<String>): Long {
    val monkeys = MutableList((input.size + 1) / 7) { Monkey() }
    val lCM = monkeyBusiness(input, monkeys)
    for (i in 1..<10001) {
        monkeys.forEach { monkey ->
            monkey.items.forEach { item ->
                item.value = monkey.operation(item.value)
                if (item.value >= lCM) {
                    item.value = item.value % lCM
                }
                if (monkey.test(item.value)) {
                    monkey.ifTrue.items.add(item)
                } else {
                    monkey.ifFalse.items.add(item)
                }
                monkey.inspected++
            }
            monkey.items = mutableListOf()
        }
    }
    return monkeys.maxOf { it.inspected } * monkeys.map { it.inspected }
        .filter { monkey -> monkey != monkeys.maxOf { it.inspected } }.maxOrNull()!!
}

private fun monkeyBusiness(
    input: List<String>,
    monkeys: MutableList<Monkey>,
): Int {
    var lCM = 1
    input.chunked(7).forEachIndexed { index, monkeyInfo ->
        monkeyInfo[1].filter { it.isDigit() || it == ',' }.split(",")
            .forEach { monkeys[index].items.add(Item(it.toLong())) }
        val operation = monkeyInfo[2].split(" = ")[1]
        if (operation.split("*").size == 2) {
            if (operation.split(" * ")[1] == "old") {
                monkeys[index].operation = { input -> input * input }
            } else {
                monkeys[index].operation = { input -> input * operation.split(" * ")[1].toInt() }
            }
        } else if (operation.split("+").size == 2) {
            monkeys[index].operation = { input -> input + operation.split(" + ")[1].toInt() }
        }
        lCM *= monkeyInfo[3].split(" ").last().toInt()
        monkeys[index].test = { input -> input % monkeyInfo[3].split(" ").last().toLong() == 0L }
        monkeys[index].ifTrue = monkeys[monkeyInfo[4].filter { it.isDigit() }.toInt()]
        monkeys[index].ifFalse = monkeys[monkeyInfo[5].filter { it.isDigit() }.toInt()]
    }
    return lCM
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("src/main/kotlin/year2022/day11/Day11_test")
    check(part1(testInput) == 10605)
    check(part2(testInput) == 2713310158)

    val input = readInput("src/main/kotlin/year2022/day11/Day11")
    println(part1(input))
    println(part2(input))
}

class Monkey {
    var items = mutableListOf<Item>()
    var operation: (Long) -> Long = { input -> input }
    var test: (Long) -> Boolean = { input -> input == 0L }
    lateinit var ifTrue: Monkey
    lateinit var ifFalse: Monkey
    var inspected = 0L
}

data class Item(var value: Long)
