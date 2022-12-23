import utils.Coord2DInt
import java.util.*

class Day23 {
    enum class Orientation(val direction: Coord2DInt) {
        NORTH(Coord2DInt(-1, 0)),
        SOUTH(Coord2DInt(1,0)),
        WEST(Coord2DInt(0, -1)),
        EAST(Coord2DInt(0, 1))
    }

    fun parseInput(input: List<String>): Set<Coord2DInt> {
        val result = mutableSetOf<Coord2DInt>()
        for (i in input.indices) {
            for (j in input[i].indices) {
                if (input[i][j] == '#') result += Coord2DInt(i, j)
            }
        }
        return result
    }

    fun simulate(start: Set<Coord2DInt>, iteration: Int): Set<Coord2DInt> {
        var elves = start
        val priority = mutableListOf(
            Orientation.NORTH,
            Orientation.SOUTH,
            Orientation.WEST,
            Orientation.EAST
        )
        repeat(iteration) {
            val proposedMovement = elves.associateWith { move(it, elves, priority) }
            val actualMovement = mutableSetOf<Coord2DInt>()
            for ((source, dest) in proposedMovement) {
                actualMovement += if (proposedMovement.count { it.value == dest } > 1) source else dest
            }
            elves = actualMovement
            Collections.rotate(priority, -1)
        }
        return elves
    }

    fun simulateEndless(start: Set<Coord2DInt>): Int {
        var elves = start
        val priority = mutableListOf(
            Orientation.NORTH,
            Orientation.SOUTH,
            Orientation.WEST,
            Orientation.EAST
        )
        var count = 0
        while (true) {
            val proposedMovement = elves.associateWith { move(it, elves, priority) }
            if (proposedMovement.all { it.key == it.value }) {
                break
            }
            val actualMovement = mutableSetOf<Coord2DInt>()
            for ((source, dest) in proposedMovement) {
                actualMovement += if (proposedMovement.count { it.value == dest } > 1) source else dest
            }
            elves = actualMovement
            Collections.rotate(priority, -1)
            count++
        }
        return count + 1
    }

    private fun move(start: Coord2DInt, blockers: Set<Coord2DInt>, priority: MutableList<Orientation>): Coord2DInt {
        val n = start + Orientation.NORTH.direction
        val s = start + Orientation.SOUTH.direction
        val w = start + Orientation.WEST.direction
        val e = start + Orientation.EAST.direction
        val nw = n + Orientation.WEST.direction
        val ne = n + Orientation.EAST.direction
        val sw = s + Orientation.WEST.direction
        val se = s + Orientation.EAST.direction
        if (setOf(n, nw ,w ,sw, s, se, e, ne).all { it !in blockers }) return start // not moving if no one else is around
        val mapping = mapOf(
            Orientation.NORTH to (setOf(n, nw, ne).all { it !in blockers } to n),
            Orientation.SOUTH to (setOf(s, sw, se).all { it !in blockers } to s),
            Orientation.WEST to (setOf(nw, w, sw).all { it !in blockers } to w),
            Orientation.EAST to (setOf(ne, e, se).all { it !in blockers } to e)
        )
        for (direction in priority) {
            if (mapping[direction]!!.first) return mapping[direction]!!.second
        }
        return start
    }
}

fun main() {
    val day = Day23()
    fun part1(input: List<String>): Int {
        val elves = day.parseInput(input)
        val result = day.simulate(elves, 10)
        val rows = result.maxOf { it.x } - result.minOf { it.x } + 1
        val cols = result.maxOf { it.y } - result.minOf { it.y } + 1
        println("Rectangle size = $rows x $cols. Elves: ${result.size}")
        return rows * cols - result.size
    }

    fun part2(input: List<String>): Int {
        val elves = day.parseInput(input)
        return day.simulateEndless(elves)
    }

    val test = readInput("Day23_test")
    println("=== Part 1 (test) ===")
    println(part1(test))
    println("=== Part 2 (test) ===")
    println(part2(test))

    val input = readInput("Day23")
    println("=== Part 1 (puzzle) ===")
    println(part1(input))
    println("=== Part 2 (puzzle) ===")
    println(part2(input))
}