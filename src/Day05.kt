import java.util.*

fun main() {
    val stacks = Array<Stack<Char>>(9) { Stack() }
    stacks[0] += "TDWZVP".toList()
    stacks[1] += "LSWVFJD".toList()
    stacks[2] += "ZMLSVTBH".toList()
    stacks[3] += "RSJ".toList()
    stacks[4] += "CZBGFMLW".toList()
    stacks[5] += "QVWHZRGB".toList()
    stacks[6] += "VJPCBDN".toList()
    stacks[7] += "PTBQ".toList()
    stacks[8] += "HGZRC".toList()

    fun part1(input: List<String>): String {
        for (line in input) {
            val tokens = line.split(" ")
            val count = tokens[1].toInt()
            val source = tokens[3].toInt() - 1
            val target = tokens[5].toInt() - 1
            for (i in 0 until count) {
                stacks[target].push(stacks[source].pop())
            }
        }
        return stacks.map { it.peek() }.joinToString("")

    }

    fun part2(input: List<String>): String {
        for (line in input) {
            val tokens = line.split(" ")
            val count = tokens[1].toInt()
            val source = tokens[3].toInt() - 1
            val target = tokens[5].toInt() - 1
            val move = mutableListOf<Char>()
            for (i in 0 until count) {
                move += stacks[source].pop()
            }
            for (c in move.reversed()) {
                stacks[target].push(c)
            }
        }
        return stacks.map { it.peek() }.joinToString("")
    }

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}
