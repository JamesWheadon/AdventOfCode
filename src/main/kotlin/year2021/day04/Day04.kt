package year2021.day04

import readInput

fun columns(rowMatrix: MutableList<MutableList<Int>>): MutableList<MutableList<Int>> {
    val columnMatrix = MutableList(5) { MutableList(5) { 1 } }

    rowMatrix.forEachIndexed { indexRow, ints ->
        ints.forEachIndexed { indexColumn, i ->
            columnMatrix[indexColumn][indexRow] = i
        }
    }

    return columnMatrix
}

fun score(board: List<MutableList<Int>>, boardChecked: MutableList<MutableList<Int>>, number: Int): Int {
    var total = 0
    boardChecked.forEachIndexed { rowIndex, ints ->
        ints.forEachIndexed { columnIndex, i ->
            if (i == 1) {
                total += board[rowIndex][columnIndex]
            }
        }
    }
    return total * number
}

fun part1(input: List<String>): Int {
    val (numbers, boards, boardsChecked) = getBoards(input)
    for (number in numbers) {
        boards.forEachIndexed { boardIndex, board ->
            board.forEachIndexed { rowIndex, row ->
                val location = row.indexOf(number)
                if (location != -1) {
                    boardsChecked[boardIndex][rowIndex][location] = 0
                }
            }
        }
        boardsChecked.forEachIndexed { boardIndex, board ->
            if (board.contains(mutableListOf(0, 0, 0, 0, 0)) || columns(board).contains(mutableListOf(0, 0, 0, 0, 0))) {
                return score(boards[boardIndex], board, number)
            }
        }
    }
    return 0
}

fun part2(input: List<String>): Int {
    val (numbers, boards, boardsChecked) = getBoards(input)
    val doneBoards = MutableList(boards.size) { 1 }
    var lastBoard = -1
    for (number in numbers) {
        checkBoards(boards, number, boardsChecked)
        boardsChecked.forEachIndexed { boardIndex, board ->
            if (board.contains(mutableListOf(0, 0, 0, 0, 0)) || columns(board).contains(mutableListOf(0, 0, 0, 0, 0))) {
                doneBoards[boardIndex] = 0
                if (doneBoards.filter { it == 1 }.size == 1) {
                    lastBoard = doneBoards.indexOf(1)
                }
                if (boardIndex == lastBoard) {
                    return score(boards[lastBoard], boardsChecked[lastBoard], number)
                }
            }
        }
    }
    return 0
}

private fun checkBoards(
    boards: List<List<MutableList<Int>>>,
    number: Int,
    boardsChecked: MutableList<MutableList<MutableList<Int>>>,
) {
    boards.forEachIndexed { boardIndex, board ->
        board.forEachIndexed { rowIndex, row ->
            val location = row.indexOf(number)
            if (location != -1) {
                boardsChecked[boardIndex][rowIndex][location] = 0
            }
        }
    }
}

private fun getBoards(input: List<String>): Triple<List<Int>, List<List<MutableList<Int>>>, MutableList<MutableList<MutableList<Int>>>> {
    val numbers = input[0].split(",").map { it.toInt() }
    val boardRows = MutableList(0) { MutableList(0) { 0 } }
    input.subList(2, input.size).filter { it != "" }.forEach {
        boardRows.add(
            it.split(" ").filter { entry -> entry != "" }
                .map { value -> value.toInt() } as MutableList<Int>,
        )
    }
    val boards = boardRows.chunked(5)
    val boardsChecked = MutableList(boards.size) { MutableList(5) { MutableList(5) { 1 } } }
    return Triple(numbers, boards, boardsChecked)
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2021/day04/Day04_test")
    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)

    val input = readInput("src/main/kotlin/year2021/day04/Day04")
    println(part1(input))
    println(part2(input))
}
