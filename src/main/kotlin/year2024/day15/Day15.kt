package year2024.day15

import readInput

fun part1(input: List<String>): Int {
    val walls = mutableListOf<Wall>()
    val boxes = mutableListOf<Box>()
    lateinit var robot: Robot
    input.takeWhile { line -> line.isNotEmpty() }.forEachIndexed { row, s ->
        s.forEachIndexed { col, c ->
            when (c) {
                '#' -> walls.add(Wall(col, row))
                'O' -> boxes.add(Box(col, row))
                '@' -> robot = Robot(col, row)
            }
        }
    }
    input.takeLastWhile { line -> line.isNotEmpty() }.joinToString(separator = "").forEach { instruction ->
        val nextBoxes = mutableListOf<Box>()
        var movedBoxes = listOf<Box>()
        when (instruction) {
            '>' -> {
                var x = robot.x + 1
                val y = robot.y
                while (boxes.firstOrNull { box -> box.x == x && box.y == y } != null) {
                    nextBoxes.add(boxes.first { box -> box.x == x && box.y == y })
                    x += 1
                }
                if (walls.none { wall -> wall.x == x && wall.y == y }) {
                    robot = robot.copy(x = robot.x + 1)
                    movedBoxes = nextBoxes.map { box -> box.copy(x = box.x + 1) }
                }
            }

            '<' -> {
                var x = robot.x - 1
                val y = robot.y
                while (boxes.firstOrNull { box -> box.x == x && box.y == y } != null) {
                    nextBoxes.add(boxes.first { box -> box.x == x && box.y == y })
                    x -= 1
                }
                if (walls.none { wall -> wall.x == x && wall.y == y }) {
                    robot = robot.copy(x = robot.x - 1)
                    movedBoxes = nextBoxes.map { box -> box.copy(x = box.x - 1) }
                }
            }

            'v' -> {
                val x = robot.x
                var y = robot.y + 1
                while (boxes.firstOrNull { box -> box.x == x && box.y == y } != null) {
                    nextBoxes.add(boxes.first { box -> box.x == x && box.y == y })
                    y += 1
                }
                if (walls.none { wall -> wall.x == x && wall.y == y }) {
                    robot = robot.copy(y = robot.y + 1)
                    movedBoxes = nextBoxes.map { box -> box.copy(y = box.y + 1) }
                }
            }

            '^' -> {
                val x = robot.x
                var y = robot.y - 1
                while (boxes.firstOrNull { box -> box.x == x && box.y == y } != null) {
                    nextBoxes.add(boxes.first { box -> box.x == x && box.y == y })
                    y -= 1
                }
                if (walls.none { wall -> wall.x == x && wall.y == y }) {
                    robot = robot.copy(y = robot.y - 1)
                    movedBoxes = nextBoxes.map { box -> box.copy(y = box.y - 1) }
                }
            }
        }
        if (movedBoxes.size == nextBoxes.size) {
            boxes.removeAll(nextBoxes)
            boxes.addAll(movedBoxes)
        }
    }
    return boxes.sumOf { box -> 100 * box.y + box.x }
}

fun part2(input: List<String>): Int {
    val walls = mutableListOf<Wall>()
    val boxes = mutableListOf<Box>()
    lateinit var robot: Robot
    input.takeWhile { line -> line.isNotEmpty() }.map { line ->
        line.map { c ->
            when (c) {
                '#' -> "##"
                'O' -> "[]"
                '.' -> ".."
                '@' -> "@."
                else -> ""
            }
        }.joinToString("")
    }.forEachIndexed { row, s ->
        s.forEachIndexed { col, c ->
            when (c) {
                '#' -> walls.add(Wall(col, row))
                '[' -> boxes.add(Box(col, row))
                '@' -> robot = Robot(col, row)
            }
        }
    }
    input.takeLastWhile { line -> line.isNotEmpty() }.joinToString(separator = "").forEach { instruction ->
        val nextBoxes = mutableListOf<Box>()
        var movedBoxes = listOf<Box>()
        when (instruction) {
            '>' -> {
                var x = robot.x + 1
                val y = robot.y
                while (boxes.firstOrNull { box -> box.x == x && box.y == y } != null) {
                    nextBoxes.add(boxes.first { box -> box.x == x && box.y == y })
                    x += 2
                }
                if (walls.none { wall -> wall.x == x && wall.y == y }) {
                    robot = robot.copy(x = robot.x + 1)
                    movedBoxes = nextBoxes.map { box -> box.copy(x = box.x + 1) }
                }
            }

            '<' -> {
                var x = robot.x - 1
                val y = robot.y
                while (boxes.firstOrNull { box -> box.x == x - 1 && box.y == y } != null) {
                    nextBoxes.add(boxes.first { box -> box.x == x - 1 && box.y == y })
                    x -= 2
                }
                if (walls.none { wall -> wall.x == x && wall.y == y }) {
                    robot = robot.copy(x = robot.x - 1)
                    movedBoxes = nextBoxes.map { box -> box.copy(x = box.x - 1) }
                }
            }

            'v' -> {
                while (boxes.filter { box -> !nextBoxes.contains(box) }.any { box ->
                        (box.x in robot.x - 1..robot.x && box.y == robot.y + 1) || nextBoxes.any { underBox ->
                            box.y - underBox.y == 1 && (box.x..box.x + 1).intersect(
                                underBox.x..underBox.x + 1
                            ).isNotEmpty()
                        }
                    }) {
                    nextBoxes.addAll(boxes.filter { box -> !nextBoxes.contains(box) }.filter { box ->
                        (box.x in robot.x - 1..robot.x && box.y == robot.y + 1) || nextBoxes.any { underBox ->
                            box.y - underBox.y == 1 && (box.x..box.x + 1).intersect(
                                underBox.x..underBox.x + 1
                            ).isNotEmpty()
                        }
                    })
                }
                if (walls.none { wall -> wall.x == robot.x && wall.y == robot.y + 1 || nextBoxes.any { underBox ->
                        wall.y - underBox.y == 1 && wall.x in underBox.x..underBox.x + 1
                    } }) {
                    robot = robot.copy(y = robot.y + 1)
                    movedBoxes = nextBoxes.map { box -> box.copy(y = box.y + 1) }
                }
            }

            '^' -> {
                while (boxes.filter { box -> !nextBoxes.contains(box) }.any { box ->
                        (box.x in robot.x - 1..robot.x && box.y == robot.y - 1) || nextBoxes.any { underBox ->
                            box.y - underBox.y == -1 && (box.x..box.x + 1).intersect(
                                underBox.x..underBox.x + 1
                            ).isNotEmpty()
                        }
                    }) {
                    nextBoxes.addAll(boxes.filter { box -> !nextBoxes.contains(box) }.filter { box ->
                        (box.x in robot.x - 1..robot.x && box.y == robot.y - 1) || nextBoxes.any { underBox ->
                            box.y - underBox.y == -1 && (box.x..box.x + 1).intersect(
                                underBox.x..underBox.x + 1
                            ).isNotEmpty()
                        }
                    })
                }
                if (walls.none { wall -> wall.x == robot.x && wall.y == robot.y - 1 || nextBoxes.any { underBox ->
                        wall.y - underBox.y == -1 && wall.x in underBox.x..underBox.x + 1
                    } }) {
                    robot = robot.copy(y = robot.y - 1)
                    movedBoxes = nextBoxes.map { box -> box.copy(y = box.y - 1) }
                }
            }
        }
        if (movedBoxes.size == nextBoxes.size) {
            boxes.removeAll(nextBoxes)
            boxes.addAll(movedBoxes)
        }
    }
    return boxes.sumOf { box -> 100 * box.y + box.x }
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2024/day15/Day15_test")
    check(part1(testInput) == 10092)
    check(part2(testInput) == 9021)

    val input = readInput("src/main/kotlin/year2024/day15/Day15")
    println(part1(input))
    println(part2(input))
}

data class Wall(val x: Int, val y: Int)
data class Box(val x: Int, val y: Int)
data class Robot(val x: Int, val y: Int)
