package year2021.day23

class Node(val y: Int, val x: Int, val roomType: Char?, val stop: Boolean) {

    val connectedTo = mutableListOf<Node>()
    override fun toString(): String {
        return "Node(x=$x, y=$y)"
    }

    fun getPossibleMoves(agents: MutableList<Agent>, possibleMoves: MutableList<Node>) {
        connectedTo.forEach { node ->
            if (!possibleMoves.contains(node) && agents.none { it.position.x == node.x && it.position.y == node.y }) {
                possibleMoves.add(node)
                node.getPossibleMoves(agents, possibleMoves)
            }
        }
    }
}
