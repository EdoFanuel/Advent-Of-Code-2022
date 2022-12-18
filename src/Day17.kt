import kotlin.math.min

val rocks = listOf(
    (0..3L).map { Coord2D(it, 0) },
    listOf(Coord2D(0, 1), Coord2D(1, 2), Coord2D(1, 1), Coord2D(1, 0), Coord2D(2, 1)),
    listOf(Coord2D(0, 0), Coord2D(1, 0), Coord2D(2, 2), Coord2D(2, 1), Coord2D(2, 0)),
    (0..3L).map { Coord2D(0, it) },
    listOf(Coord2D(0, 0), Coord2D(0, 1), Coord2D(1, 0), Coord2D(1, 1))
)
val directions = mapOf(
    '<' to Coord2D(-1, 0),
    '>' to Coord2D(1, 0),
    'v' to Coord2D(0, -1)
)

fun isValid(pos: Coord2D, tower: Set<Coord2D>): Boolean =
    pos.x in 0 until 7
            && pos.y >= 0
            && pos !in tower

fun dropRock(rock: List<Coord2D>, blocks: MutableSet<Coord2D>, windIdx: Long, winds: String): Long {
    val maxHeight = blocks.maxOfOrNull { it.y } ?: -1
    var pos = Coord2D(2, maxHeight + 4)
    var j = windIdx
    while (true) {
        val wind = directions[winds[(j % winds.length).toInt()]]!!
        pos = if (rock.any { !isValid(it + pos + wind, blocks) }) pos else pos + wind   // move sideways
        j++

        if (rock.any { !isValid(it + pos + directions['v']!!, blocks) }) {              // move down
            blocks += rock.map { it + pos }
            break
        } else {
            pos += directions['v']!!
        }
    }
//    printRocks(blocks)
    return j
}
fun printRocks(blocks: MutableSet<Coord2D>) {
    for (i in blocks.maxOf { it.y } downTo blocks.minOf { it.y }) {
        println((0..6L).joinToString("") { j -> if (Coord2D(j, i) in blocks) "#" else "." })
    }
    println()
}


fun main() {
    val test = readInput("Day17_test")
//    println(part(test, 10))
    println(part(test, 2022) + 1)
    println(part(test, 1_000_000_000_000))

    val input = readInput("Day17")
    println(part(input, 2022) + 1)
    println(part(input, 1_000_000_000_000))
}

fun part(input: List<String>, rockCount: Long = 2022): Long {
    val winds = input[0]
    val simulation = 10_000L
    val history = mutableListOf<Long>()
    val blockers = mutableSetOf<Coord2D>()
    var windIndex = 0L
    for (i in 0 until min(rockCount, simulation)) {
        windIndex = dropRock(rocks[(i % rocks.size).toInt()], blockers, windIndex, winds)
        history += blockers.maxOf { it.y }
    }
    if (rockCount < simulation) return history.last() + 1
    val diff = history.zipWithNext().map { (prev, curr) -> curr - prev }

    val loopStart = 200 // could be anything that's inside the loop
    val markerLength = 10
    val marker = diff.subList(loopStart, loopStart + markerLength)

    var loopHeight = -1L
    var loopLength = -1
    val heightBeforeLoop = history[loopStart - 1] + 1
    for (i in loopStart + markerLength until diff.size) {
        if (marker == diff.subList(i, i + markerLength)) {
            // Marker matches the sequence starting at i, so Loop ends at i - 1
            loopLength = i - loopStart
            loopHeight = history[i - 1] - heightBeforeLoop
            break
        }
    }

    // Calculate the height at the target based on the number of loops and the height of the loop
    val numFullLoops = (rockCount - loopStart) / loopLength
    val offsetIntoLastLoop = ((rockCount - loopStart) % loopLength).toInt()
    val extraHeight = history[loopStart + offsetIntoLastLoop] - heightBeforeLoop
    return heightBeforeLoop + loopHeight * numFullLoops + extraHeight
}
