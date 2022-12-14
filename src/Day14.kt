fun markRocks(input: List<String>): MutableSet<Pair<Int, Int>> {
    val result = mutableSetOf<Pair<Int, Int>>()
    for (line in input) {
        val corners = line.split("->").map { it.trim() }
        for (i in 1 until corners.size) {
            val (ax, ay) = corners[i - 1].split(",").map { it.toInt() }
            val (bx, by) = corners[i].split(",").map { it.toInt() }
            for (x in minOf(ax, bx)..maxOf(ax, bx)) {
                for (y in minOf(ay, by)..maxOf(ay, by)) {
                    result += x to y
                }
            }
        }
    }
    return result
}

fun dropRock(startPoint: Pair<Int, Int>, blockers: Set<Pair<Int, Int>>, floor: Int): Pair<Int, Int> {
    var (x, y) = startPoint
    var hasMove = true
    while (hasMove && y < floor) {
        if (x to y + 1 !in blockers) {
            y++
            continue
        }
        if (x - 1 to y + 1 !in blockers) {
            x--
            y++
            continue
        }
        if (x + 1 to y + 1 !in blockers) {
            x++
            y++
            continue
        }
        else hasMove = false
    }
    return x to y
}

fun main() {
    fun part1(input: MutableSet<Pair<Int, Int>>): Int {
        val startPoint = 500 to 0
        val maxHeight = input.maxOf { it.second }
        var result  = 1
        var newRock = dropRock(startPoint, input, maxHeight + 1)
        while (newRock.second < maxHeight) {
            input += newRock
            newRock = dropRock(startPoint, input, maxHeight + 1)
            result++
        }
        return result
    }

    fun part2(input: MutableSet<Pair<Int, Int>>): Int {
        val startPoint = 500 to 0
        var result  = 1
        val maxHeight = input.maxOf { it.second }
        var newRock = dropRock(startPoint, input, maxHeight + 1)
        while (newRock != startPoint) {
            input += newRock
            newRock = dropRock(startPoint, input, maxHeight + 1)
            result++
        }
        return result
    }

    val test = readInput("Day14_test")
    println(part1(markRocks(test)))
    println(part2(markRocks(test)))

    val input = readInput("Day14")
    println(part1(markRocks(input)))
    println(part2(markRocks(input)))
}