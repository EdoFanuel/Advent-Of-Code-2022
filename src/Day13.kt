import utils.Tree
import java.util.Stack
import kotlin.math.min
import kotlin.math.sign

fun buildTree(input: String): Tree<Int> {
    val stack = Stack<Tree<Int>>()
    val root = Tree<Int>()
    var currentValue = 0
    var hasValue = false
    var currentNode = root
    for (c in input) {
        when (c) {
            '[' -> {
                stack.push(currentNode)
                currentNode = Tree()        // first children
            }
            ']' -> {
                currentNode.value = if (hasValue) currentValue else null
                stack.peek().children += currentNode
                currentNode = stack.pop()   // go back to parent
                currentValue = 0
                hasValue = false
            }
            ',' -> {
                currentNode.value = if (hasValue) currentValue else null
                stack.peek().children += currentNode
                currentNode = Tree()        // next children
                currentValue = 0
                hasValue = false
            }
            in '0'..'9' -> {
                currentValue = currentValue * 10 + c.digitToInt()
                hasValue = true
            }
        }
    }
    return root
}

fun compare(a: Tree<Int>, b: Tree<Int>): Int {
    if (a.isLeaf() && a.hasValue() && b.isLeaf() && b.hasValue()) return (a.value!! - b.value!!).sign
    val childrenA = if (a.isLeaf() && a.hasValue()) mutableListOf(Tree(a.value)) else a.children
    val childrenB = if (b.isLeaf() && b.hasValue()) mutableListOf(Tree(b.value)) else b.children

    for(i in 0 until min(childrenA.size, childrenB.size)) {
        when (compare(childrenA[i], childrenB[i])) {
            -1 -> return -1
            1 -> return 1
            0 -> {}  //do nothing
        }
    }
    return (childrenA.size - childrenB.size).sign
}

fun main() {
    fun part1(input: List<String>): Int {
        var result = 0
        var counter = 1
        for (i in input.indices step 3) {
            val leftTree = buildTree(input[i])
            val rightTree = buildTree(input[i + 1])
            val sign = compare(leftTree, rightTree)
            if (sign <= 0) {
                result += counter
            }
            counter++
        }
        return result
    }

    fun part2(input: List<String>): Int {
        val decoders = arrayOf(
            Tree<Int>(),
            Tree()
        )
        decoders[0].children += Tree(2)
        decoders[1].children += Tree(6)
        val trees = mutableListOf(*decoders)
        for (line in input) {
            if (line.isEmpty()) continue
            trees += buildTree(line)
        }
        trees.sortWith { a, b -> compare(a, b) }
        return decoders.map { trees.indexOf(it) + 1 }.reduce { acc, i -> acc * i }
    }

    val test = readInput("Day13_test")
    println(part1(test))
    println(part2(test))

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}
