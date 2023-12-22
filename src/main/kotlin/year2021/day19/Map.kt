package year2021.day19

import kotlin.math.absoluteValue

class Map(private val scanners: MutableList<Scanner>) {

    private val centralScanner = scanners.first()
    private val unknownScanners = scanners.filter { it != centralScanner }.toMutableList()
    private val scannerPositions = mutableListOf(Triple(0, 0, 0))

    fun findAllBeacons(): Int {
        scanners.forEach { it.getRelativeBeaconPositions() }
        while (unknownScanners.isNotEmpty()) {
            scanners.filter { it != centralScanner }.forEach { firstScanner ->
                if (unknownScanners.contains(firstScanner)) {
                    findOverlaps(firstScanner)
                }
            }
        }
        return centralScanner.beacons.size
    }

    fun findFurthestBeacons(): Int {
        findAllBeacons()
        var maxDistance = 0
        scannerPositions.forEach { first ->
            scannerPositions.filter { it != first }.forEach {
                val distance = (first.first - it.first).absoluteValue +
                    (first.second - it.second).absoluteValue +
                    (first.third - it.third).absoluteValue
                if (distance > maxDistance) {
                    maxDistance = distance
                }
            }
        }
        return maxDistance
    }

    private fun findOverlaps(secondScanner: Scanner) {
        val sameBeacons = mutableListOf<Pair<Beacon, Beacon>>()
        centralScanner.relativePositions.forEachIndexed topLoop@{ firstIndex, row ->
            secondScanner.relativePositions.forEachIndexed { secondIndex, secondRow ->
                val matchingBeacons = mutableListOf<Pair<Int, Int>>()
                row.forEach { vector ->
                    if (vector != Triple(0, 0, 0)) {
                        secondRow.forEach { secondVector ->
                            if (vector != Triple(0, 0, 0) && sameLine(vector, secondVector)) {
                                matchingBeacons.add(Pair(row.indexOf(vector), secondRow.indexOf(secondVector)))
                            }
                        }
                    }
                }
                if (matchingBeacons.size >= 11) {
                    val firstBeacon = centralScanner.beacons[firstIndex]
                    val secondBeacon = secondScanner.beacons[secondIndex]
                    sameBeacons.add(Pair(firstBeacon, secondBeacon))
                }
            }
        }
        if (sameBeacons.size >= 12) {
            matchBeacons(sameBeacons, secondScanner)
        }
    }

    private fun matchBeacons(
        sameBeacons: MutableList<Pair<Beacon, Beacon>>,
        secondScanner: Scanner,
    ) {
        val secondPosition = findMapping(sameBeacons)
        scannerPositions.add(secondPosition)
        var orientationMapping = findOrientationMapping(secondPosition, sameBeacons.first())
        var startIndex = 1
        while (orientationMapping.flatten().filter { it == 0 }.size < 6) {
            orientationMapping = findOrientationMapping(secondPosition, sameBeacons[startIndex])
            startIndex += 1
        }
        secondScanner.getTrueBeaconPositions(secondPosition, orientationMapping)
        centralScanner.beacons.size
        secondScanner.beacons.forEach { newBeacon ->
            var included = false
            centralScanner.beacons.forEach {
                if (newBeacon.x == it.x && newBeacon.y == it.y && newBeacon.z == it.z) {
                    included = true
                }
            }
            if (!included) {
                centralScanner.beacons.add(newBeacon)
            }
        }
        centralScanner.getRelativeBeaconPositions()
        unknownScanners.remove(secondScanner)
    }

    private fun findOrientationMapping(
        secondPosition: Triple<Int, Int, Int>,
        beacons: Pair<Beacon, Beacon>,
    ): MutableList<MutableList<Int>> {
        val p = listOf(beacons.first.x, beacons.first.y, beacons.first.z)
        val q = listOf(beacons.second.x, beacons.second.y, beacons.second.z)
        val s = listOf(secondPosition.first, secondPosition.second, secondPosition.third)
        val orientationMatrix = MutableList(3) { MutableList(3) { 0 } }
        s.forEachIndexed { row, i ->
            q.forEachIndexed { column, j ->
                if ((p[row].absoluteValue - j.absoluteValue).absoluteValue == i.absoluteValue || p[row].absoluteValue + j.absoluteValue == i.absoluteValue) {
                    orientationMatrix[row][column] = (p[row] - i) / j
                }
            }
        }
        return orientationMatrix
    }

    private fun findMapping(sameBeacons: MutableList<Pair<Beacon, Beacon>>): Triple<Int, Int, Int> {
        var possiblePositions = mutableListOf<Triple<Int, Int, Int>>()
        sameBeacons.forEach { beaconPair ->
            val pairPositions = findPositions(beaconPair.first, beaconPair.second)
            possiblePositions = if (possiblePositions.isEmpty()) {
                pairPositions
            } else {
                possiblePositions.filter { pairPositions.contains(it) }.toMutableList()
            }
        }
        return possiblePositions.first()
    }

    private fun findPositions(f: Beacon, s: Beacon): MutableList<Triple<Int, Int, Int>> {
        val positions = mutableListOf<Triple<Int, Int, Int>>()
        val possibleFirst = findSingle(f.x, s)
        val possibleSecond = findSingle(f.y, s)
        val possibleThird = findSingle(f.z, s)
        possibleFirst.forEach { first ->
            possibleSecond.filter { it.second != first.second }.forEach { second ->
                possibleThird.filter { it.second != first.second && it.second != second.second }.forEach { third ->
                    positions.add(Triple(first.first, second.first, third.first))
                }
            }
        }
        return positions.distinct().toMutableList()
    }

    private fun findSingle(x: Int, s: Beacon): MutableList<Pair<Int, Int>> {
        val possible = mutableListOf<Pair<Int, Int>>()
        possible.add(Pair(x + s.x, 1))
        possible.add(Pair(x - s.x, 1))
        possible.add(Pair(x + s.y, 2))
        possible.add(Pair(x - s.y, 2))
        possible.add(Pair(x + s.z, 3))
        possible.add(Pair(x - s.z, 3))
        return possible
    }

    private fun sameLine(v: Triple<Int, Int, Int>, sV: Triple<Int, Int, Int>): Boolean {
        var same = true
        val absFirst = mutableListOf(v.first.absoluteValue, v.second.absoluteValue, v.third.absoluteValue)
        val absSecond = mutableListOf(sV.first.absoluteValue, sV.second.absoluteValue, sV.third.absoluteValue)
        absFirst.forEach { same = same && absSecond.contains(it) }
        return same
    }
}
