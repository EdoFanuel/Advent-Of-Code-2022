import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import java.util.regex.Pattern

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

fun String.match(pattern: String) = Regex(pattern).matchEntire(this)

data class Tree<T>(var value: T? = null, val children: MutableList<Tree<T>> = mutableListOf())

fun <T> Tree<T>.print(level: Int  = 0) {
    println("${"\t".repeat(level)}${this.value}")
    this.children.forEach { it.print(level + 1) }
}

fun <T> Tree<T>.isLeaf(): Boolean {
    return this.value != null && this.children.isEmpty()
}
