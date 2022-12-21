package utils

data class Tree<T>(var value: T? = null, val children: MutableList<Tree<T>> = mutableListOf()) {
    fun print(level: Int  = 0) {
        println("${"\t".repeat(level)}${this.value}")
        this.children.forEach { it.print(level + 1) }
    }

    fun isLeaf(): Boolean = this.children.isEmpty()
    fun hasValue() = this.value != null
}
