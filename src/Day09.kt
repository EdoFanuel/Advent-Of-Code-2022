import kotlin.math.abs

fun main() {
    val direction = mapOf(
        "D" to (-1 to 0),
        "U" to (1 to 0),
        "L" to (0 to -1),
        "R" to (0 to 1)
    )

    fun stepCloser(head: Int, tail: Int): Int = when {
        head > tail -> tail + 1
        head < tail -> tail - 1
        else -> tail
    }

    fun moveTail(head: Pair<Int, Int>, tail: Pair<Int, Int>): Pair<Int, Int> {
        val (hx, hy) = head
        val (tx, ty) = tail
        if (abs(hx - tx) <= 1 && abs(hy - ty) <= 1) return tail // tail already touches head
        return stepCloser(hx, tx) to stepCloser(hy, ty)
    }

    fun part1(input: List<String>): Int {
        var head = 0 to 0
        var tail = head
        val positions = mutableSetOf<Pair<Int, Int>>()
        positions += tail
        for (line in input) {
            val (c, dist) = line.split(" ")
            val dir = direction[c] ?: (0 to 0)
            for (i in 0 until dist.toInt()) {
                head = head.first + dir.first to head.second + dir.second
                tail = moveTail(head, tail)
                positions += tail
            }
        }
        return positions.size
    }

    fun part2(input: List<String>): Int {
        val knots = Array(10) { 0 to 0 }
        val positions = mutableSetOf<Pair<Int, Int>>()
        positions += knots
        for (line in input) {
            val (c, dist) = line.split(" ")
            val dir = direction[c] ?: (0 to 0)
            for (i in 0 until dist.toInt()) {
                knots[0] = knots[0].first + dir.first to knots[0].second + dir.second
                for (j in 1 until knots.size) {
                    knots[j] = moveTail(knots[j - 1], knots[j])
                }
                positions += knots.last()
            }
        }
        return positions.size
    }

    val test = readInput("Day09_test")
    println(part1(test))
    println(part2(test))

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}

