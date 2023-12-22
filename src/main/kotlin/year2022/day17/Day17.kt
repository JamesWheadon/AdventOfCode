package year2022.day17

import readInput

val shapeNames = listOf("h", "c", "l", "v", "s")
val shapeWidths = mapOf("h" to 4, "c" to 3, "l" to 3, "v" to 1, "s" to 2)
val shapes = mapOf(
    "h" to listOf(listOf('#', '#', '#', '#')),
    "c" to listOf(listOf('.', '#', '.'), listOf('#', '#', '#'), listOf('.', '#', '.')),
    "l" to listOf(listOf('.', '.', '#'), listOf('.', '.', '#'), listOf('#', '#', '#')),
    "v" to listOf(listOf('#'), listOf('#'), listOf('#'), listOf('#')),
    "s" to listOf(listOf('#', '#'), listOf('#', '#')),
)

fun contact(grid: MutableList<MutableList<Char>>, shape: String, shapeLeft: Int, shapeAbove: Int): Boolean {
    if (shapeAbove > 0) {
        return false
    }
    var row = 0
    for (y in shapeAbove..0) {
        if (shapes[shape]!!.size > row) {
            val shapeRow = shapes[shape]!!.reversed()[row]
            var shapeRowIndex = 0
            grid.reversed()[-y].subList(shapeLeft, shapeLeft + shapeWidths[shape]!!).forEach { c ->
                if (c == shapeRow[shapeRowIndex] && c == '#') {
                    return true
                }
                shapeRowIndex++
            }
            row++
        } else {
            break
        }
    }
    return false
}

fun canMoveLeft(grid: MutableList<MutableList<Char>>, shape: String, shapeLeft: Int, shapeAbove: Int): Boolean {
    if (shapeLeft == 0) {
        return false
    }
    if (shapeAbove > 0) {
        return true
    }
    return !contact(grid, shape, shapeLeft - 1, shapeAbove)
}

fun canMoveRight(grid: MutableList<MutableList<Char>>, shape: String, shapeLeft: Int, shapeAbove: Int): Boolean {
    if (7 - shapeLeft - shapeWidths[shape]!! == 0) {
        return false
    }
    if (shapeAbove > 0) {
        return true
    }
    return !contact(grid, shape, shapeLeft + 1, shapeAbove)
}

fun addToGrid(grid: MutableList<MutableList<Char>>, shape: String, shapeLeft: Int, shapeAbove: Int) {
    var row = 0
    if (shapeAbove <= 0) {
        for (y in shapeAbove..0) {
            if (shapes[shape]!!.size > y - shapeAbove) {
                val shapeRow = shapes[shape]!!.reversed()[row]
                shapeRow.forEachIndexed { index, c ->
                    if (c == '#') {
                        grid.reversed()[-y][shapeLeft + index] = c
                    }
                }
                row++
            } else {
                break
            }
        }
    }
    while (row < shapes[shape]!!.size) {
        grid.add(MutableList(7) { '.' })
        val shapeRow = shapes[shape]!!.reversed()[row]
        shapeRow.forEachIndexed { index, c ->
            grid.reversed()[0][shapeLeft + index] = c
        }
        row++
    }
}

fun pruneGrid(grid: MutableList<MutableList<Char>>): Pair<MutableList<MutableList<Char>>, Int> {
    val covered = MutableList(7) { false }
    for (i in grid.reversed().indices) {
        grid.reversed()[i].forEachIndexed { index, c ->
            if (c == '#') {
                covered[index] = true
            }
        }
        if (!covered.contains(false)) {
            return Pair(grid.subList(grid.size - i - 1, grid.size), grid.size - i - 1)
        }
    }
    return Pair(grid, 0)
}

fun part1(input: List<String>): Int {
    val grid = MutableList(1) { MutableList(7) { '#' } }
    val jets = input[0].toList()
    var fallen = 0L
    var jet = 0
    while (fallen < 2022) {
        val shape = shapeNames[(fallen % 5).toInt()]
        val pair = getFallenAndJets(jets, jet, grid, shape, fallen)
        fallen = pair.first
        jet = pair.second
    }
    return grid.lastIndex
}

fun part2(input: List<String>): Long {
    var grid = MutableList(1) { MutableList(7) { '#' } }
    val jets = input[0].toList()
    var fallen = 0L
    var jet = 0
    var rows = 0L
    val lineJetPairs = mutableListOf<Triple<Long, Int, Long>>()
    while (fallen < 1000000000000) {
        val shape = shapeNames[(fallen % 5).toInt()]
        val pair = getFallenAndJets(jets, jet, grid, shape, fallen)
        fallen = pair.first
        jet = pair.second
        if (shape == "h" && grid.last().count { it == '.' } == 3) {
            if (lineJetPairs.any { it.second == jet }) {
                val previous = lineJetPairs.first { it.second == jet }
                val fallenGap = fallen - previous.first
                val rowGap = rows + grid.lastIndex - previous.third
                while (fallen < 1000000000000 - fallenGap) {
                    fallen += fallenGap
                    rows += rowGap
                }
            } else {
                lineJetPairs.add(Triple(fallen, jet, rows + grid.lastIndex))
            }
        }
        val gridInfo = pruneGrid(grid)
        grid = gridInfo.first
        rows += gridInfo.second
    }
    return rows + grid.lastIndex
}

private fun getFallenAndJets(
    jets: List<Char>,
    jet: Int,
    grid: MutableList<MutableList<Char>>,
    shape: String,
    fallen: Long,
): Pair<Long, Int> {
    var jet1 = jet
    var fallen1 = fallen
    var shapeLeft = 2
    var shapeAbove = 4
    while (true) {
        val gust = jets[jet1]
        if (gust == '<' && canMoveLeft(grid, shape, shapeLeft, shapeAbove)) {
            shapeLeft--
        } else if (gust == '>' && canMoveRight(grid, shape, shapeLeft, shapeAbove)) {
            shapeLeft++
        }
        jet1++
        if (jet1 == jets.size) {
            jet1 = 0
        }
        if (contact(grid, shape, shapeLeft, shapeAbove - 1)) {
            break
        } else {
            shapeAbove--
        }
    }
    addToGrid(grid, shape, shapeLeft, shapeAbove)
    fallen1++
    return Pair(fallen1, jet1)
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("src/main/kotlin/year2022/day17/Day17_test")
    check(part1(testInput) == 3068)
    check(part2(testInput) == 1514285714288L)

    val input = readInput("src/main/kotlin/year2022/day17/Day17")
    println(part1(input))
    println(part2(input))
}
