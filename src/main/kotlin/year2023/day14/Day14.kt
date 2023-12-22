package year2023.day14

import readInput

fun part1(input: List<String>): Int {
    val columns = transpose(input.map { it.toList() })
    return columns.sumOf { column ->
        moveRocksToStart(column).mapIndexed { index, c -> if (c == 'O') column.size - index else 0 }.sum()
    }
}

fun part2(input: List<String>): Int {
    val finishCycles = mutableListOf(input.map { it.toList() })
    for (i in 1..1000000000) {
        val start = finishCycles.last()
        val north = transpose(transpose(start).map { moveRocksToStart(it) })
        val west = north.map { moveRocksToStart(it) }
        val south = transpose(transpose(west).map { moveRocksToEnd(it) })
        val east = south.map { moveRocksToEnd(it) }
        val repeat = finishCycles.firstOrNull { finished -> finished.zip(east).all { it.first == it.second } }
        if (repeat != null) {
            val repeatIndex = finishCycles.indexOf(repeat)
            val possible = finishCycles.subList(repeatIndex, finishCycles.size)
            val final = possible[(1000000000 - i) % possible.size]
            return transpose(final).sumOf { column ->
                column.mapIndexed { index, c -> if (c == 'O') column.size - index else 0 }.sum()
            }
        }
        finishCycles.add(east)
    }
    return 0
}

private fun transpose(input: List<List<Char>>) =
    (0..<input[0].size).map { columnNum -> input.map { it[columnNum] } }

fun moveRocksToEnd(column: List<Char>): List<Char> {
    var start = column
    while (true) {
        val next = start.toMutableList()
        for (i in 0..<next.size - 1) {
            if (next[i] == 'O' && next[i + 1] == '.') {
                next[i] = '.'
                next[i + 1] = 'O'
            }
        }
        if (next == start) {
            break
        }
        start = next
    }
    return start
}

private fun moveRocksToStart(column: List<Char>): List<Char> {
    var start = column
    while (true) {
        val next = start.toMutableList()
        for (i in 0..<next.size - 1) {
            if (next[i] == '.' && next[i + 1] == 'O') {
                next[i] = 'O'
                next[i + 1] = '.'
            }
        }
        if (next == start) {
            break
        }
        start = next
    }
    return start
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2023/day14/Day14_test")
    check(part1(testInput) == 136)
    check(part2(testInput) == 64)

    val input = readInput("src/main/kotlin/year2023/day14/Day14")
    println(part1(input))
    println(part2(input))
}
