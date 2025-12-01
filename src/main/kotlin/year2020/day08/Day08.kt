package year2020.day08

import check
import readInput

fun part1(input: List<String>): Int {
    var acc = 0
    var current = 0
    val visited = mutableSetOf<Int>()
    while (!visited.contains(current)) {
        visited.add(current)
        if (input[current].startsWith("acc")) {
            acc += input[current].split(" ")[1].toInt()
            current += 1
        } else if (input[current].startsWith("jmp")) {
            current += input[current].split(" ")[1].toInt()
        } else {
            current += 1
        }
    }
    return acc
}

fun part2(input: List<String>): Int {
    var paths = listOf(Path(0, 0, mutableSetOf(), false))
    while (paths.none { it.current == input.size }) {
        paths = paths.flatMap { path ->
            val updatedPath = path.copy(visited = path.visited.plus(path.current))
            if (input[updatedPath.current].startsWith("acc")) {
                listOf(
                    updatedPath.copy(
                        acc = updatedPath.acc + input[updatedPath.current].split(" ")[1].toInt(),
                        current = updatedPath.current + 1
                    )
                )
            } else if (input[updatedPath.current].startsWith("jmp")) {
                if (updatedPath.changed) {
                    listOf(
                        updatedPath.copy(current = updatedPath.current + input[updatedPath.current].split(" ")[1].toInt())
                    )
                } else {
                    listOf(
                        updatedPath.copy(current = updatedPath.current + input[updatedPath.current].split(" ")[1].toInt()),
                        updatedPath.copy(current = updatedPath.current + 1, changed = true)
                    )
                }
            } else {
                if (updatedPath.changed) {
                    listOf(
                        updatedPath.copy(current = updatedPath.current + 1)
                    )
                } else {
                    listOf(
                        updatedPath.copy(current = updatedPath.current + input[updatedPath.current].split(" ")[1].toInt(), changed = true),
                        updatedPath.copy(current = updatedPath.current + 1)
                    )
                }
            }.filter { !it.visited.contains(it.current) }
        }
    }
    return paths.first { it.current == input.size }.acc
}

data class Path(val acc: Int, val current: Int, val visited: Set<Int>, val changed: Boolean)

fun main() {
    val testInput = readInput("src/main/kotlin/year2020/day08/Day08_test")
    check(part1(testInput), 5)
    check(part2(testInput), 8)

    val input = readInput("src/main/kotlin/year2020/day08/Day08")
    println(part1(input))
    println(part2(input))
}
