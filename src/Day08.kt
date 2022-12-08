import kotlin.math.max

fun main() {
    fun initGrid(input: List<String>): Array<IntArray> {
        val result = Array(input.size) { IntArray(input[0].length) }
        for (i in input.indices) {
            for (j in input[i].indices) {
                result[i][j] = input[i][j].digitToInt()
            }
        }
        return result
    }

    fun part1(input: Array<IntArray>): Int {
        val rows = input.size
        val cols = input[0].size
        val invalidCoords = mutableSetOf<Pair<Int, Int>>()
        for (i in 0 until rows) {
            // Left visibility
            var maxHeight = -1
            for (j in 0 until cols) {
                if (input[i][j] > maxHeight) {
                    maxHeight = input[i][j]
                    invalidCoords += i to j
                }
            }
            // Right visibility
            maxHeight = -1
            for (j in cols - 1 downTo 0) {
                if (input[i][j] > maxHeight) {
                    maxHeight = input[i][j]
                    invalidCoords += i to j
                }
            }
        }
        for (j in 0 until cols) {
            // Top visibility
            var maxHeight = -1
            for (i in 0 until rows) {
                if (input[i][j] > maxHeight) {
                    maxHeight = input[i][j]
                    invalidCoords += i to j
                }
            }
            // Bottom visibility
            maxHeight = -1
            for (i in rows - 1 downTo 0) {
                if (input[i][j] > maxHeight) {
                    maxHeight = input[i][j]
                    invalidCoords += i to j
                }
            }
        }
        return invalidCoords.size
    }

    fun part2(input: Array<IntArray>): Int {
        val rows = input.size
        val cols = input[0].size
        var maxScore = 0
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                val maxHeight = input[i][j]
                var topScore = 0
                for (ni in i - 1 downTo 0) {
                    topScore++
                    if (input[ni][j] >= maxHeight) break
                }
                var bottomScore = 0
                for (ni in i + 1 until rows) {
                    bottomScore++
                    if (input[ni][j] >= maxHeight) break
                }
                var leftScore = 0
                for (nj in j - 1 downTo 0) {
                    leftScore++
                    if (input[i][nj] >= maxHeight) break
                }
                var rightScore = 0
                for (nj in j + 1 until cols) {
                    rightScore++
                    if (input[i][nj] >= maxHeight) break
                }
                val score = topScore * bottomScore * leftScore * rightScore
                maxScore = max(maxScore, score)
            }
        }
        return maxScore
    }

    val test = readInput("Day08_test")
    val testGrid = initGrid(test)
    println(part1(testGrid))
    println(part2(testGrid))

    val input = readInput("Day08")
    val grid = initGrid(input)
    println(part1(grid))
    println(part2(grid))
}

