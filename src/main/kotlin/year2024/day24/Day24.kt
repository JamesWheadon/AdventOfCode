package year2024.day24

import readInput
import kotlin.math.min

fun part1(input: List<String>): Long {
    val gates = input.takeWhile { line -> line.isNotEmpty() }
        .map { ValueGate(it.split(":")[0], it.split(":")[1].first(Char::isDigit) == '1') }
        .toMutableList()
    val operations = input.takeLastWhile { line -> line.isNotEmpty() }.toMutableList()
    while (operations.isNotEmpty()) {
        val completed = mutableListOf<String>()
        for (i in operations.indices) {
            val parts = operations[i].split(" ")
            val first = gates.firstOrNull { it.name == parts[0] } ?: continue
            val second = gates.firstOrNull { it.name == parts[2] } ?: continue
            when (parts[1]) {
                "XOR" -> gates.add(ValueGate(parts[4], first.value.xor(second.value)))
                "OR" -> gates.add(ValueGate(parts[4], first.value.or(second.value)))
                "AND" -> gates.add(ValueGate(parts[4], first.value.and(second.value)))
            }
            completed.add(operations[i])
        }
        operations.removeAll(completed)
    }
    return gates.filter { it.name.first() == 'z' }
        .sortedByDescending { it.name }
        .joinToString("") { gate ->
            if (gate.value) {
                "1"
            } else {
                "0"
            }
        }.toLong(2)
}

fun part2(input: List<String>): String {
    var gates: MutableList<Gate> = input.takeWhile { line -> line.isNotEmpty() }
        .map { StartGate(it.split(":")[0], it.split(":")[1].first(Char::isDigit) == '1') }
        .toMutableList()
    val operations = input.takeLastWhile { line -> line.isNotEmpty() }.toMutableList()
    while (operations.isNotEmpty()) {
        val completed = mutableListOf<String>()
        for (i in operations.indices) {
            val parts = operations[i].split(" ")
            val first = gates.firstOrNull { it.name == parts[0] } ?: continue
            val second = gates.firstOrNull { it.name == parts[2] } ?: continue
            gates.add(OperationGate(parts[4], first, second, parts[1]))
            completed.add(operations[i])
        }
        operations.removeAll(completed)
    }
    gates.sortedBy { it.name }.forEach { println("${it.name} ${it.operation()}") }
    val gateOpOrder = mutableListOf("XOR", "XOR")
    repeat(50) {
        gateOpOrder.addAll(listOf("AND", "OR", "XOR", "AND"))
    }
    val swaps = mutableListOf("krs", "cpm")
    while (swaps.size < 8) {
        val zGates = gates.filter { it.name.first() == 'z' }.sortedBy { it.name }
        for (zGate in zGates) {
            val operation = zGate.operation()
            val ops = operation.replace("[x[0-9]+|y[0-9]+|(|)]".toRegex(), "")
                .removePrefix(" ")
                .removeSuffix(" ")
                .replace("  ", " ")
                .split(" ")
            val patternStart = if (zGate == gates.filter { it.name.first() == 'z' }.maxBy { it.name }) {
                2
            } else {
                0
            }
            val patternLength = when (zGate.name) {
                "z00" -> 1
                "z01" -> 3
                "z45" -> 177
                else -> zGate.name.filter(Char::isDigit).toInt() * 4 - 1
            }
            val pattern = gateOpOrder.subList(patternStart, patternLength)
            if (ops != pattern) {
                val gateNumber = zGate.name.filter(Char::isDigit).toInt()
                var desired = "(x${String.format("%02d", gateNumber)} XOR y${String.format("%02d", gateNumber)})"
                if (gateNumber == 1) {
                    desired = "((x01 XOR y01) XOR (x00 AND y00))"
                } else if (gateNumber > 1) {
                    var start = 1
                    var suffix = "(x00 AND y00)"
                    while (start < gateNumber) {
                        suffix = "((x${String.format("%02d", start)} AND y${
                            String.format(
                                "%02d",
                                start
                            )
                        }) OR ((x${String.format("%02d", start)} XOR y${String.format("%02d", start)}) AND $suffix))"
                        start += 1
                    }
                    desired = "($desired XOR $suffix)"
                }
                println(zGate.operation())
                println(desired)
                var errorStart = 0
                var errorEnd = 0
                if (operation.length < desired.length) {
                    for (i in operation.indices) {
                        if (operation[i] != desired[i]) {
                            errorStart = i
                            break
                        }
                    }
                    for (i in operation.indices) {
                        if (operation[operation.length - 1 - i] != desired[desired.length - 1 - i]) {
                            errorEnd = operation.length - 1 - i
                            break
                        }
                    }
                } else {
                    for (i in desired.indices) {
                        if (operation[i] != desired[i]) {
                            errorStart = i
                            break
                        }
                    }
                    for (i in desired.indices) {
                        if (operation[operation.length - 1 - i] != desired[desired.length - 1 - i]) {
                            errorEnd = operation.length - 1 - i
                            break
                        }
                    }
                }
                while (operation[errorStart] != '(') {
                    errorStart -= 1
                }
                while (operation[errorStart] == '(' && errorStart > 0) {
                    errorStart -= 1
                }

                while (operation.substring(errorStart, errorEnd + 1).count { it == '(' } > operation.substring(
                        errorStart,
                        errorEnd + 1
                    ).count { it == ')' }) {
                    errorEnd += 1
                }
                val toReplace = operation.substring(errorStart, errorEnd + 1)
                println(toReplace)
                val errorGate = gates.first { gate ->
                    gate.operation() == toReplace
                }
                val swapGate = gates.first { gate ->
                    operation.replace(toReplace, gate.operation()) == desired
                }
                swapGate as OperationGate
                errorGate as OperationGate
                val newErrorGate = errorGate.copy(firstGate = swapGate.firstGate, secondGate = swapGate.secondGate, operation = swapGate.operation)
                val newSwapGate = swapGate.copy(firstGate = errorGate.firstGate, secondGate = errorGate.secondGate, operation = errorGate.operation)
                gates.remove(errorGate)
                gates.remove(swapGate)
                gates.add(newSwapGate)
                gates.add(newErrorGate)
                var changeNames = mutableListOf(newSwapGate.name, newErrorGate.name)
                while (changeNames.isNotEmpty()) {
                    val nextNameChanges = mutableListOf<String>()
                    gates = gates.map { gate ->
                        when (gate) {
                            is StartGate -> gate
                            is OperationGate -> {
                                when (gate) {
                                    swapGate -> {
                                        newSwapGate
                                    }

                                    errorGate -> {
                                        newErrorGate
                                    }

                                    else -> {
                                        var newGate = gate
                                        if (gate.firstGate.name in changeNames) {
                                            newGate =
                                                newGate.copy(firstGate = gates.first { it.name == gate.firstGate.name })
                                            nextNameChanges.add(gate.name)
                                        }
                                        if (gate.secondGate.name in changeNames) {
                                            newGate =
                                                newGate.copy(secondGate = gates.first { it.name == gate.secondGate.name })
                                            nextNameChanges.add(gate.name)
                                        }
                                        newGate
                                    }
                                }
                            }

                            else -> gate
                        }
                    }.toMutableList()
                    changeNames = nextNameChanges
                }
                swaps.add(swapGate.name)
                swaps.add(errorGate.name)
                println(swaps)
                break
            }
        }
    }
    return swaps.sorted().joinToString(",")
}

data class ValueGate(val name: String, val value: Boolean)
interface Gate {
    fun operation(): String

    val name: String
}

data class StartGate(override val name: String, val value: Boolean) : Gate {
    override fun operation() = name
}

data class OperationGate(override val name: String, var firstGate: Gate, var secondGate: Gate, val operation: String) :
    Gate {
    override fun operation(): String {
        val firstOp = firstGate.operation()
        val secondOp = secondGate.operation()
        return if (firstOp.length < secondOp.length) {
            "($firstOp $operation $secondOp)"
        } else if (secondOp.length < firstOp.length) {
            "($secondOp $operation $firstOp)"
        } else {
            if (firstOp.length == 3) {
                if (firstOp.contains("x")) {
                    "($firstOp $operation $secondOp)"
                } else {
                    "($secondOp $operation $firstOp)"
                }
            } else {
                if (firstOp.filter(Char::isDigit) > secondOp.filter(Char::isDigit)) {
                    "($firstOp $operation $secondOp)"
                } else {
                    "($secondOp $operation $firstOp)"
                }
            }
        }
    }
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2024/day24/Day24_test")
    check(part1(testInput) == 2024L)

    val input = readInput("src/main/kotlin/year2024/day24/Day24")
    println(part1(input))
    println(part2(input))
}
