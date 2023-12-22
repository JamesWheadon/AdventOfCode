package year2023.day20

import readInput

fun part1(input: List<String>): Int {
    val moduleNames = input.map { line ->
        val parts = line.split(" -> ")
        val name = parts[0].filter { it.isLetter() }
        val module = if (parts[0].contains("%")) {
            FlipFLop(name, false)
        } else if (parts[0].contains("&")) {
            Conjunction(name)
        } else {
            Broadcast(name)
        }
        Pair(module, parts[1].split(", "))
    }
    val modules = moduleNames.map { it.first }.toMutableList()
    val newModules = mutableListOf<Module>()
    moduleNames.forEach {
        val module = it.first
        it.second.forEach { dest ->
            if (modules.none { mod -> mod.name == dest }) {
                val destModule = Output(dest)
                newModules.add(destModule)
                module.destinations.add(destModule)
            } else {
                module.destinations.add(modules.first { mod -> mod.name == dest })
            }
        }
    }
    modules.addAll(newModules)
    val button = Button(broadcast = modules.first { it.name == "broadcaster" })
    modules.add(0, button)
    modules.forEach { it.setUp() }
    for (i in 1..1000) {
        val moduleQueue: MutableList<Triple<Module, Boolean, Module>> = mutableListOf(Triple(button, false, button))
        while (moduleQueue.isNotEmpty()) {
            val current = moduleQueue.removeFirst()
            val received = current.first.receivePulse(current.second, current.third)
            moduleQueue.addAll(received)
        }
    }
    return modules.sumOf { it.highSent } * modules.sumOf { it.lowSent }
}

fun part2(input: List<String>): Long {
    val moduleNames = input.map { line ->
        val parts = line.split(" -> ")
        val name = parts[0].filter { it.isLetter() }
        val module = if (parts[0].contains("%")) {
            FlipFLop(name, false)
        } else if (parts[0].contains("&")) {
            Conjunction(name)
        } else {
            Broadcast(name)
        }
        Pair(module, parts[1].split(", "))
    }
    val modules = moduleNames.map { it.first }.toMutableList()
    val newModules = mutableListOf<Module>()
    moduleNames.forEach {
        val module = it.first
        it.second.forEach { dest ->
            if (modules.none { mod -> mod.name == dest }) {
                val destModule = Output(dest)
                newModules.add(destModule)
                module.destinations.add(destModule)
            } else {
                module.destinations.add(modules.first { mod -> mod.name == dest })
            }
        }
    }
    modules.addAll(newModules)
    val button = Button(broadcast = modules.first { it.name == "broadcaster" })
    modules.add(0, button)
    modules.forEach { it.setUp() }
    val rxInputs = mutableListOf("kh", "lz", "tg", "hn")
    var press = 0L
    var final = 1L
    while (rxInputs.isNotEmpty()) {
        press++
        val moduleQueue: MutableList<Triple<Module, Boolean, Module>> = mutableListOf(Triple(button, false, button))
        while (moduleQueue.isNotEmpty()) {
            val current = moduleQueue.removeFirst()
            val received = current.first.receivePulse(current.second, current.third)
            if (received.any { it.first.name in rxInputs && !it.second }) {
                received.filter { it.first.name in rxInputs && !it.second }.forEach {
                    rxInputs.remove(it.first.name)
                    final *= press
                }
            }
            moduleQueue.addAll(received)
        }
    }
    return final
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2023/day20/Day20_test")
    val testInput2 = readInput("src/main/kotlin/year2023/day20/Day20_test2")
    check(part1(testInput) == 32000000)
    check(part1(testInput2) == 11687500)

    val input = readInput("src/main/kotlin/year2023/day20/Day20")
    println(part1(input))
    println(part2(input))
}

interface Module {
    val name: String
    val destinations: MutableList<Module>
    var lowSent: Int
    var highSent: Int

    fun setUp()
    fun receivePulse(high: Boolean, source: Module): List<Triple<Module, Boolean, Module>>
    fun setInput(input: Module)
}

class Button(override val name: String = "Button", broadcast: Module) : Module {
    override val destinations: MutableList<Module> = mutableListOf(broadcast)
    override var lowSent = 0
    override var highSent = 0

    override fun setUp() {
        destinations.forEach { it.setInput(this) }
    }

    override fun setInput(input: Module) { //no setup required
    }

    override fun receivePulse(high: Boolean, source: Module): List<Triple<Module, Boolean, Module>> {
        lowSent += destinations.size
        return destinations.map { Triple(it, false, this) }
    }
}

class Broadcast(override val name: String) : Module {
    override val destinations: MutableList<Module> = mutableListOf()
    override var lowSent = 0
    override var highSent = 0

    override fun setUp() {
        destinations.forEach { it.setInput(this) }
    }

    override fun setInput(input: Module) { //no setup required
    }

    override fun receivePulse(high: Boolean, source: Module): List<Triple<Module, Boolean, Module>> {
        if (high) highSent += destinations.size else lowSent += destinations.size
        return destinations.map { Triple(it, high, this) }
    }
}

class FlipFLop(override val name: String, private var on: Boolean) : Module {
    override val destinations: MutableList<Module> = mutableListOf()
    override var lowSent = 0
    override var highSent = 0

    override fun setUp() {
        destinations.forEach { it.setInput(this) }
    }

    override fun setInput(input: Module) { //no setup required
    }

    override fun receivePulse(high: Boolean, source: Module): List<Triple<Module, Boolean, Module>> {
        if (!high) {
            return if (on) {
                on = false
                lowSent += destinations.size
                destinations.map { Triple(it, false, this) }
            } else {
                on = true
                highSent += destinations.size
                destinations.map { Triple(it, true, this) }
            }
        }
        return mutableListOf()
    }
}

class Conjunction(override val name: String) : Module {
    override val destinations: MutableList<Module> = mutableListOf()
    private val inputs: MutableMap<Module, Boolean> = mutableMapOf()
    override var lowSent = 0
    override var highSent = 0

    override fun setUp() {
        destinations.forEach { it.setInput(this) }
    }

    override fun setInput(input: Module) {
        inputs[input] = false
    }

    override fun receivePulse(high: Boolean, source: Module): List<Triple<Module, Boolean, Module>> {
        inputs[source] = high
        val allInputsHigh = inputs.values.all { it }
        return if (allInputsHigh) {
            lowSent += destinations.size
            destinations.map { Triple(it, false, this) }
        } else {
            highSent += destinations.size
            destinations.map { Triple(it, true, this) }
        }
    }
}

class Output(override val name: String) : Module {
    override val destinations: MutableList<Module> = mutableListOf()
    override var lowSent = 0
    override var highSent = 0

    override fun setUp() { //empty
    }

    override fun setInput(input: Module) { //empty
    }

    override fun receivePulse(high: Boolean, source: Module): List<Triple<Module, Boolean, Module>> {
        return listOf()
    }
}
