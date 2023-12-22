package year2021.day23

class Map(private val agents: MutableList<Agent>) {

    private var openSearches = mutableListOf<TreeNode>()

    fun minimumCost(): Int {
        val startNode = TreeNode(agents, null)
        val searchTree = Tree(startNode)
        startNode.getEstimate()
        openSearches = searchTree.getLeaves()
        var minCostLeaf = openSearches.minByOrNull { it.cost + it.estimateCost }!!
        minCostLeaf.getEstimate()
        while (minCostLeaf.estimateCost != 0) {
            minCostLeaf.agents.filter { !it.done }.forEach { agent ->
                val homeOpen = minCostLeaf.roomAvailable(agent.type)
                var possibleMoves = minCostLeaf.possibleMoves(agent).filter { it.stop }
                possibleMoves = getPossibleMoves(possibleMoves, homeOpen, agent)
                possibleMoves.forEach { node ->
                    val newAgentPosition = agent.copy(position = node)
                    val newAgents = minCostLeaf.agents.toList().toMutableList()
                    newAgents[newAgents.indexOf(agent)] = newAgentPosition
                    if (agent.position.roomType == agent.type) {
                        agent.done =
                            newAgents.none { it.position.x == agent.position.x && it.type != agent.type && it.position.y > agent.position.y }
                    }
                    val newTreeNode = TreeNode(newAgents, minCostLeaf)
                    newTreeNode.cost = minCostLeaf.cost + agent.costToNode(node)
                    newTreeNode.getEstimate()
                    newTreeNode.moves = minCostLeaf.moves + 1
                    if (nodeNotInSearch(newTreeNode)) {
                        openSearches.add(newTreeNode)
                    }
                }
            }
            openSearches.remove(minCostLeaf)
            minCostLeaf = openSearches.minByOrNull { it.cost + it.estimateCost }!!
        }
        var current: TreeNode?
        current = minCostLeaf
        val route = mutableListOf<TreeNode>()
        while (current != null) {
            route.add(current)
            current = current.parent
        }
        return minCostLeaf.cost
    }

    private fun getPossibleMoves(
        possibleMoves: List<Node>,
        homeOpen: Boolean,
        agent: Agent,
    ): List<Node> {
        var possibleMoves1 = possibleMoves
        possibleMoves1 = when {
            homeOpen && agent.position.roomType != null -> {
                possibleMoves1.filter { move ->
                    move.roomType == null || (
                        move.roomType == agent.type && move.y == possibleMoves1.filter { it.roomType == agent.type }
                            .maxOf { it.y } && move.y > agent.position.y
                        )
                }.toMutableList()
            }

            homeOpen && agent.position.roomType == null -> {
                possibleMoves1.filter { move ->
                    (
                        move.roomType == agent.type && move.y == possibleMoves1.filter { it.roomType == agent.type }
                            .maxOf { it.y }
                        )
                }.toMutableList()
            }

            !homeOpen && agent.position.roomType != null -> {
                possibleMoves1.filter { it.roomType == null }.toMutableList()
            }

            else -> {
                mutableListOf()
            }
        }
        return possibleMoves1
    }

    private fun nodeNotInSearch(newTreeNode: TreeNode): Boolean {
        return openSearches.none { it.samePositions(newTreeNode.agents) }
    }
}
