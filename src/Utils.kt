import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

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

class LRUCache<K, V>(val capacity: Int): LinkedHashMap<K, V>() {
    override fun removeEldestEntry(eldest: MutableMap.MutableEntry<K, V>?): Boolean {
        return size > capacity
    }
}

data class Coord2D(val x: Long, val y: Long)
operator fun Coord2D.plus(other: Coord2D) = Coord2D(this.x + other.x, this.y + other.y)
operator fun Coord2D.minus(other: Coord2D) = Coord2D(this.x - other.x, this.y - other.y)
operator fun Coord2D.times(scale: Long) = Coord2D(this.x * scale, this.y * scale)
operator fun Coord2D.div(scale: Long) = Coord2D(this.x / scale, this.y / scale)

data class Coord3D(val x: Long, val y: Long, val z: Long)

data class BBox3D(val x: LongRange, val y: LongRange, val z: LongRange)
operator fun BBox3D.contains(coord: Coord3D) = coord.x in this.x && coord.y in this.y && coord.z in this.z

operator fun Coord3D.plus(other: Coord3D) = Coord3D(
    this.x + other.x,
    this.y + other.y,
    this.z + other.z
)


data class Tree<T>(var value: T? = null, val children: MutableList<Tree<T>> = mutableListOf())
fun <T> Tree<T>.print(level: Int  = 0) {
    println("${"\t".repeat(level)}${this.value}")
    this.children.forEach { it.print(level + 1) }
}

fun <T> Tree<T>.isLeaf(): Boolean {
    return this.value != null && this.children.isEmpty()
}
