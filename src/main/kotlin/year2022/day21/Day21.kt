package year2022.day21

import readInput

fun part1(input: List<String>): Long {
    val (monkeys, root) = getMonkeysAndRoot(input)
    while (root.number == null) {
        monkeys.filter { it.number == null && it.firstMonkey!!.number != null && it.secondMonkey!!.number != null }
            .forEach {
                it.calculate()
            }
    }
    return root.number!!
}

fun part2(input: List<String>): Long {
    val (monkeys, root) = getMonkeysAndRoot(input)
    root.operation = '='
    val me = monkeys.first { it.name == "humn" }
    me.number = null
    while (root.firstMonkey!!.number == null && root.secondMonkey!!.number == null) {
        monkeys.filter { it != me && it.number == null && it.firstMonkey!!.number != null && it.secondMonkey!!.number != null }
            .forEach {
                it.calculate()
            }
    }
    var unknownYell: Monkey
    if (root.firstMonkey!!.number != null) {
        root.secondMonkey!!.number = root.firstMonkey!!.number
        unknownYell = root.secondMonkey!!
    } else {
        root.firstMonkey!!.number = root.secondMonkey!!.number
        unknownYell = root.firstMonkey!!
    }
    while (me.number == null) {
        unknownYell = unknownYell.calculateYells()
    }
    return me.number!!
}

private fun getMonkeysAndRoot(input: List<String>): Pair<List<Monkey>, Monkey> {
    val monkeys = input.map { Monkey(it.split(":")[0]) }
    input.forEach { monkeyInfo ->
        val monkey = monkeys.first { it.name == monkeyInfo.split(":")[0] }
        val yell = monkeyInfo.split(": ")[1]
        if (yell.filter { it.isDigit() }.length == yell.length) {
            monkey.number = yell.toLong()
        } else {
            val calculation = yell.split(" ")
            monkey.firstMonkey = monkeys.first { it.name == calculation[0] }
            monkey.secondMonkey = monkeys.first { it.name == calculation[2] }
            monkey.operation = calculation[1][0]
        }
    }
    val root = monkeys.first { it.name == "root" }
    return Pair(monkeys, root)
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("src/main/kotlin/year2022/day21/Day21_test")
    check(part1(testInput) == 152L)
    check(part2(testInput) == 301L)

    val input = readInput("src/main/kotlin/year2022/day21/Day21")
    println(part1(input))
    println(part2(input))
}

class Monkey(val name: String) {

    var number: Long? = null
    var firstMonkey: Monkey? = null
    var secondMonkey: Monkey? = null
    var operation: Char? = null

    fun calculate() {
        if (firstMonkey != null && secondMonkey != null && firstMonkey!!.number != null && secondMonkey!!.number != null) {
            number = when (operation) {
                '+' -> {
                    firstMonkey!!.number!! + secondMonkey!!.number!!
                }

                '-' -> {
                    firstMonkey!!.number!! - secondMonkey!!.number!!
                }

                '*' -> {
                    firstMonkey!!.number!! * secondMonkey!!.number!!
                }

                else -> {
                    firstMonkey!!.number!! / secondMonkey!!.number!!
                }
            }
        }
    }

    fun calculateYells(): Monkey {
        val unknown = if (firstMonkey!!.number == null) {
            firstMonkey!!
        } else {
            secondMonkey!!
        }
        when (operation) {
            '+' -> {
                if (unknown == firstMonkey) {
                    unknown.number = number!! - secondMonkey!!.number!!
                } else {
                    unknown.number = number!! - firstMonkey!!.number!!
                }
            }

            '*' -> {
                if (unknown == firstMonkey) {
                    unknown.number = number!! / secondMonkey!!.number!!
                } else {
                    unknown.number = number!! / firstMonkey!!.number!!
                }
            }

            '-' -> {
                if (unknown == firstMonkey) {
                    unknown.number = number!! + secondMonkey!!.number!!
                } else {
                    unknown.number = firstMonkey!!.number!! - number!!
                }
            }

            else -> {
                if (unknown == firstMonkey) {
                    unknown.number = number!! * secondMonkey!!.number!!
                } else {
                    unknown.number = firstMonkey!!.number!! / number!!
                }
            }
        }
        return unknown
    }
}
