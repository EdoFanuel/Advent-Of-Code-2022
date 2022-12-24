
import utils.aStar
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

data class Path(val start: Pair<Int, Int>, val end: Pair<Int, Int>, val grid: Array<IntArray>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Path

        if (start != other.start) return false
        if (end != other.end) return false
        if (!grid.contentDeepEquals(other.grid)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = start.hashCode()
        result = 31 * result + end.hashCode()
        result = 31 * result + grid.contentDeepHashCode()
        return result
    }
}

fun main() {
    val directions = listOf(
        -1 to 0,
        1 to 0,
        0 to -1,
        0 to 1
    )
    fun convertToPath(input: List<String>): Path {
        var startPoint = 0 to 0
        var endPoint = 0 to 0
        val result = input.mapIndexed { i, row -> row.mapIndexed { j, c ->
            when(c) {
                'S' -> {
                    startPoint =  i to j
                    0
                }
                'E' -> {
                    endPoint = i to j
                    25
                }
                else -> c - 'a'
            }
        }.toIntArray()}.toTypedArray()
        return Path(startPoint, endPoint, result)
    }

    fun part1(input: Path): Int {
        val path = aStar(
            input.start,
            {p -> p == input.end},
            { (x, y) -> directions
                .map { (dx, dy) -> x + dx to y + dy }
                .filter { (nx, ny) -> nx >= 0 && nx <= input.grid.lastIndex
                        && ny >= 0 && ny <= input.grid[nx].lastIndex
                        && input.grid[nx][ny] <= input.grid[x][y] + 1}},
            { (x, y) -> max(abs(x - input.end.first), abs(y - input.end.second)) * 1.0 }
        )
        return path.size
    }

    fun part2(input: Path): Int {
        var minPath = Int.MAX_VALUE
        for (i in input.grid.indices) {
            for (j in input.grid[i].indices) {
                if (input.grid[i][j] == 0) {
                    val path = aStar(
                        i to j,
                        {p -> p == input.end},
                        { (x, y) -> directions
                            .map { (dx, dy) -> x + dx to y + dy }
                            .filter { (nx, ny) -> nx >= 0 && nx <= input.grid.lastIndex
                                    && ny >= 0 && ny <= input.grid[nx].lastIndex
                                    && input.grid[nx][ny] <= input.grid[x][y] + 1}},
                        { (x, y) -> max(abs(x - input.end.first), abs(y - input.end.second)) * 1.0 }
                    )
                    if (path.isEmpty()) continue
                    minPath = min(minPath, path.size)
                }
            }
        }
        return minPath
    }

    val test = readInput("Day12_test")
    println(part1(convertToPath(test)))
    println(part2(convertToPath(test)))

    val input = readInput("Day12")
    println(part1(convertToPath(input)))
    println(part2(convertToPath(input)))
}