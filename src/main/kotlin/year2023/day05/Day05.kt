package year2023.day05

import readInput

fun part1(input: List<String>): Long {
    val seeds =
        input.first().split(" ").filter { item -> item.contains("[0-9]".toRegex()) }.map { item -> item.toLong() }
    val maps = getMaps(input)
    return seeds.minOf { seed ->
        var currentValue = seed
        maps.forEach { map ->
            map.filter { mapping -> currentValue in mapping[1]..<(mapping[1] + mapping[2]) }.forEach { mapping ->
                currentValue = mapping[0] + (currentValue - mapping[1])
            }
        }
        currentValue
    }
}

fun part2(input: List<String>): Long {
    val seeds: List<MutableList<LongRange>> = input.first()
        .split(" ")
        .asSequence()
        .filter { item -> item.contains("[0-9]".toRegex()) }
        .map { item -> item.toLong() }
        .windowed(2, 2)
        .map { pair ->
            mutableListOf(pair[0] ..< pair[0] + pair[1])
        }.toList()
    val maps = getMaps(input)
    return seeds.minOf { seedRange ->
        var current: MutableList<LongRange> = seedRange
        maps.forEach { map ->
            val mapped = current.filter { range -> map.none { overlap(it, range) } }.toMutableList()
            current = current.filter { range -> map.any { overlap(it, range) } }.toMutableList()
            while (current.isNotEmpty()) {
                val range = current.removeFirst()
                if (!getMappedRanges(map, range, mapped, current)) {
                    mapped.add(range)
                }
            }
            current.addAll(mapped)
        }
        current.minOf { it.first }
    }
}

private fun getMappedRanges(
    map: MutableList<List<Long>>,
    range: LongRange,
    mapped: MutableList<LongRange>,
    current: MutableList<LongRange>
): Boolean {
    for (mapping in map) {
        when {
            fullRangeInMap(range, mapping) -> {
                mapped.add(mapping[0] + range.first - mapping[1]..mapping[0] + range.last - mapping[1])
                return true
            }
            fullMapInRange(mapping, range) -> {
                mapped.add(mapping[0]..<mapping[0] + mapping[2])
                current.add(range.first..<mapping[1])
                current.add(mapping[1] + mapping[2]..range.last)
                return true
            }
            mapStartInRange(mapping, range) -> {
                mapped.add(mapping[0]..mapping[0] + range.last - mapping[1])
                current.add(range.first..<mapping[1])
                return true
            }
            mapEndInRange(mapping, range) -> {
                mapped.add(mapping[0] + range.first - mapping[1]..<mapping[0] + mapping[2])
                current.add(mapping[1] + mapping[2]..range.last)
                return true
            }
        }
    }
    return false
}

private fun mapEndInRange(mapping: List<Long>, range: LongRange) =
    (mapping[1] + mapping[2] - 1) in range

private fun mapStartInRange(mapping: List<Long>, range: LongRange) = mapping[1] in range

private fun fullMapInRange(mapping: List<Long>, range: LongRange) =
    mapStartInRange(mapping, range) && mapEndInRange(mapping, range)

private fun fullRangeInMap(range: LongRange, mapping: List<Long>) =
    range.first in mapping[1]..<mapping[1] + mapping[2] && range.last in mapping[1]..<mapping[1] + mapping[2]

fun overlap(mapping: List<Long>, range: LongRange): Boolean {
    return (mapping[1] <= range.last && mapping[1] + mapping[2] - 1 >= range.first)
}

private fun getMaps(input: List<String>): MutableList<MutableList<List<Long>>> {
    val maps = mutableListOf<MutableList<List<Long>>>()
    input.filter { it.contains(":").xor(it.contains("[0-9]".toRegex())) }.forEach { row ->
        if (row.contains(":")) {
            maps.add(mutableListOf())
        } else {
            maps.last().add(row.split(" ").map { it.toLong() })
        }
    }
    return maps
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2023/day05/Day05_test")
    check(part1(testInput) == 35L)
    check(part2(testInput) == 46L)

    val input = readInput("src/main/kotlin/year2023/day05/Day05")
    println(part1(input))
    println(part2(input))
}
