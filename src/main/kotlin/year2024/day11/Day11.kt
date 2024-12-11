package year2024.day11

import readInput

fun part1(input: List<String>): Int {
    var row = input[0].split(" ").map { it.toLong() }
    for (i in 1..25) {
        row = row.flatMap { rock ->
            if (rock == 0L) {
                listOf(1)
            } else if (rock.toString().length % 2 == 0) {
                listOf(
                    rock.toString().substring(0, rock.toString().length / 2).toLong(),
                    rock.toString().substring(rock.toString().length / 2).toLong()
                )
            } else {
                listOf(rock * 2024)
            }
        }
    }
    return row.size
}

fun part2(input: List<String>): Long {
    var row = input[0].split(" ").map { it.toLong() }.groupingBy { it }.eachCount().mapValues { it.value.toLong() }
    for (i in 1..75) {
        val next = mutableMapOf<Long, Long>()
        row.forEach { (t, u) ->
            if (t == 0L) {
                if (1L in next.keys) {
                    next[1L] = next[1L]!! + u
                } else {
                    next[1L] = u
                }
            } else if (t.toString().length % 2 == 0) {
                val left = t.toString().substring(0, t.toString().length / 2).toLong()
                val right = t.toString().substring(t.toString().length / 2).toLong()
                if (left in next.keys) {
                    next[left] = next[left]!! + u
                } else {
                    next[left] = u
                }
                if (right in next.keys) {
                    next[right] = next[right]!! + u
                } else {
                    next[right] = u
                }
            } else {
                if (t * 2024 in next.keys) {
                    next[t * 2024] = next[t * 2024]!! + u
                } else {
                    next[t * 2024] = u
                }
            }
        }
        row = next
    }
    return row.values.sum()
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2024/day11/Day11_test")
    check(part1(testInput) == 55312)
    check(part2(testInput) == 65601038650482L)

    val input = readInput("src/main/kotlin/year2024/day11/Day11")
    println(part1(input))
    println(part2(input))
}
