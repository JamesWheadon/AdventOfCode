package year2024.day17

import readInput
import kotlin.math.floor
import kotlin.math.pow

fun part1(input: List<String>): String {
    val out = mutableListOf<Int>()
    var a = input[0].filter(Char::isDigit).toInt()
    var b = input[1].filter(Char::isDigit).toInt()
    var c = input[2].filter(Char::isDigit).toInt()
    val program = input[4].filter(Char::isDigit).map { it.digitToInt() }
    var pointer = 0
    while (pointer in program.indices) {
        when (program[pointer]) {
            0 -> {
                val operand = combo(a, b, c, program[pointer + 1])
                a = floor(a / 2.0.pow(operand)).toInt()
                pointer += 2
            }

            1 -> {
                b = b xor program[pointer + 1]
                pointer += 2
            }

            2 -> {
                b = combo(a, b, c, program[pointer + 1]) % 8
                pointer += 2
            }

            3 -> {
                if (a == 0) {
                    pointer += 2
                } else {
                    pointer = program[pointer + 1]
                }
            }

            4 -> {
                b = b xor c
                pointer += 2
            }

            5 -> {
                out.add(combo(a, b, c, program[pointer + 1]) % 8)
                pointer += 2
            }

            6 -> {
                val operand = combo(a, b, c, program[pointer + 1])
                b = floor(a / 2.0.pow(operand)).toInt()
                pointer += 2
            }

            else -> {
                val operand = combo(a, b, c, program[pointer + 1])
                c = floor(a / 2.0.pow(operand)).toInt()
                pointer += 2
            }
        }
    }
    return out.joinToString(",")
}

fun part2(input: List<String>): Long {
    val program = input[4].filter(Char::isDigit).map { it.digitToInt() }
    val possAtIndex = mutableMapOf<Int, List<Int>>()
    val indexPointers = MutableList(program.size) { 0 }
    for (i in program.indices) {
        val range = if (i == 0) 1..7 else 0..7
        while (true) {
            val possible = mutableListOf<Int>()
            for (j in range) {
                var a = indexPointers.filterIndexed { index, _ -> index < i }
                    .mapIndexed { index, it -> possAtIndex[index]!![it] }.joinToString("").plus(j).toLong(8)
                var b = 0L
                var c = 0L
                val out = mutableListOf<Int>()
                var pointer = 0
                while (pointer in program.indices) {
                    when (program[pointer]) {
                        0 -> {
                            a = floor(a / 8.0).toLong()
                            pointer += 2
                        }

                        1 -> {
                            b = b xor program[pointer + 1].toLong()
                            pointer += 2
                        }

                        2 -> {
                            b = combo(a, b, c, program[pointer + 1]) % 8
                            pointer += 2
                        }

                        3 -> {
                            if (a == 0L) {
                                pointer += 2
                            } else {
                                pointer = program[pointer + 1]
                            }
                        }

                        4 -> {
                            b = b xor c
                            pointer += 2
                        }

                        5 -> {
                            out.add((combo(a, b, c, program[pointer + 1]) % 8).toInt())
                            pointer += 2
                        }

                        6 -> {
                            val operand = combo(a, b, c, program[pointer + 1])
                            b = floor(a / 2.0.pow(operand.toInt())).toLong()
                            pointer += 2
                        }

                        else -> {
                            val operand = combo(a, b, c, program[pointer + 1])
                            c = floor(a / 2.0.pow(operand.toInt())).toLong()
                            pointer += 2
                        }
                    }
                }
                if (out == program.subList(program.size - i - 1, program.size)) {
                    possible.add(j)
                }
            }
            if (possible.isEmpty()) {
                increment(possAtIndex, indexPointers, i - 1)
            } else {
                possAtIndex[i] = possible
                break
            }
        }
    }
    return indexPointers.mapIndexed { index, it -> possAtIndex[index]!![it] }.joinToString("").toLong(8)
}

fun increment(possAtIndex: MutableMap<Int, List<Int>>, indexPointers: MutableList<Int>, i: Int) {
    indexPointers[i] = if (indexPointers[i] == possAtIndex[i]!!.size - 1) {
        increment(possAtIndex, indexPointers, i - 1)
        0
    } else {
        indexPointers[i] + 1
    }
}

fun combo(a: Int, b: Int, c: Int, i: Int): Int {
    return when (i) {
        in 0..3 -> i
        4 -> a
        5 -> b
        6 -> c
        else -> 0
    }
}

fun combo(a: Long, b: Long, c: Long, i: Int): Long {
    return when (i) {
        in 0..3 -> i.toLong()
        4 -> a
        5 -> b
        6 -> c
        else -> 0
    }
}

fun main() {
    check(part1(readInput("src/main/kotlin/year2024/day17/Day17_test1")) == "4,6,3,5,6,3,5,2,1,0")
    check(part2(readInput("src/main/kotlin/year2024/day17/Day17_test2")) == 117440L)

    val input = readInput("src/main/kotlin/year2024/day17/Day17")
    println(part1(input))
    println(part2(input))
}
