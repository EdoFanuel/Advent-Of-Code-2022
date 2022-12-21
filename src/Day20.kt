import java.math.BigInteger
import java.util.*
import kotlin.math.max
import kotlin.math.min

class Day20 {
    fun readInput(input: List<String>): List<Long> {
        return input.map { it.toLong() }
    }

    fun decrypt(input: List<Long>, loop: Int = 1): List<Long> {
        val result = input.mapIndexed { i, value -> i to value}.toMutableList()
        repeat(loop) {
            for ((i, value) in input.withIndex()) {
                val idx = result.indexOfFirst { it.first == i }
                Collections.rotate(result, -idx)
                result.removeFirst()
                Collections.rotate(result, (-value % result.size).toInt())
                result.add(0, i to value)
                Collections.rotate(result, (value % result.size).toInt())
            }
        }
        return result.map { it.second }
    }
}

fun main() {
    val day = Day20()

    fun part1(input: List<String>): Long {
        val data = day.readInput(input)
        println("input: $data")
        val decrypted = day.decrypt(data)
        println("output: $decrypted")
        val zeroIdx = decrypted.indexOfFirst { it == 0L}
        val digits = listOf(
            decrypted[(zeroIdx + 1000) % decrypted.size],
            decrypted[(zeroIdx + 2000) % decrypted.size],
            decrypted[(zeroIdx + 3000) % decrypted.size]
        )
        println("$zeroIdx, $digits")
        return digits.sumOf { it }
    }

    fun part2(input: List<String>): Long {
        val decryptionKey = 811_589_153
        val data = day.readInput(input).map { it * decryptionKey }
        println("input: $data")
        val decrypted = day.decrypt(data, 10)
        println("output: $decrypted")
        val zeroIdx = decrypted.indexOfFirst { it == 0L}
        val digits = listOf(
            decrypted[(zeroIdx + 1000) % decrypted.size],
            decrypted[(zeroIdx + 2000) % decrypted.size],
            decrypted[(zeroIdx + 3000) % decrypted.size]
        )
        println("$zeroIdx, $digits")
        return digits.sumOf { it }
    }

    val test = readInput("Day20_test")
    println("=== Part 1 (test) ===")
    println(part1(test))
    println("=== Part 2 (test) ===")
    println(part2(test))

    val input = readInput("Day20")
    println("=== Part 1 (puzzle) ===")
    println(part1(input))
    println("=== Part 2 (puzzle) ===")
    println(part2(input))
}