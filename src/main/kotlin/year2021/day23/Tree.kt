package year2021.day23

class Tree(private val treeRoot: TreeNode) {
    fun getLeaves(): MutableList<TreeNode> {
        val leafNodes = mutableListOf<TreeNode>()
        treeRoot.getLeaves(leafNodes)
        return leafNodes
    }
}
