package year2023.day12

import readInput

fun part1(input: List<String>): Int {
    return input.sumOf { springs ->
        val springInfo = springs.split(" ")[0]
        val springGroups = springs.split(" ")[1].split(",").map { it.toInt() }
        val firstLocations = mutableListOf<Int>()
        springGroups.forEach { springGroup ->
            val remaining = springInfo.substring(firstLocations.lastOrNull()?.plus(1)?.plus(springGroups[firstLocations.size - 1]) ?: 0, springInfo.length)
            firstLocations.add(remaining.replace("?", "#").indexOf("#".repeat(springGroup)) + (firstLocations.lastOrNull()?.plus(1)?.plus(springGroups[firstLocations.size - 1]) ?: 0))
        }
        val lastLocations = mutableListOf<Int>()
        springGroups.reversed().forEach { springGroup ->
            val remaining = springInfo.substring(0, lastLocations.lastOrNull()?.minus(1) ?: springInfo.length)
            lastLocations.add(remaining.replace("?", "#").lastIndexOf("#".repeat(springGroup)))
        }
        lastLocations.reverse()
        val springPossiblePlaces = springGroups.mapIndexed { index, group ->
            val possibleRange =
                springInfo.substring(firstLocations[index]..<lastLocations[index] + group).replace("?", "#")
            var startIndex = possibleRange.indexOf("#".repeat(group))
            val places = mutableListOf<IntRange>()
            while (startIndex != -1) {
                places.add(firstLocations[index] + startIndex..<firstLocations[index] + startIndex + group)
                startIndex = possibleRange.indexOf("#".repeat(group), startIndex + 1)
            }
            places.map { mutableMapOf(it to 1) }.toList()
        }.toMutableList()
        springPossiblePlaces[0] = springPossiblePlaces[0].filter { !springInfo.substring(0, it.keys.first().first).contains("#") }
        springPossiblePlaces[springPossiblePlaces.size - 1] = springPossiblePlaces.last().filter { !springInfo.substring(it.keys.first().last + 1).contains("#") }
        for (i in 1 ..<springPossiblePlaces.size) {
            springPossiblePlaces[i].map { possiblePlace ->
                possiblePlace[possiblePlace.keys.first()] =
                    springPossiblePlaces[i - 1].filter { it.keys.first().last + 1 < possiblePlace.keys.first().first }
                        .filter {
                            !springInfo.substring(it.keys.first().last + 1, possiblePlace.keys.first().first)
                                .contains("#")
                        }.sumOf { it.values.first() }
            }
        }
        springPossiblePlaces.last().sumOf { it.values.sum() }
    }
}

fun part2(input: List<String>): Long {
    return input.sumOf { springs ->
        val springInfo = springs.split(" ")[0].plus("?").repeat(5).substring(0, springs.split(" ")[0].length * 5 + 4)
        val springGroups = MutableList(5) { springs.split(" ")[1].split(",").map { it.toInt() } }.flatten()
        val firstLocations = mutableListOf<Int>()
        println(springInfo)
        println(springGroups)
        springGroups.forEach { springGroup ->
            val remaining = springInfo.substring(firstLocations.lastOrNull()?.plus(1)?.plus(springGroups[firstLocations.size - 1]) ?: 0, springInfo.length)
            println(firstLocations)
            println(remaining)
            firstLocations.add(remaining.replace("?", "#").indexOf("#".repeat(springGroup)) + (firstLocations.lastOrNull()?.plus(1)?.plus(springGroups[firstLocations.size - 1]) ?: 0))
        }
        val lastLocations = mutableListOf<Int>()
        springGroups.reversed().forEach { springGroup ->
            val remaining = springInfo.substring(0, lastLocations.lastOrNull()?.minus(1) ?: springInfo.length)
            lastLocations.add(remaining.replace("?", "#").lastIndexOf("#".repeat(springGroup)))
        }
        lastLocations.reverse()
        val springPossiblePlaces = springGroups.mapIndexed { index, group ->
            val possibleRange =
                springInfo.substring(firstLocations[index]..<lastLocations[index] + group).replace("?", "#")
            var startIndex = possibleRange.indexOf("#".repeat(group))
            val places = mutableListOf<IntRange>()
            while (startIndex != -1) {
                places.add(firstLocations[index] + startIndex..<firstLocations[index] + startIndex + group)
                startIndex = possibleRange.indexOf("#".repeat(group), startIndex + 1)
            }
            places.map { mutableMapOf(it to 1L) }.toList()
        }.toMutableList()
        springPossiblePlaces[0] = springPossiblePlaces[0].filter { !springInfo.substring(0, it.keys.first().first).contains("#") }
        springPossiblePlaces[springPossiblePlaces.size - 1] = springPossiblePlaces.last().filter { !springInfo.substring(it.keys.first().last + 1).contains("#") }
        for (i in 1 ..<springPossiblePlaces.size) {
            springPossiblePlaces[i].map { possiblePlace ->
                possiblePlace[possiblePlace.keys.first()] =
                    springPossiblePlaces[i - 1].filter { it.keys.first().last + 1 < possiblePlace.keys.first().first }
                        .filter {
                            !springInfo.substring(it.keys.first().last + 1, possiblePlace.keys.first().first)
                                .contains("#")
                        }.sumOf { it.values.first() }
            }
        }
        springPossiblePlaces.last().sumOf { it.values.sum() }
    }
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2023/day12/Day12_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 525152L)

    val input = readInput("src/main/kotlin/year2023/day12/Day12")
    println(part1(input))
    println(part2(input))
}
