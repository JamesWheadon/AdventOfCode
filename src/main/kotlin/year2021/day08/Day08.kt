package year2021.day08

import readInput

fun part1(input: List<String>): Int {
    var total = 0
    input.forEach { line ->
        total += line.split(" | ")[1].split(" ")
            .filter { it.length == 2 || it.length == 3 || it.length == 4 || it.length == 7 }.size
    }
    return total
}

fun part2(input: List<String>): Int {
    var total = 0
    input.forEach { outputs ->
        val positions = MutableList(7) { Pair('x', 'x') }
        val decodedNumbers = MutableList(10) { "" }
        val allNumbers = outputs.split(" | ")[0].split(" ")
        val outputNumbers = outputs.split(" | ")[1].split(" ")
        decodedNumbers[1] = allNumbers.filter { it.length == 2 }[0]
        decodedNumbers[4] = allNumbers.filter { it.length == 4 }[0]
        decodedNumbers[7] = allNumbers.filter { it.length == 3 }[0]
        decodedNumbers[8] = allNumbers.filter { it.length == 7 }[0]

        positions[0] = Pair('a', decodedNumbers[7].filter { !decodedNumbers[1].contains(it) }[0])

        val twoThreeFive = allNumbers.filter { it.length == 5 }
        val middleSections = twoThreeFive[0].toList().intersect(twoThreeFive[1].toList().toSet())
            .intersect(twoThreeFive[2].toList().toSet())
        positions[3] = Pair('d', middleSections.filter { decodedNumbers[4].contains(it) }[0])
        positions[1] =
            Pair('b', decodedNumbers[4].filter { !decodedNumbers[1].contains(it) && it != positions[3].second }[0])

        decodedNumbers[5] = twoThreeFive.filter {
            it.contains(positions[0].second) && it.contains(positions[1].second) && it.contains(positions[3].second)
        }[0]
        val fG =
            decodedNumbers[5].filter { it != (positions[0].second) && it != (positions[1].second) && it != (positions[3].second) }
        positions[5] = Pair('f', fG.filter { decodedNumbers[1].contains(it) }[0])
        positions[6] = Pair('g', fG.filter { !decodedNumbers[1].contains(it) }[0])
        positions[2] = Pair(
            'c',
            decodedNumbers[4].filter { it != (positions[1].second) && it != (positions[3].second) && it != (positions[5].second) }[0],
        )

        decodedNumbers[3] =
            twoThreeFive.filter { it.contains(positions[2].second) && it.contains(positions[5].second) }[0]
        decodedNumbers[2] = twoThreeFive.filter { !decodedNumbers.contains(it) }[0]

        val oneSixNine = allNumbers.filter { it.length == 6 }
        decodedNumbers[0] = oneSixNine.filter { !it.contains(positions[3].second) }[0]
        decodedNumbers[6] = oneSixNine.filter { !it.contains(positions[2].second) }[0]
        decodedNumbers[9] = oneSixNine.filter { !decodedNumbers.contains(it) }[0]

        var number = ""
        outputNumbers.forEach {
            decodedNumbers.forEachIndexed { index, s ->
                if (s.toList().sortedDescending() == it.toList().sortedDescending()) {
                    number += index.toString()
                }
            }
        }
        total += number.toInt()
    }
    return total
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2021/day08/Day08_test")
    check(part1(testInput) == 26)
    check(part2(testInput) == 61229)

    val input = readInput("src/main/kotlin/year2021/day08/Day08")
    println(part1(input))
    println(part2(input))
}
