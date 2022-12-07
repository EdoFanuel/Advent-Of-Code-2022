import kotlin.math.max

fun main() {
    fun part1(input: List<String>): Long {
        var maxCal = Long.MIN_VALUE
        var currCal = 0L
        for (cal in input) {
            if (cal.isBlank()) {
                maxCal = max(maxCal, currCal)
                currCal = 0
                continue
            }
            currCal += cal.toLong()
        }
        return maxCal
    }

    fun part2(input: List<String>): Long {
        var cals = mutableListOf<Long>()
        var currCal = 0L
        for (cal in input) {
            if (cal.isBlank()) {
                cals += currCal
                currCal = 0
                continue
            }
            currCal += cal.toLong()
        }
        cals.sortDescending()
        return cals.subList(0, 3).sum()
    }

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
