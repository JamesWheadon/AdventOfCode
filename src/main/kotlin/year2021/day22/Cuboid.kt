package year2021.day22

import kotlin.math.max
import kotlin.math.min

class Cuboid(private val xRange: Pair<Int, Int>, val yRange: Pair<Int, Int>, private val zRange: Pair<Int, Int>) {

    fun cuboidOff(
        offXRange: Pair<Int, Int>,
        offYRange: Pair<Int, Int>,
        offZRange: Pair<Int, Int>,
    ): MutableList<Cuboid> {
        if (intersect(xRange, offXRange) && intersect(yRange, offYRange) && intersect(zRange, offZRange)) {
            val xIntersect = Pair(max(offXRange.first, xRange.first), min(offXRange.second, xRange.second))
            val yIntersect = Pair(max(offYRange.first, yRange.first), min(offYRange.second, yRange.second))
            val zIntersect = Pair(max(offZRange.first, zRange.first), min(offZRange.second, zRange.second))
            val newOffRegion = Cuboid(xIntersect, yIntersect, zIntersect)
            return split(newOffRegion)
        }
        return mutableListOf(this)
    }

    private fun split(newOffRegion: Cuboid): MutableList<Cuboid> {
        val newCuboids = mutableListOf<Cuboid>()
        if (newOffRegion.xRange.first > xRange.first) {
            newCuboids.add(Cuboid(Pair(xRange.first, newOffRegion.xRange.first - 1), yRange, zRange))
        }
        if (newOffRegion.xRange.second < xRange.second) {
            newCuboids.add(Cuboid(Pair(newOffRegion.xRange.second + 1, xRange.second), yRange, zRange))
        }
        if (newOffRegion.yRange.first > yRange.first) {
            newCuboids.add(Cuboid(newOffRegion.xRange, Pair(yRange.first, newOffRegion.yRange.first - 1), zRange))
        }
        if (newOffRegion.yRange.second < yRange.second) {
            newCuboids.add(Cuboid(newOffRegion.xRange, Pair(newOffRegion.yRange.second + 1, yRange.second), zRange))
        }
        if (newOffRegion.zRange.first > zRange.first) {
            newCuboids.add(
                Cuboid(
                    newOffRegion.xRange,
                    newOffRegion.yRange,
                    Pair(zRange.first, newOffRegion.zRange.first - 1),
                ),
            )
        }
        if (newOffRegion.zRange.second < zRange.second) {
            newCuboids.add(
                Cuboid(
                    newOffRegion.xRange,
                    newOffRegion.yRange,
                    Pair(newOffRegion.zRange.second + 1, zRange.second),
                ),
            )
        }
        return newCuboids
    }

    private fun intersect(range: Pair<Int, Int>, offRange: Pair<Int, Int>): Boolean {
        return (range.first >= offRange.first && range.first <= offRange.second) || (range.second >= offRange.first && range.second <= offRange.second) ||
            (offRange.first >= range.first && offRange.first <= range.second) || (offRange.second >= range.first && offRange.second <= range.second)
    }

    fun size(): Long {
        return (xRange.second - xRange.first + 1).toLong() *
            (yRange.second - yRange.first + 1).toLong() *
            (zRange.second - zRange.first + 1).toLong()
    }

    override fun toString(): String {
        return "Cuboid(xRange=$xRange, yRange=$yRange, zRange=$zRange)"
    }
}
