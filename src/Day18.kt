import java.util.LinkedList

enum class Direction3D(val vector: Coord3D) {
    RIGHT(Coord3D(1, 0, 0)),
    LEFT(Coord3D(-1, 0, 0)),
    TOP(Coord3D(0, 1, 0)),
    BOTTOM(Coord3D(0, -1, 0)),
    BACK(Coord3D(0, 0, 1)),
    FORWARD(Coord3D(0, 0, -1));
}

fun main() {
    val test = readInput("Day18_test")
    println("=== Part 1 (test) ===")
    println(part1(test))
    println("=== Part 2 (test) ===")
    println(part2(test))

    val input = readInput("Day18")
    println("=== Part 1 (puzzle) ===")
    println(part1(input))
    println("=== Part 2 (puzzle) ===")
    println(part2(input))
}

fun part1(input: List<String>): Long {
    val shape = input.map { row ->
        val (a, b, c) = row.split(",").map { it.toLong() }
        Coord3D(a, b, c)
    }.toSet()
    return calculateSurfaceArea(shape)
}

fun part2(input: List<String>): Long {
    val shape = input.map { row ->
        val (a, b, c) = row.split(",").map { it.toLong() }
        Coord3D(a, b, c)
    }.toMutableSet()
    val bbox = BBox3D(
        shape.minOf { it.x }..shape.maxOf { it.x },
        shape.minOf { it.y }..shape.maxOf { it.y },
        shape.minOf { it.z }..shape.maxOf { it.z }
    )
    val trappedAir = mutableSetOf<Coord3D>()
    for (x in bbox.x) {
        for (y in bbox.y) {
            for (z in bbox.z) {
                if (isTrapped(Coord3D(x, y, z), bbox, shape)) trappedAir += Coord3D(x, y, z)
            }
        }
    }
    val surfaceArea = calculateSurfaceArea(shape)
    val airArea = calculateSurfaceArea(trappedAir)
    return surfaceArea - airArea
}

fun calculateSurfaceArea(shape: Set<Coord3D>): Long {
    var surfaceArea = 0L
    for (cube in shape) {
        surfaceArea += Direction3D.values().map { it.vector + cube }.filter { it !in shape }.size
    }
    return surfaceArea
}

fun isTrapped(point: Coord3D, boundingBox: BBox3D, obstacle: Set<Coord3D>): Boolean {
    if (point in obstacle) return false
    val queue = LinkedList<Coord3D>()
    val visited = mutableSetOf<Coord3D>()
    queue += point
    visited += point
    while (queue.isNotEmpty()) {
        val cell = queue.poll()
        visited += cell
        for (direction in Direction3D.values()) {
            val nextCell = direction.vector + cell
            if (nextCell in obstacle || nextCell in visited || nextCell in queue) continue
            if (nextCell !in boundingBox) return false
            queue += nextCell
        }
    }
    return true
}
