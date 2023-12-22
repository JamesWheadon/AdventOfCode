package year2021.day23

class TreeNode(val agents: MutableList<Agent>, val parent: TreeNode?) {

    var cost = 0
    var estimateCost = 0
    var moves = 0
    private var children = mutableListOf<TreeNode>()
    private var searched = false

    fun getLeaves(leafNodes: MutableList<TreeNode>) {
        if (!searched) {
            leafNodes.add(this)
        } else {
            children.forEach {
                it.getLeaves(leafNodes)
            }
        }
    }

    fun getEstimate() {
        estimateCost = 0
        agents.forEach {
            estimateCost += it.costEstimate()
        }
        agents.filter { it.position.y == 2 }.forEach { agent ->
            if (agent.position.roomType == agent.type && agents.any { it.position.x == agent.position.x && it.position.y > agent.position.y && it.type != agent.type }) {
                estimateCost += ((agent.position.y * 2 + 1) * agent.moveCost)
            }
        }
        val types = agents.map { it.type }.distinct()
        types.forEach { type ->
            val agentsOfType = agents.filter { it.type == type }
            val agentsInRoom = agentsOfType.filter { it.position.roomType == type }
            val spaces = (1..agentsOfType.size).toMutableList()
            agentsInRoom.forEach {
                spaces.remove(it.position.y - 1)
            }
            spaces.forEach {
                estimateCost += (it - 1) * agentsOfType.first().moveCost
            }
        }
    }

    override fun toString(): String {
        return "TreeNode(agents=$agents, cost=$cost)"
    }

    fun samePositions(newAgents: MutableList<Agent>): Boolean {
        var same = true
        newAgents.forEach { newAgent ->
            same = same && agents.any { it.position == newAgent.position && it.type == newAgent.type }
        }
        return same
    }

    fun roomAvailable(type: Char): Boolean {
        return agents.none { it.position.roomType == type && it.type != type }
    }

    fun possibleMoves(agent: Agent): MutableList<Node> {
        val possibleMoves = mutableListOf<Node>()
        agent.position.getPossibleMoves(agents, possibleMoves)
        return possibleMoves
    }
}
