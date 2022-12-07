
fun main() {
    fun part1(input: String): Int {
        for (i in input.indices) {
            val marker = input.substring(i..i + 3)
            if (marker.toSet().size >= 4) {
                return i + 4
            }
        }
        return -1
    }

    fun part2(input: String): Int {
        for (i in 0 until input.length - 13) {
            val marker = input.substring(i..i + 13)
            if (marker.toSet().size >= 14) {
                return i + 14
            }
        }
        return -1
    }

    val input = readInput("Day06")[0]
    println(part1(input))
    println(part2(input))
}
