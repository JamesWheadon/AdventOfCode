package year2021.day16

import readInput
import java.math.BigInteger

fun literalValueVersion(version: Int, binaryPacket: String): Pair<Int, String> {
    var value = ""
    val groups = binaryPacket.substring(6, binaryPacket.length).chunked(5)
    groups.forEach {
        value += it.substring(1, it.length)
        if (it.first() == '0') {
            return Pair(version, binaryPacket.substring(6 + (groups.indexOf(it) + 1) * 5))
        }
    }
    return Pair(version, "")
}

fun decodePacketVersion(binaryPacket: String): Pair<Int, String> {
    val version = binaryPacket.substring(0, 3).toInt(2)
    val packageType = binaryPacket.substring(3, 6).toInt(2)
    return if (packageType == 4) {
        literalValueVersion(version, binaryPacket)
    } else {
        val lengthType = binaryPacket[6].toString().toInt(2)
        if (lengthType == 0) {
            val subPacketLength = binaryPacket.substring(7, 22).toInt(2)
            var total = 0
            var subPackets = binaryPacket.substring(22, 22 + subPacketLength)
            while (subPackets.isNotEmpty()) {
                val decodedPacket = decodePacketVersion(subPackets)
                total += decodedPacket.first
                subPackets = decodedPacket.second
            }
            total += version
            Pair(total, binaryPacket.substring(22 + subPacketLength))
        } else {
            val numberOfPackets = binaryPacket.substring(7, 18).toInt(2)
            var packets = binaryPacket.substring(18, binaryPacket.length)
            var total = 0
            for (i in 0..<numberOfPackets) {
                val decodePacket = decodePacketVersion(packets)
                total += decodePacket.first
                packets = decodePacket.second
            }
            total += version
            Pair(total, packets)
        }
    }
}

fun part1(input: List<String>): Int {
    val binaryPacket = BigInteger(input[0], 16).toString(2).padStart(input[0].length * 4, '0')
    return decodePacketVersion(binaryPacket).first
}

fun literalValue(binaryPacket: String): Pair<Long, String> {
    var value = ""
    val groups = binaryPacket.substring(6, binaryPacket.length).chunked(5)
    groups.forEach {
        value += it.substring(1, it.length)
        if (it.first() == '0') {
            return Pair(value.toLong(2), binaryPacket.substring(6 + (groups.indexOf(it) + 1) * 5))
        }
    }
    return Pair(value.toLong(2), "")
}

fun getScore(values: MutableList<Long>, packageType: Int): Long {
    when (packageType) {
        0 -> return values.sum()
        1 -> return values.reduce { acc, i -> acc * i }
        2 -> return values.minOrNull()!!
        3 -> return values.maxOrNull()!!
        5 -> return if (values[0] > values[1]) {
            1
        } else {
            0
        }
        6 -> return if (values[0] < values[1]) {
            1
        } else {
            0
        }
        7 -> return if (values[0] == values[1]) {
            1
        } else {
            0
        }
        else -> {
            print("x is neither 1 nor 2")
        }
    }
    return 0
}

fun decodePacket(binaryPacket: String): Pair<Long, String> {
    binaryPacket.substring(0, 3).toInt(2)
    val packageType = binaryPacket.substring(3, 6).toInt(2)
    return if (packageType == 4) {
        literalValue(binaryPacket)
    } else {
        val lengthType = binaryPacket[6].toString().toInt(2)
        if (lengthType == 0) {
            val subPacketLength = binaryPacket.substring(7, 22).toInt(2)
            val values = mutableListOf<Long>()
            var subPackets = binaryPacket.substring(22, 22 + subPacketLength)
            while (subPackets.isNotEmpty()) {
                val decodedPacket = decodePacket(subPackets)
                values.add(decodedPacket.first)
                subPackets = decodedPacket.second
            }
            Pair(getScore(values, packageType), binaryPacket.substring(22 + subPacketLength))
        } else {
            val numberOfPackets = binaryPacket.substring(7, 18).toInt(2)
            var packets = binaryPacket.substring(18, binaryPacket.length)
            val values = mutableListOf<Long>()
            for (i in 0..<numberOfPackets) {
                val decodePacket = decodePacket(packets)
                values.add(decodePacket.first)
                packets = decodePacket.second
            }
            Pair(getScore(values, packageType), packets)
        }
    }
}

fun part2(input: List<String>): Long {
    val binaryPacket = BigInteger(input[0], 16).toString(2).padStart(input[0].length * 4, '0')
    return decodePacket(binaryPacket).first
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2021/day16/Day16_test")
    check(part1(testInput) == 31)
    check(part2(testInput) == 54L)

    val input = readInput("src/main/kotlin/year2021/day16/Day16")
    println(part1(input))
    println(part2(input))
}
