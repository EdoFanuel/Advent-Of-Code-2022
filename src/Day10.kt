import java.util.*

fun main() {
    fun readState(input: List<String>): TreeMap<Int, Int> {
        var cycle = 0
        var signal = 1
        val state = TreeMap<Int, Int>()
        for (line in input) {
            when {
                line == "noop" -> cycle++
                line.startsWith("addx") -> {
                    cycle++
                    val increment = line.split(" ")[1].toInt()
                    signal += increment
                    cycle++
                }
            }
            state[cycle] = signal
        }
        return state
    }

    fun part1(state: TreeMap<Int, Int>): Int {
        return listOf(20, 60, 100, 140, 180, 220).sumOf { it * (state.lowerEntry(it)?.value ?: 0) }
    }

    fun part2(state: TreeMap<Int, Int>): String {
        val sb = StringBuilder()
        for (i in 0 until  state.keys.max()) {
            if (i % 40 == 0) sb.append('\n')
            val pointer = state.lowerEntry(i + 1)?.value ?: 1
            sb.append(if (i % 40 in pointer - 1..pointer + 1) '#' else '.')
        }
        return sb.toString()
    }

    val test = readInput("Day10_test")
    val testState = readState(test)
    println(part1(testState))
    println(part2(testState))

    val input = readInput("Day10")
    val state = readState(input)
    println(part1(state))
    println(part2(state))
}

