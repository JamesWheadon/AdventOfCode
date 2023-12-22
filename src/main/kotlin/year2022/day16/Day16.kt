package year2022.day16

import readInput

fun getDistanceToValve(startValve: Valve, endValve: Valve): Int {
    return if (startValve == endValve) {
        0
    } else {
        var depth = 1
        var connections = startValve.leadsTo
        while (!connections.contains(endValve)) {
            depth++
            connections = connections.map { it.leadsTo }.flatten().toMutableList()
        }
        depth
    }
}

fun getValvesInfo(input: List<String>): MutableList<Valve> {
    val valves =
        input.map { valveInfo ->
            Valve(valveInfo.split(" ")[1], valveInfo.filter { it.isDigit() }.toInt())
        }
    input.forEach { valveInfo ->
        valveInfo.split("; ")[1].filter { it.isLetter() || it == ' ' }.split(" ")
            .filter { valves.any { valve -> valve.name == it } }
            .forEach { connectedValveName ->
                valves.find { it.name == valveInfo.split(" ")[1] }!!
                    .leadsTo.add(valves.find { it.name == connectedValveName }!!)
            }
    }
    val keyValves = valves.filter { it.name == "AA" || it.flowRate > 0 }.toMutableList()
    keyValves.forEach { valve ->
        valve.flowValves =
            keyValves.filter { it != valve && it.flowRate > 0 }.map { ValveMove(it, getDistanceToValve(valve, it)) }
                .toMutableList()
    }
    return keyValves
}

fun part1(input: List<String>): Int {
    val keyValves = getValvesInfo(input)
    var moves = mutableListOf(Move(keyValves.first { it.name == "AA" }, mutableListOf(), 30))
    while (moves[0].openValves.size < keyValves.size - 1) {
        val newMoves = moves.map { it.createNewMoves() }.flatten().toMutableList()
        if (moves == newMoves) {
            break
        }
        moves = newMoves
    }
    return moves.maxOf { move -> move.getCost() }
}

fun findBestMove(startingMove: DualMoves): Int {
    var moves = mutableListOf(startingMove)
    var size = 0
    while (size != moves.size) {
        size = moves.size
        moves = moves.map { it.createNewMoves() }.flatten().toMutableList()
    }
    return moves.maxOf { it.getCost() }
}

fun part2(input: List<String>): Int {
    val keyValves = getValvesInfo(input)
    val moves = mutableListOf(
        DualMoves(
            keyValves.first { it.name == "AA" },
            keyValves.first { it.name == "AA" },
            mutableListOf(),
            mutableListOf(),
            26,
        ),
    )
    val startingMoves = moves.map { it.createNewMoves() }.flatten().toMutableList()
    val movesToDelete = mutableListOf<DualMoves>()
    for (firstMove in startingMoves) {
        if (!movesToDelete.contains(firstMove)) {
            movesToDelete.addAll(
                startingMoves.filter { secondMove ->
                    firstMove.myPath.map { it.valve } == secondMove.elephantPath.map { it.valve } &&
                        firstMove.elephantPath.map { it.valve } == secondMove.myPath.map { it.valve }
                },
            )
        }
    }
    startingMoves.removeAll(movesToDelete)
    val scores = mutableListOf<Int>()
    for (startingMove in startingMoves) {
        scores.add(findBestMove(startingMove))
        println(scores)
    }
    val best = scores.maxOrNull()!!
    var bestPathStart = mutableListOf(startingMoves[scores.indexOf(best)])
    var size = 0
    while (size != bestPathStart.size) {
        size = bestPathStart.size
        bestPathStart = bestPathStart.map { it.createNewMoves() }.flatten().toMutableList()
    }
    val bestPath = bestPathStart.maxByOrNull { it.getCost() }!!
    println(best)
    println("my path:")
    bestPath.myPath.forEach { println(it) }
    println("elephant path")
    bestPath.elephantPath.forEach { println(it) }
    return best
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("src/main/kotlin/year2022/day16/Day16_test")
    check(part1(testInput) == 1651)
    check(part2(testInput) == 1707)

    val input = readInput("src/main/kotlin/year2022/day16/Day16")
    println(part1(input))
    println(part2(input))
}

class Valve(val name: String, val flowRate: Int) {
    val leadsTo: MutableList<Valve> = mutableListOf()
    var flowValves: MutableList<ValveMove> = mutableListOf()
    override fun toString(): String {
        return "Valve(name='$name', flowRate=$flowRate)"
    }
}

data class ValveMove(val valve: Valve, val distance: Int) {
    override fun toString(): String {
        return "ValveMove(valve=$valve, distance=$distance)"
    }
}

class Move(private val position: Valve, val openValves: MutableList<ValveMove>, private val maxTime: Int) {

    fun createNewMoves(): MutableList<Move> {
        return if (getPathTime() < maxTime) {
            val newMoves = mutableListOf<Move>()
            position.flowValves.filter { valve -> !openValves.map { it.valve.name }.contains(valve.valve.name) }
                .forEach {
                    val newOpenValves = mutableListOf<ValveMove>()
                    newOpenValves.addAll(openValves)
                    newOpenValves.add(it)
                    newMoves.add(Move(it.valve, newOpenValves, maxTime))
                }
            newMoves
        } else {
            mutableListOf(this)
        }
    }

    private fun getPathTime(): Int {
        var minute = 0
        openValves.forEach {
            minute += (it.distance + 1)
        }
        return minute
    }

    fun getCost(): Int {
        var cost = 0
        var minute = 0
        openValves.forEach {
            minute += (it.distance + 1)
            if (minute < maxTime) {
                cost += it.valve.flowRate * (maxTime - minute)
            }
        }
        return cost
    }
}

class DualMoves(
    private val myPosition: Valve,
    private val elephantPosition: Valve,
    val myPath: MutableList<ValveMove>,
    val elephantPath: MutableList<ValveMove>,
    private val maxTime: Int,
) {

    fun createNewMoves(): MutableList<DualMoves> {
        if (myPosition.flowValves.toSet()
                .intersect((myPath + elephantPath).toSet()).size == myPosition.flowValves.size
        ) {
            return mutableListOf(DualMoves(myPosition, elephantPosition, myPath, elephantPath, maxTime))
        }
        val newMoves = mutableListOf<DualMoves>()
        val myPathTime = getPathTime(myPath)
        val elephantPathTime = getPathTime(elephantPath)
        var bothPossible = false
        if (myPathTime < maxTime && elephantPathTime < maxTime) {
            myPosition.flowValves.filter { myValve ->
                !myPath.map { it.valve }.contains(myValve.valve) &&
                    !elephantPath.map { it.valve }.contains(myValve.valve) &&
                    myPathTime + myValve.distance < maxTime
            }.forEach { myValve ->
                elephantPosition.flowValves.filter { elephantValve ->
                    !myPath.map { it.valve }.contains(elephantValve.valve) &&
                        !elephantPath.map { it.valve }.contains(elephantValve.valve) &&
                        elephantPathTime + elephantValve.distance < maxTime &&
                        myValve.valve != elephantValve.valve
                }.forEach { elephantValve ->
                    val myNewPath = mutableListOf<ValveMove>()
                    myNewPath.addAll(myPath)
                    myNewPath.add(myValve)
                    val elephantNewPath = mutableListOf<ValveMove>()
                    elephantNewPath.addAll(elephantPath)
                    elephantNewPath.add(elephantValve)
                    newMoves.add(DualMoves(myValve.valve, elephantValve.valve, myNewPath, elephantNewPath, maxTime))
                    bothPossible = true
                }
            }
        }
        if (myPathTime < maxTime && !bothPossible) {
            myPosition.flowValves.filter { myValve ->
                !myPath.map { it.valve }.contains(myValve.valve) &&
                    !elephantPath.map { it.valve }.contains(myValve.valve) &&
                    myPathTime + myValve.distance < maxTime
            }.forEach { myValve ->
                val myNewPath = mutableListOf<ValveMove>()
                myNewPath.addAll(myPath)
                myNewPath.add(myValve)
                newMoves.add(DualMoves(myValve.valve, elephantPosition, myNewPath, elephantPath, maxTime))
            }
        }
        if (elephantPathTime < maxTime && !bothPossible) {
            elephantPosition.flowValves.filter { elephantValve ->
                !myPath.map { it.valve }.contains(elephantValve.valve) &&
                    !elephantPath.map { it.valve }.contains(elephantValve.valve) &&
                    elephantPathTime + elephantValve.distance < maxTime
            }.forEach { elephantValve ->
                val elephantNewPath = mutableListOf<ValveMove>()
                elephantNewPath.addAll(elephantPath)
                elephantNewPath.add(elephantValve)
                newMoves.add(DualMoves(myPosition, elephantValve.valve, myPath, elephantNewPath, maxTime))
            }
        }
        if (newMoves.isEmpty()) {
            newMoves.add(DualMoves(myPosition, elephantPosition, myPath, elephantPath, maxTime))
        }
        return newMoves
    }

    private fun getPathTime(path: MutableList<ValveMove>): Int {
        var minute = 0
        path.forEach {
            minute += (it.distance + 1)
        }
        return minute
    }

    fun getCost(): Int {
        var cost = 0
        var minute = 0
        myPath.forEach {
            minute += (it.distance + 1)
            if (minute < maxTime) {
                cost += it.valve.flowRate * (maxTime - minute)
            }
        }
        minute = 0
        elephantPath.forEach {
            minute += (it.distance + 1)
            if (minute < maxTime) {
                cost += it.valve.flowRate * (maxTime - minute)
            }
        }
        return cost
    }
}
