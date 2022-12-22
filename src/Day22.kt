import utils.Coord2DInt
import kotlin.math.max

class Day22 {
    enum class Direction(val movement: Coord2DInt, val value: Int) {
        RIGHT(Coord2DInt(0, 1), 0),
        DOWN(Coord2DInt(1, 0), 1),
        LEFT(Coord2DInt(0, -1), 2),
        UP(Coord2DInt(-1, 0), 3)
    }

    companion object {
        val part1 = Part1()
        val part2 = Part2()
        private fun rotate(direction: Direction, rotate: Int): Direction {
            return Direction.values()[(direction.value + rotate + Direction.values().size) % Direction.values().size]
        }
    }

    class Part1 {
        data class Stage2D(
            val rowRange: List<Pair<Int, Int>>,
            val colRange: List<Pair<Int, Int>>,
            val grid: List<String>
        )

        fun buildStage2D(grid: List<String>): Stage2D {
            val rowRange = grid.map { row ->
                row.indexOfFirst { it == '.' || it == '#' } to row.indexOfLast { it == '.' || it == '#' }
            }.toList()
            val colRange = mutableListOf<Pair<Int, Int>>()
            for (i in 0 until grid.maxOf { it.length }) {
                val col = grid.map { if (i >= it.length) ' ' else it[i] }
                colRange += col.indexOfFirst { it == '.' || it == '#' } to col.indexOfLast { it == '.' || it == '#' }
            }
            return Stage2D(rowRange, colRange, grid)
        }

        fun traverse2D(stage: Stage2D, steps: List<Int>, rotations: List<Int>): Pair<Coord2DInt, Direction> {
            var position = Coord2DInt(0, stage.rowRange[0].first)
            var direction = Direction.RIGHT
            var i = 0
            while (i < max(steps.size, rotations.size)) {
                if (i < steps.size) {
                    val step = steps[i]
                    for (j in 0 until step) {
                        val newPos = move2D(position, direction, stage)
                        if (newPos != position) {
                            position = newPos
                        }
                    }
                }
                if (i < rotations.size) {
                    direction = rotate(direction, rotations[i])
                }
                i++
            }
            return position to direction
        }

        private fun move2D(position: Coord2DInt, direction: Direction, stage: Stage2D): Coord2DInt {
            val newPos = position + direction.movement
            if (direction == Direction.UP || direction == Direction.DOWN) {
                if (newPos.x < stage.colRange[newPos.y].first) newPos.x =
                    stage.colRange[newPos.y].second // loop from top to bottom
                if (newPos.x > stage.colRange[newPos.y].second) newPos.x =
                    stage.colRange[newPos.y].first // loop from bottom to top
            }
            if (direction == Direction.LEFT || direction == Direction.RIGHT) {
                if (newPos.y < stage.rowRange[newPos.x].first) newPos.y =
                    stage.rowRange[newPos.x].second // loop from left to right
                if (newPos.y > stage.rowRange[newPos.x].second) newPos.y =
                    stage.rowRange[newPos.x].first // loop from right to left
            }
            return if (stage.grid[newPos.x][newPos.y] == '#') position else newPos  // can't move if there's a wall
        }
    }

    class Part2 {
        data class Stage3D(val faces: Map<Char, Array<CharArray>>)

        data class Position3D(val face: Char, val coord: Coord2DInt, val direction: Direction)

        fun buildStage3D(grid: List<String>, net: List<String>, size: Int): Stage3D {
            val result = mutableMapOf<Char, Array<CharArray>>()
            for(i in net.indices) {
                for (j in net[i].indices) {
                    if (net[i][j] == ' ') continue
                    val face = Array(size) { CharArray(size) }
                    for (x in 0 until size) {
                        for (y in 0 until size) {
                            face[x][y] = grid[i * size + x][j * size + y]
                        }
                    }
                    result += net[i][j] to face
                }
            }
            return Stage3D(result)
        }

        fun traverse3D(stage: Stage3D, size: Int, steps: List<Int>, rotations: List<Int>): Position3D {
            var position = Position3D(
                'U',
                Coord2DInt(0, 0),
                Direction.RIGHT
            )
            var i = 0
            while (i < max(steps.size, rotations.size)) {
                if (i < steps.size) {
                    val step = steps[i]
                    for (j in 0 until step) {
                        val newPos = move3D(position, size, stage)
                        if (newPos != position) {
                            position = newPos
                        }
                    }
                }
                if (i < rotations.size) {
                    position = Position3D(
                        position.face,
                        position.coord,
                        rotate(position.direction, rotations[i])
                    )
                }
                i++
            }
            return position
        }

        private fun move3D(position: Position3D, size: Int, stage3D: Stage3D): Position3D {
            val newPos = position.coord + position.direction.movement
            if (newPos.x in 0 until size && newPos.y in 0 until size) {
                // not moving to adjacent face
                return Position3D(
                    position.face,
                    if (stage3D.faces[position.face]!![newPos.x][newPos.y] == '#') position.coord else newPos,
                    position.direction
                )
            }
            return faceTransition(position, size - 1, stage3D)
        }

        // Only works
        private fun faceTransition(position: Position3D, lastIndex: Int, stage3D: Stage3D): Position3D {
            val newFace = when (position.direction) {
                Direction.UP -> when(position.face) {
                    'B' -> 'L'
                    in listOf('L', 'D') -> 'F'
                    'F' -> 'U'
                    in listOf('U', 'R') -> 'B'
                    else -> throw IllegalArgumentException("Cannot parse ${position.face} next face")
                }
                Direction.DOWN -> when(position.face){
                    'B' -> 'R'
                    in listOf('L', 'D') -> 'B'
                    'F' -> 'D'
                    in listOf('U', 'R') -> 'F'
                    else -> throw IllegalArgumentException("Cannot parse ${position.face} next face")
                }
                Direction.LEFT -> when(position.face) {
                    in listOf('U', 'F', 'D') -> 'L'
                    in listOf('L', 'B', 'R') -> 'U'
                    else -> throw IllegalArgumentException("Cannot parse ${position.face} next face")
                }
                Direction.RIGHT -> when(position.face) {
                    in listOf('U', 'F', 'D') -> 'R'
                    in listOf('L', 'B', 'R') -> 'D'
                    else -> throw IllegalArgumentException("Cannot parse ${position.face} next face")
                }
            }
            val faceRotation = faceRotation(position.face, newFace)
            val newPos = when(position.direction) {
                Direction.UP -> when(faceRotation) {
                    0 -> Coord2DInt(
                        lastIndex,
                        position.coord.y
                    )
                    1 -> Coord2DInt(
                        position.coord.y,
                        0
                    )
                    else -> throw IllegalArgumentException("Cannot parse ${position.face} going ${position.direction}")
                }
                Direction.DOWN -> when (faceRotation) {
                    0 -> Coord2DInt(
                        0,
                        position.coord.y
                    )
                    1 -> Coord2DInt(
                        lastIndex - position.coord.y,
                        lastIndex
                    )
                    else -> throw IllegalArgumentException("Cannot parse ${position.face} going ${position.direction}")
                }
                Direction.LEFT -> when (faceRotation) {
                    0 -> Coord2DInt(
                        position.coord.x,
                        lastIndex
                    )
                    -1 -> Coord2DInt(
                        0,
                        position.coord.x
                    )
                    2 -> Coord2DInt(
                        lastIndex - position.coord.x,
                        0
                    )
                    else -> throw IllegalArgumentException("Cannot parse ${position.face} going ${position.direction}")
                }
                Direction.RIGHT -> when (faceRotation) {
                    0 -> Coord2DInt(
                        position.coord.x,
                        0
                    )
                    -1 -> Coord2DInt(
                        lastIndex,
                        position.coord.x
                    )
                    2 -> Coord2DInt(
                        lastIndex - position.coord.x,
                        lastIndex
                    )
                    else -> throw IllegalArgumentException("Cannot parse ${position.face}")
                }
            }
            return if (stage3D.faces[newFace]!![newPos.x][newPos.y] == '#') position else Position3D(
                newFace,
                newPos,
                rotate(position.direction, faceRotation)
            )
        }

        private fun faceRotation(from: Char, to: Char): Int {
            val map = mapOf(
                ('U' to 'L') to 2,
                ('D' to 'R') to 2,
                ('L' to 'U') to 2,
                ('R' to 'D') to 2,
                ('U' to 'B') to 1,
                ('D' to 'B') to 1,
                ('L' to 'F') to 1,
                ('R' to 'F') to 1,
                ('B' to 'U') to -1,
                ('B' to 'D') to -1,
                ('F' to 'L') to -1,
                ('F' to 'R') to -1
            )
            return map[from to to] ?: 0
        }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val grid = input.filter { it.contains(Regex("[.#]")) }
        val path = input.first { it.contains(Regex("[\\d\\w]")) }
        val (coord, direction) = Day22.part1.traverse2D(
            Day22.part1.buildStage2D(grid),
            Regex("\\d+").findAll(path).map { it.value.toInt() }.toList(),
            Regex("[LR]").findAll(path).map { if (it.value == "L") -1 else 1 }.toList()
        )
        println("$direction, (${coord.x + 1}, ${coord.y + 1}")
        return 1000 * (coord.x + 1) + 4 * (coord.y + 1) + direction.value
    }

    fun part2(input: List<String>, net: List<String>, size: Int): Int {
        val grid = input.filter { it.contains(Regex("[.#]")) }
        val path = input.first { it.contains(Regex("[\\d\\w]")) }
        val stage = Day22.part2.buildStage3D(grid, net, size)
        val (face, position, direction) = Day22.part2.traverse3D(
            stage,
            size,
            Regex("\\d+").findAll(path).map { it.value.toInt() }.toList(),
            Regex("[LR]").findAll(path).map { if (it.value == "L") -1 else 1 }.toList()
        )

        for (i in net.indices) {
            for (j in net[i].indices) {
                if (net[i][j] == face) {
                    val row = i * size + position.x + 1
                    val col = j * size + position.y + 1
                    println("$direction ($row, $col)")
                    return row * 1000 + col * 4 + direction.value
                }
            }
        }
        return 0
    }

    val test = readInput("Day22_test")
    println("=== Part 1 (test) ===")
    println(part1(test))
    val testNet = readInput("Day22_test_net")
    println("=== Part 2 (test) ===")
    println(part2(test, testNet, 4))

    val input = readInput("Day22")
    val net = readInput("Day22_net")
    println("=== Part 1 (puzzle) ===")
    println(part1(input))
    println("=== Part 2 (puzzle) ===")
    println(part2(input, net, 50))
}