package year2020.day18

import check
import readInput

fun part1(input: List<String>): Long {
    val innerCalc = "\\([0-9+* ]+\\)".toRegex()
    return input.sumOf {
        var calculation = it
        while (calculation.contains("(")) {
            innerCalc.find(calculation)?.groups?.get(0)?.let { group ->
                calculation =
                    calculation.replace(group.value, answer(group.value.removePrefix("(").removeSuffix(")")).toString())
            }
        }
        answer(calculation)
    }
}

fun part2(input: List<String>): Long {
    val addition = "[0-9]+ \\+ [0-9]+".toRegex()
    val innerCalc = "\\([0-9*+ ]+\\)".toRegex()
    return input.sumOf {
        var calculation = it
        while (calculation.contains(addition) || calculation.contains(innerCalc)) {
            addition.find(calculation)?.groups?.get(0)?.let { group ->
                calculation = calculation.replace(group.value, answer(group.value).toString())
            } ?: run {
                innerCalc.find(calculation)?.groups?.get(0)?.let { group ->
                    calculation =
                        calculation.replace(
                            group.value,
                            answer(group.value.removePrefix("(").removeSuffix(")")).toString()
                        )
                }
            }
        }
        answer(calculation)
    }
}

fun answer(calc: String): Long =
    calc.replace("* ", "*").replace("+ ", "+").split(" ").fold(0L) { acc, s ->
        if (s.startsWith("*")) {
            acc * s.filter { it.isDigit() }.toLong()
        } else {
            acc + s.filter { it.isDigit() }.toLong()
        }
    }

fun main() {
    val testInput = readInput("src/main/kotlin/year2020/day18/Day18_test")
    check(part1(testInput), 13632)
    check(part2(testInput), 23340)

    val input = readInput("src/main/kotlin/year2020/day18/Day18")
    println(part1(input))
    println(part2(input))
}
