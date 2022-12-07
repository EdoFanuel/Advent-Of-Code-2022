fun main() {
    fun part1(input: List<String>): Int {
        var count = 0
        for (line in input) {
            val (range1, range2) = line.split(",")
            val (start1, end1) = range1.split("-").map { it.toInt() }
            val (start2, end2) = range2.split("-").map { it.toInt() }
            if ((start1 <= start2 && end1 >= end2) || (start2 <= start1 && end2 >= end1)) count++
        }
        return count
    }

    fun part2(input: List<String>): Int {
        var count = 0
        for (line in input) {
            val (range1, range2) = line.split(",")
            val (start1, end1) = range1.split("-").map { it.toInt() }
            val (start2, end2) = range2.split("-").map { it.toInt() }
            if (end1 < start2 || end2 < start1) continue
            count++
        }
        return count
    }

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
