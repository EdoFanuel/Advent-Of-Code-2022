class Day25 {
    fun decode(input: String): Long {
        var result = 0L
        for (c in input) {
            result *= 5
            result += when(c) {
                '2' -> 2
                '1' -> 1
                '0' -> 0
                '-' -> -1
                '=' -> -2
                else -> throw IllegalArgumentException()
            }
        }
        return result
    }

    fun encode(input: Long): String {
        val sb = StringBuilder()
        var n = input
        var carry = 0
        while (n > 0) {
            n += carry
            carry = 0
            when (n % 5) {
                0L -> sb.append(0)
                1L -> sb.append(1)
                2L -> sb.append(2)
                3L -> {
                    sb.append('=')
                    carry = 1
                }
                4L -> {
                    sb.append('-')
                    carry = 1
                }
            }
            n /= 5
        }
        return sb.toString().reversed()
    }
}

fun main() {
    val day = Day25()
    fun part1(input: List<String>): String {
        return day.encode(input.sumOf { day.decode(it) })
    }

    fun part2(input: List<String>): String {
        return "There is no part 2"
    }


    val test = readInput("Day25_test")
    println("=== Part 1 (test) ===")
    println(part1(test))
    println("=== Part 2 (test) ===")
    println(part2(test))

    val input = readInput("Day25")
    println("=== Part 1 (puzzle) ===")
    println(part1(input))
    println("=== Part 2 (puzzle) ===")
    println(part2(input))
}