fun main() {
    val rockPaperScissor = arrayOf(
        intArrayOf(4, 8, 3),
        intArrayOf(1, 5, 9),
        intArrayOf(7, 2, 6)
    )
    val opponentCodex = arrayOf("A", "B", "C")
    val yourCodex = arrayOf("X", "Y", "Z")

    fun part1(input: List<String>): Int {
        var score = 0
        for (line in input) {
            val (a, b) = line.split(" ")
            score += rockPaperScissor[opponentCodex.indexOf(a)][yourCodex.indexOf(b)]
        }
        return score
    }

    fun part2(input: List<String>): Int {
        var score = 0
        for (line in input) {
            val (a, b) = line.split(" ")
            val opponent = opponentCodex.indexOf(a)
            val you = yourCodex.indexOf(b) - 1
            score += rockPaperScissor[opponent][(opponent + you + 3) % 3]
        }
        return score

    }

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
