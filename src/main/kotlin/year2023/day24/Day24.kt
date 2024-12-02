package year2023.day24

import readInput
import kotlin.math.ceil
import kotlin.math.floor

fun part1(input: List<String>, xBounds: LongRange, yBounds: LongRange): Int {
    val hailstones = input.map { line ->
        Hail(line.replace("@", ",").filter { it.isDigit() || it == ',' || it == '-' }.split(",").map { it.toLong() })
    }
    return hailstones.mapIndexed { index, hail ->
        val otherHail = hailstones.subList(index + 1, hailstones.size)
        otherHail.filter { hail.getXYIntercept(it, xBounds, yBounds) }.size
    }.sum()
}

fun part2(input: List<String>): Long {
//    val hailstones = input.map { line ->
//        Hail(line.replace("@", ",").filter { it.isDigit() || it == ',' || it == '-' }.split(",").map { it.toLong() })
//    }
//
//    val range = (hailstones.maxOf { maxOf(it.vx, it.vy, it.vz) } * 1.5).toLong()
//    for (vx in -range..range) {
//        for (vy in -range..range) {
//            for (vz in -range..range) {
//                if (vx == 0L || vy == 0L || vz == 0L) {
//                    continue
//                }
//                val rock = Hail(listOf(0L, 0L, 0L, vx, vy, vz))
//                rock.findStartPoint(hailstones[1], hailstones[2])
//
//                var hitall = true
//                for (element in hailstones) {
//                    val h = element
//                    val u: Long = if (h.vx != vx) {
//                        (x - h.x) / (h.vx - vx)
//                    } else if (h.vy != vy) {
//                        (y - h.y) / (h.vy - vy)
//                    } else if (h.vz != vz) {
//                        (z - h.z) / (h.vz - vz)
//                    } else {
//                        throw RuntimeException()
//                    }
//
//                    if ((x + u * vx != h.x + u * h.vx) || (y + u * vy != h.y + u * h.vy) || (z + u * vz != h.z + u * h.vz)) {
//                        hitall = false
//                        break
//                    }
//                }
//
//                if (hitall) {
//                    System.out.printf("%d %d %d   %d %d %d   %d %n", x, y, z, vx, vy, vz, x + y + z)
//                    return x + y + z
//                }
//            }
//        }
//    }
    return 0L
}

fun main() {
    val testInput = readInput("src/main/kotlin/year2023/day24/Day24_test")
    check(part1(testInput, 7L..27L, 7L..27L) == 2)
    check(part2(testInput) == 47L)

    val input = readInput("src/main/kotlin/year2023/day24/Day24")
    println(part1(input, 200000000000000..400000000000000, 200000000000000..400000000000000))
    println(part2(input))
}

class Hail(values: List<Long>) {
    var x: Long = values[0]
    var y: Long = values[1]
    var z: Long = values[2]
    var vx: Long = values[3]
    var vy: Long = values[4]
    var vz: Long = values[5]

    fun getXYIntercept(other: Hail, xBounds: LongRange, yBounds: LongRange): Boolean {
        val t2 = (vx * (other.y - y) - vy * (other.x - x)).toDouble() / (other.vx * vy - other.vy * vx).toDouble()
        val t1 = (other.x - x + t2 * other.vx) / vx.toDouble()
        return if (t1 < 0 || t2 < 0 || t1 == Double.POSITIVE_INFINITY || t2 == Double.POSITIVE_INFINITY) {
            false
        } else {
            val xIntercept = x + t1 * vx
            val yIntercept = y + t1 * vy
            floor(xIntercept).toLong() in xBounds && ceil(xIntercept).toLong() in xBounds
                    && floor(yIntercept).toLong() in yBounds && ceil(yIntercept).toLong() in yBounds
        }
    }

    override fun toString(): String {
        return "Hail(x=$x, y=$y, z=$z, vx=$vx, vy=$vy, vz=$vz)"
    }

    fun findStartPoint(first: Hail, second: Hail): Boolean {
        // Find starting point for rock that will intercept first two hailstones (x,y) on this trajectory

        // simultaneous linear equation (from part 1):
        // H1:  x = A + a*t   y = B + b*t
        // H2:  x = C + c*u   y = D + d*u
        //
        //  t = [ d ( C - A ) - c ( D - B ) ] / ( a * d - b * c )
        //
        // Solve for origin of rock intercepting both hailstones in x,y:
        //     x = A + a*t - vx*t   y = B + b*t - vy*t
        //     x = C + c*u - vx*u   y = D + d*u - vy*u
        val A: Long = first.x
        val a: Long = first.vx - vx
        val B: Long = first.y
        val b: Long = first.vy - vy
        val C: Long = second.x
        val c: Long = second.vx - vx
        val D: Long = second.y
        val d: Long = second.vy - vy


        // skip if division by 0
        if (c == 0L || (a * d) - (b * c) == 0L) {
            return false
        }


        // Rock intercepts H1 at time t
        val t = (d * (C - A) - c * (D - B)) / ((a * d) - (b * c))

        x = first.x + first.vx * t - vx * t
        y = first.y + first.vy * t - vy * t
        z = first.z + first.vz * t - vz * t
        return true
    }
}
