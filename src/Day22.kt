import utils.Coord2DInt
import kotlin.math.max

class Day22 {
    data class Stage(
        val rowRange: List<Pair<Int, Int>>,
        val colRange: List<Pair<Int, Int>>,
        val grid: List<String>
    )

    enum class Direction(val movement: Coord2DInt, val value: Int) {
        RIGHT(Coord2DInt(0, 1), 0),
        DOWN(Coord2DInt(1, 0), 1),
        LEFT(Coord2DInt(0, -1), 2),
        UP(Coord2DInt(-1, 0), 3)
    }

    fun buildStage(grid: List<String>): Stage {
        val rowRange = grid.map { row ->
            row.indexOfFirst { it == '.' || it == '#' } to row.indexOfLast { it == '.' || it == '#' }
        }.toList()
        val colRange = mutableListOf<Pair<Int, Int>>()
        for (i in 0 until grid.maxOf { it.length }) {
            val col = grid.map { if (i >= it.length) ' ' else it[i] }
            colRange += col.indexOfFirst { it == '.' || it == '#' } to col.indexOfLast { it == '.' || it == '#' }
        }
        println(rowRange)
        println(colRange)
        return Stage(rowRange, colRange, grid)
    }



    fun traverse(stage: Stage, steps: List<Int>, rotations: List<Int>): Pair<Coord2DInt, Direction> {
        var position = Coord2DInt(0, stage.rowRange[0].first)
        var direction = Direction.RIGHT
        var i = 0
        while (i < max(steps.size, rotations.size)) {
            if (i < steps.size) {
                val step = steps[i]
                for (j in 0 until step) {
                    val newPos = move(position, direction, stage)
                    if (newPos != position) {
                        position = newPos
                    }
                }
            }
            if (i < rotations.size) {
                val rotate = rotations[i]
                direction = Direction.values()[(direction.value + rotate + Direction.values().size) % Direction.values().size]
            }
            i++
        }
        return position to direction
    }

    private fun move(position: Coord2DInt, direction: Direction, stage: Stage): Coord2DInt {
        val newPos = position + direction.movement
        if (direction == Direction.UP || direction == Direction.DOWN) {
            if (newPos.x < stage.colRange[newPos.y].first) newPos.x = stage.colRange[newPos.y].second // loop from top to bottom
            if (newPos.x > stage.colRange[newPos.y].second) newPos.x = stage.colRange[newPos.y].first // loop from bottom to top
        }
        if (direction == Direction.LEFT || direction == Direction.RIGHT) {
            if (newPos.y < stage.rowRange[newPos.x].first) newPos.y = stage.rowRange[newPos.x].second // loop from left to right
            if (newPos.y > stage.rowRange[newPos.x].second) newPos.y = stage.rowRange[newPos.x].first // loop from right to left
        }
        return if (stage.grid[newPos.x][newPos.y] == '#') position else newPos  // can't move if there's a wall
    }
}

fun main() {
    val day = Day22()
    fun part1(input: List<String>): Int {
        val grid = input.filter { it.contains(Regex("[.#]")) }
        val path = input.first { it.contains(Regex("[\\d\\w]")) }
        val (coord, direction) = day.traverse(
            day.buildStage(grid),
            Regex("\\d+").findAll(path).map { it.value.toInt() }.toList(),
            Regex("[LR]").findAll(path).map { if (it.value == "L") -1 else 1 }.toList()
        )
        return 1000 * (coord.x + 1) + 4 * (coord.y + 1) + direction.value
    }

    fun part2(input: List<String>): Long {
        return 0
    }

    val test = readInput("Day22_test")
    println("=== Part 1 (test) ===")
    println(part1(test))
    println("=== Part 2 (test) ===")
    println(part2(test))

    val input = readInput("Day22")
    println("=== Part 1 (puzzle) ===")
    println(part1(input))
    println("=== Part 2 (puzzle) ===")
    println(part2(input))
}