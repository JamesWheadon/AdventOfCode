package year2021.day18

class Tree {
    lateinit var root: Node
    var size = 0
    private var stageDone = false

    fun addDigit(digit: Int) {
        root.addDigit(digit)
    }

    fun addNode() {
        root.addNode()
    }

    fun reduce() {
        var done = false
        while (!done) {
            stageDone = false
            done = root.reduceExplode()
            if (done) {
                done = root.reduceSplit()
            }
        }
    }

    fun findDepths() {
        root.findDepths()
    }

    fun getMagnitude(): Int {
        return root.getMagnitude()
    }
}
