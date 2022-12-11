class Monkey(private val items: MutableList<Long>,
             private val operation: Char,
             private val value: Int,
             val divisor: Int,
             private val passTarget: Int,
             private val failTarget: Int) {
    var itemsProcessed = 0L

    fun addItem(item: Long) {
        items += item
    }

    fun inspectP1(item: Long): Long {
        return when(operation) {
            'x' -> item * value
            '+' -> item + value
            '^' -> {
                var result = 1L
                for (i in 0 until value) {
                    result *= item
                }
                result
            }
            else -> throw UnsupportedOperationException()
        }
    }

    fun inspectP2(item: Long, lcm: Long): Long {
        return when(operation) {
            'x' -> (item * value) % lcm
            '+' -> (item + value) % lcm
            '^' -> {
                var result = 1L
                for (i in 0 until value) {
                    result = (result * item) % lcm
                }
                result
            }
            else -> throw UnsupportedOperationException()
        }
    }

    private fun test(value: Long) = if (value % divisor == 0L) passTarget else failTarget

    fun processAllItemsP1(): List<Pair<Long, Int>> {
        if (items.isEmpty()) return listOf()
        val result = mutableListOf<Pair<Long, Int>>()
        for (item in items) {
            val newValue = inspectP1(item) / 3
            result += newValue to test(newValue)
        }
        itemsProcessed += items.size
        items.clear()
        return result
    }

    fun processAllItemsP2(lcm: Long): List<Pair<Long, Int>> {
        if (items.isEmpty()) return listOf()
        val result = mutableListOf<Pair<Long, Int>>()
        for (item in items) {
            val newValue = inspectP2(item, lcm)
            result += newValue to test(newValue)
        }
        itemsProcessed += items.size
        items.clear()
        return result
    }
}

fun main() {
    fun part1(input: Array<Monkey>): Long {
        for (i in 0 until 20) {
            for (monkey in input) {
                for ((item, target) in monkey.processAllItemsP1()) {
                    input[target].addItem(item)
                }
            }
        }
        input.sortByDescending { it.itemsProcessed }
        return input[0].itemsProcessed * input[1].itemsProcessed
    }

    fun part2(input: Array<Monkey>): Long {
        val lcm = input.map { it.divisor.toLong() }.reduce { acc, i -> acc * i }
        for (i in 0 until 10_000) {
            for (monkey in input) {
                for ((item, target) in monkey.processAllItemsP2(lcm)) {
                    input[target].addItem(item)
                }
            }
        }
        input.sortByDescending { it.itemsProcessed }
        return input[0].itemsProcessed * input[1].itemsProcessed
    }

    fun generateData(input: List<String>): Array<Monkey> {
        return input.map { it ->
            val tokens = it.split(" ")
            Monkey(
                tokens[0].split("|").map { item -> item.toLong() }.toMutableList(),
                tokens[1][0],
                tokens[2].toInt(),
                tokens[3].toInt(),
                tokens[4].toInt(),
                tokens[5].toInt()
            )
        }.toTypedArray()
    }
    val test = readInput("Day11_test")
    println(part1(generateData(test)))
    println(part2(generateData(test)))

    val input = readInput("Day11")
    println(part1(generateData(input)))
    println(part2(generateData(input)))
}