package year2022.day07

import readInput

fun directories(input: List<String>): Pair<MutableList<Directory>, Directory?> {
    var currentDirectory: Directory? = null
    val directories = mutableListOf<Directory>()
    input.forEach {
        if (it[0] == '$') {
            when {
                it == "$ cd /" -> {
                    currentDirectory = Directory("/", null, mutableListOf(), mutableListOf())
                    directories.add(currentDirectory!!)
                }

                it == "$ cd .." -> {
                    currentDirectory = currentDirectory?.parent
                }

                it != "$ ls" -> {
                    currentDirectory =
                        currentDirectory?.subdirectories?.first { sub -> sub.name == it.split(" ").last() }
                }
            }
        } else {
            if (it.split(" ")[0] == "dir") {
                val newDirectory = Directory(it.split(" ")[1], currentDirectory, mutableListOf(), mutableListOf())
                currentDirectory?.subdirectories?.add(newDirectory)
                directories.add(newDirectory)
            } else {
                currentDirectory?.directoryFiles?.add(DirectoryFile(it.split(" ")[1], it.split(" ")[0].toInt()))
            }
        }
    }
    return Pair(directories, currentDirectory)
}

fun part1(input: List<String>): Int {
    val (directories, _) = directories(input)
    return directories.map { it.getSize() }.filter { it <= 100000 }.sum()
}

fun part2(input: List<String>): Int {
    var (directories, currentDirectory) = directories(input)
    while (currentDirectory?.parent != null) {
        currentDirectory = currentDirectory!!.parent
    }
    val spaceNeeded = (currentDirectory?.getSize() ?: 0) - 40000000
    return directories.map { it.getSize() }.filter { it >= spaceNeeded }.minOrNull()!!
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("src/main/kotlin/year2022/day07/Day07_test")
    check(part1(testInput) == 95437)
    check(part2(testInput) == 24933642)

    val input = readInput("src/main/kotlin/year2022/day07/Day07")
    println(part1(input))
    println(part2(input))
}

data class Directory(
    var name: String,
    var parent: Directory?,
    var directoryFiles: MutableList<DirectoryFile>,
    var subdirectories: MutableList<Directory>,
) {
    fun getSize(): Int {
        return subdirectories.sumOf { it.getSize() } + directoryFiles.sumOf { it.size }
    }
}

data class DirectoryFile(
    var name: String,
    var size: Int,
)
