import utils.BBox2D
import utils.Coord2DInt
import utils.Orientation
import utils.aStar
import kotlin.math.abs
import kotlin.math.max

class Day24 {
    data class Wind(val location: Coord2DInt, val orientation: Orientation) {
        fun move(boundary: BBox2D): Coord2DInt {
            val newLoc = location + orientation.direction
            if (newLoc.x < boundary.x.first) newLoc.x = boundary.x.last.toInt()
            if (newLoc.x > boundary.x.last) newLoc.x = boundary.x.first.toInt()
            if (newLoc.y < boundary.y.first) newLoc.y = boundary.y.last.toInt()
            if (newLoc.y > boundary.y.last) newLoc.y = boundary.y.first.toInt()
            return newLoc
        }
    }

    data class Valley(val start: Coord2DInt,
                      val end: Coord2DInt,
                      val winds: MutableList<Wind> = mutableListOf(),
                      val boundary: BBox2D)

    fun parseInput(input: List<String>): Valley {
        val start = Coord2DInt(0, input.first().indexOf('.'))
        val end = Coord2DInt(input.lastIndex, input.last().indexOf('.'))
        val winds = mutableListOf<Wind>()
        val directions = mapOf(
            '<' to Orientation.WEST,
            '>' to Orientation.EAST,
            '^' to Orientation.NORTH,
            'v' to Orientation.SOUTH
        )
        for (i in input.indices) {
            for (j in input[i].indices) {
                if (input[i][j] in directions.keys) {
                    winds += Wind(
                        Coord2DInt(i, j),
                        directions[input[i][j]]!!
                    )
                }
            }
        }
        val boundary = BBox2D(
            1L until input.lastIndex,
            1L until input[0].lastIndex
        )
        return Valley(start, end, winds, boundary)
    }

    fun getObstaclesAtTime(cache: MutableMap<Int, List<Wind>>, boundary: BBox2D, time: Int): Set<Coord2DInt> {
        if (time < 0) return emptySet()
        if (time !in cache.keys) {
            getObstaclesAtTime(cache, boundary, time - 1)
            cache[time] = cache[time - 1]!!.map { Wind(it.move(boundary), it.orientation) }
        }
        return cache[time]!!.map { it.location }.toSet()
    }
}

fun main() {
    val day = Day24()
    fun part1(input: List<String>): Int {
        val valley = day.parseInput(input)
        println("${valley.start} --> ${valley.end}")
        println(valley.boundary)
        val possibleMoves = listOf(
            Orientation.NORTH.direction,
            Orientation.SOUTH.direction,
            Orientation.WEST.direction,
            Orientation.EAST.direction,
            Coord2DInt(0, 0)       // stay still
        )
        val obstacleCache = mutableMapOf<Int, List<Day24.Wind>>()
        obstacleCache[0] = valley.winds.toList()
        val path = aStar(
            valley.start to 0,
            { (coord, _) -> coord == valley.end },
            { (coord, time) -> possibleMoves
                .map { it + coord to time + 1 }
                .filter { it.first in valley.boundary || it.first == valley.end || it.first == valley.start }
                .filter { it.first !in day.getObstaclesAtTime(obstacleCache, valley.boundary, it.second) }},
            { (coord, time) -> max(abs(coord.x - valley.end.x), abs(coord.y - valley.end.y)) * 1.0 }
        )
        return path.size
    }

    fun part2(input: List<String>): Int {
        val valley = day.parseInput(input)
        println("${valley.start} --> ${valley.end} --> ${valley.start} --> ${valley.end}")

        println(valley.boundary)
        val possibleMoves = listOf(
            Orientation.NORTH.direction,
            Orientation.SOUTH.direction,
            Orientation.WEST.direction,
            Orientation.EAST.direction,
            Coord2DInt(0, 0)       // stay still
        )
        val obstacleCache = mutableMapOf<Int, List<Day24.Wind>>()
        obstacleCache[0] = valley.winds.toList()
        val path1 = aStar(
            valley.start to 0,
            { (coord, _) -> coord == valley.end },
            { (coord, time) -> possibleMoves
                .map { it + coord to time + 1 }
                .filter { it.first in valley.boundary || it.first == valley.end || it.first == valley.start }
                .filter { it.first !in day.getObstaclesAtTime(obstacleCache, valley.boundary, it.second) }},
            { (coord, time) -> max(abs(coord.x - valley.end.x), abs(coord.y - valley.end.y)) * 1.0 }
        )
        val path2 = aStar(
            valley.end to path1.maxOf { it.second },
            { (coord, _) -> coord == valley.start },
            { (coord, time) -> possibleMoves
                .map { it + coord to time + 1 }
                .filter { it.first in valley.boundary || it.first == valley.end || it.first == valley.start }
                .filter { it.first !in day.getObstaclesAtTime(obstacleCache, valley.boundary, it.second) }},
            { (coord, time) -> max(abs(coord.x - valley.start.x), abs(coord.y - valley.start.y)) * 1.0 }
        )
        val path3 = aStar(
            valley.start to path2.maxOf { it.second },
            { (coord, _) -> coord == valley.end },
            { (coord, time) -> possibleMoves
                .map { it + coord to time + 1 }
                .filter { it.first in valley.boundary || it.first == valley.end || it.first == valley.start }
                .filter { it.first !in day.getObstaclesAtTime(obstacleCache, valley.boundary, it.second) }},
            { (coord, time) -> max(abs(coord.x - valley.end.x), abs(coord.y - valley.end.y)) * 1.0 }
        )
        return path3.maxOf { it.second }
    }

    val test = readInput("Day24_test")
    println("=== Part 1 (test) ===")
    println(part1(test))
    println("=== Part 2 (test) ===")
    println(part2(test))

    val input = readInput("Day24")
    println("=== Part 1 (puzzle) ===")
    println(part1(input))
    println("=== Part 2 (puzzle) ===")
    println(part2(input))
}