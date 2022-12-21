import java.util.*

class Day21 {
    data class Monkey(var value: Long? = null, val operation: Char?, val leftChild: String?, val rightChild: String?)

    private val leafPattern = "(\\w{4}): (\\d+)"
    private val trunkPattern = "(\\w{4}): (\\w{4}) ([+\\-\\/\\*]) (\\w{4})"

    fun parseLine(line: String): Pair<String, Monkey> {
        val leafGroup = line.match(leafPattern)?.groupValues
        val trunkGroup = line.match(trunkPattern)?.groupValues
        if (leafGroup != null) {
            return leafGroup[1] to Monkey(leafGroup[2].toLong(), null, null, null)
        }
        if (trunkGroup != null) {
            return trunkGroup[1] to Monkey(null, trunkGroup[3][0], trunkGroup[2], trunkGroup[4])
        }
        throw UnknownFormatConversionException("Cannot parse $line")
    }

    fun evalP1(monkey: Monkey, group: Map<String, Monkey>): Long {
        if (monkey.value == null) {
            monkey.value = when (monkey.operation) {
                '+' -> evalP1(group[monkey.leftChild!!]!!, group) + evalP1(group[monkey.rightChild!!]!!, group)
                '-' -> evalP1(group[monkey.leftChild!!]!!, group) - evalP1(group[monkey.rightChild!!]!!, group)
                '*' -> evalP1(group[monkey.leftChild!!]!!, group) * evalP1(group[monkey.rightChild!!]!!, group)
                '/' -> evalP1(group[monkey.leftChild!!]!!, group) / evalP1(group[monkey.rightChild!!]!!, group)
                else -> throw UnknownFormatConversionException("Cannot recognize operand ${monkey.operation}")
            }
        }
        return monkey.value!!
    }

    fun evalP2(key: String, group: Map<String, Monkey>): List<String> {
        if (key == "humn") return listOf("n")
        val monkey = group[key]!!
        if (monkey.value != null) return listOf(monkey.value.toString())
        val result = mutableListOf<String>()
        result += evalP2(monkey.leftChild!!, group)
        result += evalP2(monkey.rightChild!!, group)
        result += monkey.operation!!.toString()
        return result
    }

    fun evalRPN(tokens: List<String>): Long? {
        val stack = Stack<Long>()
        for (token in tokens) {
            when(token) {
                "n" -> return null
                in listOf("+", "-", "*", "/") -> {
                    val right = stack.pop()
                    val left = stack.pop()
                    val result = when(token) {
                        "+" -> left + right
                        "-" -> left - right
                        "*" -> left * right
                        "/" -> left / right
                        else -> throw IllegalArgumentException("Shouldn't be possible to receive $token here.")
                    }
                    stack.push(result)
                }
                else -> stack.push(token.toLong())
            }
        }
        return stack.pop()
    }

    fun solveEquation(key: String, graph: Map<String, Monkey>, target: Long): Long {
        if (key == "humn") return target
        val root = graph[key]!!
        val left = evalRPN(evalP2(root.leftChild!!, graph))
        val right = evalRPN(evalP2(root.rightChild!!, graph))
        return when (root.operation) {
            '+' -> solveEquation(
                    if (left == null) root.leftChild else root.rightChild,
                    graph,
                    target - (left ?: right)!!
                )
            '*' -> solveEquation(
                    if (left == null) root.leftChild else root.rightChild,
                    graph,
                    target / (left ?: right)!!
                )
            '-' -> solveEquation(
                if (left == null) root.leftChild else root.rightChild,
                graph,
                if (left == null) target + right!! else left - target
            )
            '/' -> return solveEquation(
                if (left == null) root.leftChild else root.rightChild,
                graph,
                if (left == null) target * right!! else left / target
            )
            else -> throw IllegalStateException("Unrecognized operation: ${root.operation}")
        }
    }
}

fun main() {
    val day = Day21()
    fun part1(input: List<String>): Long {
        val monkeys = input.associate { day.parseLine(it) }
        return day.evalP1(monkeys["root"]!!, monkeys)
    }

    fun part2(input: List<String>): Long {
        val monkeys = input.associate { day.parseLine(it) }.filter { it.key != "humn" }
        val root = monkeys["root"]!!
        val left = day.evalRPN(day.evalP2(root.leftChild!!, monkeys))
        val right = day.evalRPN(day.evalP2(root.rightChild!!, monkeys))
        val target = left ?: right
        return day.solveEquation(
            if (left == null) root.leftChild else root.rightChild,
            monkeys,
            target!!
        )
    }

    val test = readInput("Day21_test")
    println("=== Part 1 (test) ===")
    println(part1(test))
    println("=== Part 2 (test) ===")
    println(part2(test))

    val input = readInput("Day21")
    println("=== Part 1 (puzzle) ===")
    println(part1(input))
    println("=== Part 2 (puzzle) ===")
    println(part2(input))
}