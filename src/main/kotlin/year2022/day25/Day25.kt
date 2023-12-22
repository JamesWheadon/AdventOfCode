package year2022.day25

import readInput
import kotlin.math.pow

val conversion = mapOf('=' to -2, '-' to -1, '0' to 0, '1' to 1, '2' to 2)

fun snafuToDecimal(snafu: String): Long {
    var decimal = 0L
    snafu.reversed().forEachIndexed { index, c ->
        decimal += 5.0.pow(index).toLong() * conversion[c]!!
    }
    return decimal
}

fun decimalToSNAFU(sum: Long): String {
    var snafu = ""
    var remaining = sum
    var index = 1
    while (remaining != 0L) {
        val digit = (remaining % 5.0.pow(index).toLong()) / 5.0.pow(index - 1).toLong()
        if (digit <= 2) {
            snafu += digit.toString()
            remaining -= digit * 5.0.pow(index - 1).toLong()
        } else {
            if (digit == 3L) {
                snafu += "="
                remaining += 5.0.pow(index - 1).toLong() * 2
            } else {
                snafu += "-"
                remaining += 5.0.pow(index - 1).toLong()
            }
        }
        index++
    }
    return snafu.reversed()
}

fun part1(input: List<String>): String {
    return decimalToSNAFU(input.sumOf { snafuToDecimal(it) })
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("src/main/kotlin/year2022/day25/Day25_test")
    check(part1(testInput) == "2=-1=0")

    val input = readInput("src/main/kotlin/year2022/day25/Day25")
    println(part1(input))
}
