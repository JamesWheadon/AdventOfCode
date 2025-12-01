package year2020.day19

import check
import readInput

fun part1(input: List<String>): Int {
    val rules = input.takeWhile { it.isNotEmpty() }.map { rule ->
        if (rule.contains("\"")) {
            Rule(
                rule.split(":")[0].toInt(),
                rule.split(": ")[1],
                listOf(rule.dropWhile { it != '"' }.dropLastWhile { it != '"' }.removePrefix("\"").removeSuffix("\""))
            )
        } else {
            Rule(rule.split(":")[0].toInt(), rule.split(": ")[1])
        }
    }
    while (rules.any { it.match.isEmpty() }) {
        rules.filter { rule -> rule.match.isEmpty() }
            .filter { rule ->
                rule.dependentRules(rules).all { it.match.isNotEmpty() }
            }.forEach { rule ->
                rule.createRule(rules)
            }
    }
    rules.filter { it.match.isNotEmpty() }
    return input.takeLastWhile { it.isNotEmpty() }
        .count { message -> rules.first { it.number == 0 }.match.contains(message) }
}

fun part2(input: List<String>): Int {
    val rules = input.takeWhile { it.isNotEmpty() }.map { rule ->
        if (rule.contains("\"")) {
            Rule(
                rule.split(":")[0].toInt(),
                rule.split(": ")[1],
                listOf(rule.dropWhile { it != '"' }.dropLastWhile { it != '"' }.removePrefix("\"").removeSuffix("\""))
            )
        } else {
            Rule(rule.split(":")[0].toInt(), rule.split(": ")[1])
        }
    }
    while (rules.any { it.match.isEmpty() }) {
        rules.filter { rule -> rule.match.isEmpty() }
            .filter { rule ->
                rule.dependentRules(rules).all { it.match.isNotEmpty() }
            }.forEach { rule ->
                rule.createRule(rules)
            }
    }
    rules.filter { it.match.isNotEmpty() }
    return input.takeLastWhile { it.isNotEmpty() }
        .count { message -> rules.first { it.number == 0 }.matches(message, rules) }
}

data class Rule(val number: Int, val matchString: String, var match: List<String> = listOf()) {
    fun dependentRules(rules: List<Rule>): List<Rule> =
        matchString.replace("| ", "")
            .split(" ")
            .map { rule -> rule.filter { it.isDigit() }.toInt() }
            .map { ruleNumber -> rules.first { it.number == ruleNumber } }
            .distinct()
            .filter { it != this }

    fun createRule(rules: List<Rule>) {
        match = matchString.split("|").flatMap<String, String> { pattern ->
            pattern.split(" ")
                .filter { it.isNotEmpty() }
                .map {
                    val dependentRule = rules.first { rule -> rule.number.toString() == it }
                    if (dependentRule == this) {
                        listOf(number.toString())
                    } else {
                        dependentRule.match
                    }
                }
                .fold(mutableListOf()) { acc, strings ->
                    if (acc.isEmpty()) {
                        strings.toMutableList()
                    } else {
                        strings.flatMap { s1 ->
                            acc.map { acc1 ->
                                (acc1 + s1)
                            }
                        }.toMutableList()
                    }
                }
        }.distinct()
    }

    fun matches(message: String, rules: List<Rule>): Boolean {
        val (complex, simple) = match.partition { possible -> possible.any { it.isDigit() } }
        return if (simple.contains(message)) {
            true
        } else {
            complex.any { matchPattern ->
                val start = matchPattern.takeWhile { c -> !c.isDigit() }
                val end = matchPattern.takeLastWhile { c -> !c.isDigit() }
                if (message.startsWith(start) && message.endsWith(end) && message.length >= start.length + end.length) {
                    val remaining = message.removePrefix(start).removeSuffix(end)
                    val middle = matchPattern.removePrefix(start).removeSuffix(end).filter { !it.isDigit() }
                    if (middle.isEmpty()) {
                        val rule = rules.first { it.number == matchPattern.filter { it.isDigit() }.toInt() }
                        rule.matches(remaining, rules)
                    } else {
                        val remainingPattern = matchPattern.removePrefix(start).removeSuffix(end)
                        val middleSection = remainingPattern.filter { !it.isDigit() }
                        val startingRule = rules.first { remainingPattern.takeWhile { it.isDigit() }.toInt() == it.number }
                        val endingRule = rules.first { remainingPattern.takeLastWhile { it.isDigit() }.toInt() == it.number }
                        val indices = mutableListOf<Int>()
                        while (!indices.contains(-1)) {
                            indices.add(remaining.indexOf(middleSection, (indices.lastOrNull()?.plus(1)) ?: 0))
                        }
                        indices.remove(-1)
                        indices.any { index ->
                            startingRule.matches(remaining.substring(0, index), rules) && endingRule.matches(remaining.substring(index + middleSection.length), rules)
                        }
                    }
                } else {
                    false
                }
            }
        }
    }
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2020/day19/Day19_test")
    check(part1(testInput), 2)

    val input = readInput("src/main/kotlin/year2020/day19/Day19")
    println(part1(input))
    println(part2(listOf("8: 42 | 42 8", "11: 42 31 | 42 11 31").plus(input.minus(listOf("8: 42", "11: 42 31")))))
}
