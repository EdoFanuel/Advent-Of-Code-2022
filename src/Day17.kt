import kotlin.math.max

val rocks = listOf(
    (0..3).map { it to 0L },
    listOf(0 to 1L, 1 to 2L, 1 to 1L, 1 to 0L, 2 to 1L),
    listOf(0 to 0L, 1 to 0L, 2 to 2L, 2 to 1L, 2 to 0L),
    (0..3L).map { 0 to it },
    listOf(0 to 0L, 0 to 1L, 1 to 0L, 1 to 1L)
)
val directions = mapOf(
    '<' to (-1 to 0L),
    '>' to (1 to 0L),
    'v' to (0 to -1L)
)

fun isValid(pos: Pair<Int, Long>, tower: Set<Pair<Int, Long>>): Boolean =
    pos.first in 0 until 7
            && pos.second > 0
            && pos !in tower

private operator fun Pair<Int, Long>.plus(other: Pair<Int, Long>): Pair<Int, Long> =
    (this.first + other.first) to (this.second + other.second)

private operator fun Pair<Int, Long>.minus(other: Pair<Int, Long>): Pair<Int, Long> =
    (this.first - other.first) to (this.second - other.second)


fun main() {
    val jets = readInput("Day17_test")[0]
    val tower = (0..6).map { it to -1L }.toMutableSet()
    var height = -1L
    var rockIdx = 4
    var jetIdx = jets.lastIndex
    var (p1, p2) = 0L to 0L
    val cache = mutableMapOf<Pair<Int, Int>, Pair<Long, Long>>()

    val limit = 1_000_000_000_000
    for (n in 0 until limit) {
        val seen = rockIdx to jetIdx
        if (seen in cache.keys) {
            val (prevN, prevHeight) = cache[seen]!!
            val period = n - prevN
            if (n % period == limit % period) {
                p2 = prevHeight + (height + 1 - prevHeight) * (((limit - n) / period) + 1)
                break
            }
        } else {
            cache[seen] = n to (height + 1)
        }

        if (n == 2022L) p1 = height + 1

        rockIdx = if (rockIdx == rocks.lastIndex) 0 else rockIdx + 1
        var rock = rocks[rockIdx].map { it + (2 to height + 4) }

        while (true) {
            jetIdx = if (jetIdx == jets.lastIndex) 0 else jetIdx + 1
            val jet = jets[jetIdx]
            rock = rock.map { it + directions[jet]!! }
            if (rock.any { !isValid(it, tower) }) rock = rock.map { it - directions[jet]!! }
            rock = rock.map { it + directions['v']!! }
            if (rock.any { !isValid(it, tower) }) {
                tower += rock.map { it - directions['v']!! }
                height = max(height, rock.maxOf { it.second } + 1)
                break
            }
        }
    }
    println("$p1 $p2")
}
