package year2022.day19

import readInput
import kotlin.math.min

fun getFactory(blueprintInfo: List<String>) = Factory(
    blueprintInfo[0].filter { it.isDigit() }.toInt(),
    blueprintInfo[1].filter { it.isDigit() }.toInt(),
    blueprintInfo[2].filter { it.isDigit() }.toInt(),
    Pair(
        blueprintInfo[3].split("ore")[0].filter { it.isDigit() }.toInt(),
        blueprintInfo[3].split("ore")[1].filter { it.isDigit() }.toInt(),
    ),
    Pair(
        blueprintInfo[4].split("ore")[0].filter { it.isDigit() }.toInt(),
        blueprintInfo[4].split("ore")[1].filter { it.isDigit() }.toInt(),
    ),
)

fun part1(input: List<String>): Int {
    val factories =
        input.windowed(5, 6).map { blueprintInfo ->
            getFactory(blueprintInfo)
        }
    factories.forEach {
        it.mine(24)
    }
    return factories.sumOf { it.blueprintID * it.operatingList.first().third[3] }
}

fun part2(input: List<String>): Int {
    val factories =
        input.windowed(5, 6).subList(0, min(input.windowed(5, 6).size, 3)).map { blueprintInfo ->
            getFactory(blueprintInfo)
        }
    factories.forEach {
        it.mine(32)
    }
    return factories.map { it.operatingList.first().third[3] }.reduce { acc, i -> acc * i }
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("src/main/kotlin/year2022/day19/Day19_test")
    check(part1(testInput) == 33)
    check(part2(testInput) == 3472)

    val input = readInput("src/main/kotlin/year2022/day19/Day19")
    println(part1(input))
    println(part2(input))
}

class Factory(
    val blueprintID: Int,
    private val oreRobotCost: Int,
    private val clayRobotCost: Int,
    private val obsidianRobotCost: Pair<Int, Int>,
    private val geodeRobotCost: Pair<Int, Int>,
) {
    var operatingList = mutableListOf(Triple(1, mutableListOf(1, 0, 0, 0), mutableListOf(0, 0, 0, 0)))

    fun mine(time: Int) {
        while (operatingList[0].first <= time) {
            operatingList = operatingList.map { getNewPossibilities(it) }.flatten().toMutableList()
            val maxOfGeodeRobots = operatingList.maxOf { it.second[3] }
            val maxOfGeodeMined = operatingList.filter { it.second[3] == maxOfGeodeRobots }.maxOf { it.third[3] }
            val maxOfObsidianRobots =
                operatingList.filter { it.second[3] == maxOfGeodeRobots && it.third[3] == maxOfGeodeMined }
                    .maxOf { it.second[2] }
            val bestForMinute = operatingList.filter { possibility ->
                possibility.second[3] == maxOfGeodeRobots && possibility.third[3] == maxOfGeodeMined && possibility.second[2] == maxOfObsidianRobots
            }.maxByOrNull { it.third[2] }!!
            if (bestForMinute.second[3] > 0) {
                operatingList =
                    operatingList.filter { it.second[3] >= bestForMinute.second[3] - 1 && it.third[3] >= bestForMinute.third[3] - 2 }
                        .toMutableList()
            } else if (bestForMinute.second[2] > 0) {
                operatingList =
                    operatingList.filter { it.second[2] >= bestForMinute.second[2] - 1 && it.third[2] >= bestForMinute.third[2] - 4 }
                        .toMutableList()
            }
        }
        operatingList = operatingList.sortedByDescending { it.third[3] }.toMutableList()
        println("most geodes possibility is ${operatingList.first()}, best amount is ${operatingList.maxOf { it.third[3] }}")
    }

    private fun getNewPossibilities(possibility: Triple<Int, MutableList<Int>, MutableList<Int>>): MutableList<Triple<Int, MutableList<Int>, MutableList<Int>>> {
        val newPossibilities = mutableListOf<Triple<Int, MutableList<Int>, MutableList<Int>>>()
        val newRobots = buildNewRobot(possibility.third)
        if (newRobots[3] == 1) {
            val nextStateRobots = mutableListOf(
                possibility.second[0],
                possibility.second[1],
                possibility.second[2],
                possibility.second[3] + 1,
            )
            val nextStateResources = mutableListOf(
                possibility.third[0] + possibility.second[0] - geodeRobotCost.first,
                possibility.third[1] + possibility.second[1],
                possibility.third[2] + possibility.second[2] - geodeRobotCost.second,
                possibility.third[3] + possibility.second[3],
            )
            newPossibilities.add(Triple(possibility.first + 1, nextStateRobots, nextStateResources))
        } else {
            newRobots.forEachIndexed { index, newRobot ->
                if (newRobot == 1) {
                    getPossibilities(index, possibility, newPossibilities)
                }
            }
            val nextStateRobots = mutableListOf(
                possibility.second[0],
                possibility.second[1],
                possibility.second[2],
                possibility.second[3],
            )
            val nextStateResources = mutableListOf(
                possibility.third[0] + possibility.second[0],
                possibility.third[1] + possibility.second[1],
                possibility.third[2] + possibility.second[2],
                possibility.third[3] + possibility.second[3],
            )
            newPossibilities.add(Triple(possibility.first + 1, nextStateRobots, nextStateResources))
        }
        return newPossibilities
    }

    private fun getPossibilities(
        index: Int,
        possibility: Triple<Int, MutableList<Int>, MutableList<Int>>,
        newPossibilities: MutableList<Triple<Int, MutableList<Int>, MutableList<Int>>>,
    ) {
        when (index) {
            0 -> {
                if (possibility.second[0] < listOf(
                        clayRobotCost,
                        obsidianRobotCost.first,
                        geodeRobotCost.first,
                    ).maxOrNull()!!
                ) {
                    val nextStateRobots = mutableListOf(
                        possibility.second[0] + 1,
                        possibility.second[1],
                        possibility.second[2],
                        possibility.second[3],
                    )
                    val nextStateResources = mutableListOf(
                        possibility.third[0] + possibility.second[0] - oreRobotCost,
                        possibility.third[1] + possibility.second[1],
                        possibility.third[2] + possibility.second[2],
                        possibility.third[3] + possibility.second[3],
                    )
                    newPossibilities.add(
                        Triple(
                            possibility.first + 1,
                            nextStateRobots,
                            nextStateResources,
                        ),
                    )
                }
            }

            1 -> {
                if (possibility.second[0] < obsidianRobotCost.second) {
                    val nextStateRobots = mutableListOf(
                        possibility.second[0],
                        possibility.second[1] + 1,
                        possibility.second[2],
                        possibility.second[3],
                    )
                    val nextStateResources = mutableListOf(
                        possibility.third[0] + possibility.second[0] - clayRobotCost,
                        possibility.third[1] + possibility.second[1],
                        possibility.third[2] + possibility.second[2],
                        possibility.third[3] + possibility.second[3],
                    )
                    newPossibilities.add(
                        Triple(
                            possibility.first + 1,
                            nextStateRobots,
                            nextStateResources,
                        ),
                    )
                }
            }

            2 -> {
                val nextStateRobots = mutableListOf(
                    possibility.second[0],
                    possibility.second[1],
                    possibility.second[2] + 1,
                    possibility.second[3],
                )
                val nextStateResources = mutableListOf(
                    possibility.third[0] + possibility.second[0] - obsidianRobotCost.first,
                    possibility.third[1] + possibility.second[1] - obsidianRobotCost.second,
                    possibility.third[2] + possibility.second[2],
                    possibility.third[3] + possibility.second[3],
                )
                newPossibilities.add(
                    Triple(
                        possibility.first + 1,
                        nextStateRobots,
                        nextStateResources,
                    ),
                )
            }
        }
    }

    private fun buildNewRobot(resources: MutableList<Int>): MutableList<Int> {
        val options = mutableListOf(0, 0, 0, 0)
        if (resources[0] >= oreRobotCost) {
            options[0] = 1
        }
        if (resources[0] >= clayRobotCost) {
            options[1] = 1
        }
        if (resources[0] >= obsidianRobotCost.first && resources[1] >= obsidianRobotCost.second) {
            options[2] = 1
        }
        if (resources[0] >= geodeRobotCost.first && resources[2] >= geodeRobotCost.second) {
            options[3] = 1
        }
        return options
    }
}
