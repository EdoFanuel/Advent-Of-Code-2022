import java.util.*
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

fun <P> aStar(start: P,
              goalFunction: (P) -> Boolean,
              successorFunction: (P) -> List<P>,
              heuristicFunction: (P) -> Double
): List<P> {
    val visited = mutableSetOf<P>()
    val path = mutableMapOf<P, P>()
    val distance = mutableMapOf<P, Double>()
    val frontier = PriorityQueue<Pair<P, Double>>(compareBy { it.second })
    distance[start] = 0.0
    frontier.add(start to 0.0)
    while (frontier.isNotEmpty()) {
        val (node, _) = frontier.poll()
        if (node in visited) continue
        if (goalFunction(node)) return reconstructPath(path, start, node)
        visited.add(node)
        for (successor in successorFunction(node)) {
            frontier.add(successor to (distance[node]!! + 1.0 + heuristicFunction(successor)))
            if (successor !in distance || distance[node]!! + 1.0 < distance[successor]!!) {
                distance[successor] = distance[node]!! + 1.0
                path[successor] = node
            }
        }
    }
    return emptyList()
}

fun <P> reconstructPath(path: MutableMap<P, P>, start: P, end: P): List<P> {
    var node = end
    val reversePath = mutableListOf(end)
    while (node != start) {
        node = path[node]!!
        reversePath.add(node)
    }
    return reversePath.reversed()
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