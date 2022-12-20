import java.math.BigInteger
import java.util.*
import kotlin.math.max
import kotlin.math.min

class Day20 {
    fun readInput(input: List<String>): List<BigInteger> {
        return input.map { it.toBigInteger() }
    }

    fun decrypt(input: List<BigInteger>): List<BigInteger> {
        val result = input.mapIndexed { i, value -> i to value}.toMutableList()
        for (i in input.indices) {
            val (idx, elem) = result.withIndex().first { it.value.first == i}
            result.removeAt(idx)
            val newIdx = (idx.toBigInteger() + elem.second).mod(input.size.toBigInteger() - BigInteger.ONE).toInt()
            result.add(newIdx, elem)
        }
        return result.map { it.second }
    }
}

fun main() {
    val day = Day20()

    fun part1(input: List<String>): BigInteger {
        val data = day.readInput(input)
        println("input: $data")
        val decrypted = day.decrypt(data)
        println("output: $decrypted")
        val zeroIdx = decrypted.indexOf(BigInteger.ZERO)
        val digits = listOf(
            decrypted[(zeroIdx + 1000) % decrypted.size],
            decrypted[(zeroIdx + 2000) % decrypted.size],
            decrypted[(zeroIdx + 3000) % decrypted.size]
        )
        println("$zeroIdx, $digits")
        return digits.sumOf { it }
    }

    fun part2(input: List<String>): BigInteger {
        val decryptionKey = 811_589_153
        var data = day.readInput(input).map { it.multiply(decryptionKey.toBigInteger()) }
        println("input: $data")
        for (i in 0 until 10) {
            data = day.decrypt(data)
            println("iteration #$i: $data")
        }
        println("output: $data")
        val zeroIdx = data.indexOf(BigInteger.ZERO)
        val digits = listOf(
            data[(zeroIdx + 1000) % data.size],
            data[(zeroIdx + 2000) % data.size],
            data[(zeroIdx + 3000) % data.size]
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