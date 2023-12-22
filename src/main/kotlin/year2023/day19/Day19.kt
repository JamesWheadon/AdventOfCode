package year2023.day19

import readInput
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow


fun part1(input: List<String>): Int {
    val flows = input.subList(0, input.indexOf("")).associate { flow ->
        val name = flow.split("{")[0]
        val conditions = flow.substring(flow.indexOf('{') + 1, flow.indexOf('}')).split(",").map { condition ->
            if (condition.contains(":")) {
                val property = condition.first()
                val lessThan = condition.contains('<')
                val value = condition.filter { it.isDigit() }.toInt()
                val target = condition.split(":")[1]
                { test: Part -> test.properties[property] in 0..<value == lessThan } to target
            } else {
                { _: Part -> true } to condition
            }
        }
        name to conditions
    }
    val parts = input.subList(input.indexOf("") + 1, input.lastIndex + 1).map { part ->
        Part(
            listOf('x', 'm', 'a', 's').zip(
                part.split(",").map { property -> property.filter { it.isDigit() }.toInt() }).toMap()
        )
    }
    val accepted = mutableListOf<Part>()
    parts.forEach { part ->
        var currentFlow = "in"
        while (currentFlow != "R" && currentFlow != "A") {
            run breaking@{
                flows[currentFlow]!!.forEach { check ->
                    if (check.first(part)) {
                        currentFlow = check.second
                        return@breaking
                    }
                }
            }
        }
        if (currentFlow == "A") {
            accepted.add(part)
        }
    }
    return accepted.sumOf { it.properties.values.sum() }
}

fun part2(input: List<String>): Long {
    val flows = input.subList(0, input.indexOf("")).map { flow ->
        val name = flow.split("{")[0]
        val conditions = flow.substring(flow.indexOf('{') + 1, flow.indexOf('}')).split(",")
        Pair(name, conditions)
    }
    val root = TreeNode("in", listOf())
    val toExplore = mutableListOf(root)
    while (toExplore.isNotEmpty()) {
        val current = toExplore.removeFirst()
        val flow = flows.first { it.first == current.flowName }
        flow.second.forEachIndexed { index, s ->
            val previous = flow.second.subList(0, index)
            val conditions = previous.map { oppositeCondition(it) }.toMutableList()
            val newNode = if (s.contains(":")) {
                conditions.add(s.split(":")[0])
                TreeNode(s.split(":")[1], conditions)
            } else {
                TreeNode(s, conditions)
            }
            current.children.add(newNode)
            if (newNode.flowName != "A" && newNode.flowName != "R") {
                toExplore.add(newNode)
            }
        }
    }
    return root.getRoutes().filter { route -> route.map { it.flowName }.contains("A") }
        .map { route -> route.map { it.condition }.filter { it.isNotEmpty() } }.sumOf { combinations ->
            val boundaries = combinations.flatten().map { combination ->
                if (combination.contains("<")) {
                    Pair(combination.first { it.isLetter() }, 1..<combination.filter { it.isDigit() }.toInt())
                } else {
                    Pair(combination.first { it.isLetter() }, (combination.filter { it.isDigit() }.toInt() + 1)..4000)
                }
            }.groupBy { it.first }.toMutableMap()
            val possibles = boundaries.values.map { ranges ->
                val finalRange = ranges.map { it.second }.reduce { acc, pair -> rangeIntersect(acc, pair) }
                (finalRange.last - finalRange.first + 1).toLong()
            }
            possibles.reduce { acc, i -> acc * i } * 4000.0.pow(4 - possibles.size).toLong()
        }
}

fun rangeIntersect(acc: IntRange, pair: IntRange): IntRange {
    return if (acc.intersect(pair).isEmpty()) {
        0..0
    } else {
        max(acc.first, pair.first)..min(acc.last, pair.last)
    }
}

fun oppositeCondition(condition: String): String {
    return if (condition.contains("<")) {
        "${condition.first()}>${condition.filter { it.isDigit() }.toInt() - 1}"
    } else {
        "${condition.first()}<${condition.filter { it.isDigit() }.toInt() + 1}"
    }
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2023/day19/Day19_test")
    check(part1(testInput) == 19114)
    check(part2(testInput) == 167409079868000L)

    val input = readInput("src/main/kotlin/year2023/day19/Day19")
    println(part1(input))
    println(part2(input))
}

data class Part(val properties: Map<Char, Int>)

class TreeNode(val flowName: String, val condition: List<String>) {
    val children = mutableListOf<TreeNode>()

    fun getRoutes(): MutableList<MutableList<TreeNode>> {
        return if (children.isEmpty()) {
            mutableListOf(mutableListOf(this))
        } else {
            children.map { node ->
                val childRoutes = node.getRoutes()
                childRoutes.forEach { it.add(0, this) }
                childRoutes
            }.flatten().toMutableList()
        }
    }

    override fun toString(): String {
        if (children.isEmpty()) {
            return "TreeNode(flowName='$flowName')"
        }
        return "TreeNode(flowName='$flowName', children=$children)"
    }
}
