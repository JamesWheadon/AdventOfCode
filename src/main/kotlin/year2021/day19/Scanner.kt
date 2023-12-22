package year2021.day19

class Scanner {
    val beacons = mutableListOf<Beacon>()
    var relativePositions = mutableListOf<MutableList<Triple<Int, Int, Int>>>()

    fun getRelativeBeaconPositions() {
        relativePositions = mutableListOf()
        beacons.forEach { beacon ->
            val row = mutableListOf<Triple<Int, Int, Int>>()
            beacons.forEach {
                row.add(Triple(beacon.x - it.x, beacon.y - it.y, beacon.z - it.z))
            }
            relativePositions.add(row)
        }
    }

    fun getTrueBeaconPositions(
        scannerPos: Triple<Int, Int, Int>,
        oM: MutableList<MutableList<Int>>,
    ) {
        beacons.forEach {
            val tempX: Int = scannerPos.first + oM[0][0] * it.x + oM[0][1] * it.y + oM[0][2] * it.z
            val tempY: Int = scannerPos.second + oM[1][0] * it.x + oM[1][1] * it.y + oM[1][2] * it.z
            val tempZ: Int = scannerPos.third + oM[2][0] * it.x + oM[2][1] * it.y + oM[2][2] * it.z
            it.x = tempX
            it.y = tempY
            it.z = tempZ
        }
    }
}
