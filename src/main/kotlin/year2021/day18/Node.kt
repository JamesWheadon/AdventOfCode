package year2021.day18

import kotlin.math.ceil
import kotlin.math.floor

class Node(var parent: Node?, private var value: Int?) {
    var children = mutableListOf<Node>()
    private var depth = findDepth()

    private fun findDepth(): Int {
        var depth = 0
        var currentNode = this
        while (currentNode.parent != null) {
            depth += 1
            currentNode = currentNode.parent!!
        }
        return depth
    }

    fun addDigit(digit: Int) {
        val pairNodes = mutableListOf<Node>()
        getPairNodes(pairNodes)
        val first = pairNodes.first { it.children.size < 2 }
        first.children.add(Node(first, digit))
    }

    fun addNode() {
        val pairNodes = mutableListOf<Node>()
        getPairNodes(pairNodes)
        val first = pairNodes.first { it.children.size < 2 }
        first.children.add(Node(first, null))
    }

    private fun getPairNodes(pairNodes: MutableList<Node>) {
        children.filter { it.value == null }.forEach { it.getPairNodes(pairNodes) }
        pairNodes.add(this)
    }

    fun reduceExplode(): Boolean {
        val pairNodes = mutableListOf<Node>()
        getPairNodes(pairNodes)
        val explodeNodes = pairNodes.filter { it.depth == 4 }
        return if (explodeNodes.isNotEmpty()) {
            val nodes = mutableListOf<Node>()
            val first = explodeNodes.first()
            search(first, nodes)
            first.explode(nodes)
            false
        } else {
            true
        }
    }

    private fun explode(nodes: MutableList<Node>) {
        val position = nodes.indexOf(this)
        if (position > 0) {
            nodes[position - 1].value = nodes[position - 1].value?.plus(children[0].value!!)
        }
        if (position < nodes.lastIndex) {
            nodes[position + 1].value = nodes[position + 1].value?.plus(children[1].value!!)
        }
        this.value = 0
        this.children = mutableListOf()
    }

    fun reduceSplit(): Boolean {
        val valueNodes = mutableListOf<Node>()
        getValueNodes(valueNodes)
        val splitNodes = valueNodes.filter { it.value!! > 9 }
        return if (splitNodes.isNotEmpty()) {
            splitNodes.first().split()
            false
        } else {
            true
        }
    }

    private fun getValueNodes(valueNodes: MutableList<Node>) {
        children.forEach {
            if (it.value != null) {
                valueNodes.add(it)
            } else {
                it.getValueNodes(valueNodes)
            }
        }
    }

    private fun split() {
        val leftNode = Node(this, floor(value!!.toDouble() / 2).toInt())
        val rightNode = Node(this, ceil(value!!.toDouble() / 2).toInt())
        value = null
        children.add(leftNode)
        children.add(rightNode)
    }

    private fun search(node: Node, nodes: MutableList<Node>) {
        children.forEach {
            if (it.value != null || it == node) {
                nodes.add(it)
            } else {
                it.search(node, nodes)
            }
        }
    }

    fun findDepths() {
        depth = findDepth()
        children.forEach { it.findDepths() }
    }

    fun getMagnitude(): Int {
        val magnitude: Int = if (value == null) {
            3 * children[0].getMagnitude() + 2 * children[1].getMagnitude()
        } else {
            value!!
        }
        return magnitude
    }
}
