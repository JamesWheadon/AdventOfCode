package year2024.day22

import readInput

fun part1(input: List<String>): Long {
    return input.map { it.toLong() }.sumOf { start ->
        var secret = start
        for (i in 1..2000) {
            secret = pruneAndMix(secret, secret * 64)
            secret = pruneAndMix(secret, secret / 32)
            secret = pruneAndMix(secret, secret * 2048)
        }
        secret
    }
}

fun part2(input: List<String>): Int {
    val pricesChanges = mutableMapOf<String, Int>()
    input.map { it.toLong() }.forEach { start ->
        val sellerPriceChanges = mutableMapOf<String, Int>()
        val prices = mutableListOf(Price(start.toString().last().digitToInt(), 0))
        var secret = start
        for (i in 1..2000) {
            secret = pruneAndMix(secret, secret * 64)
            secret = pruneAndMix(secret, secret / 32)
            secret = pruneAndMix(secret, secret * 2048)
            val price = secret.toString().last().digitToInt()
            prices.add(Price(price, price - prices.last().price))
            if (prices.size >= 5) {
                val sequence = prices.takeLast(4).map { it.change }.joinToString(",")
                if (sequence !in sellerPriceChanges) {
                    sellerPriceChanges[sequence] = price
                }
            }
        }
        sellerPriceChanges.forEach { (sequence, price) ->
            if (sequence in pricesChanges) {
                pricesChanges[sequence] = pricesChanges[sequence]!! + price
            } else {
                pricesChanges[sequence] = price
            }
        }
    }
    return pricesChanges.values.max()
}

private fun pruneAndMix(secret: Long, result: Long) =
    (secret xor result) % 16777216

data class Price(val price: Int, val change: Int)

fun main() {
    check(part1(readInput("src/main/kotlin/year2024/day22/Day22_test1")) == 37327623L)
    check(part2(readInput("src/main/kotlin/year2024/day22/Day22_test2")) == 23)

    val input = readInput("src/main/kotlin/year2024/day22/Day22")
    println(part1(input))
    println(part2(input))
}
