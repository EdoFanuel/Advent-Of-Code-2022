import kotlin.math.abs

data class Sensor(val source: Pair<Int, Int>, val target: Pair<Int, Int>, val radius: Int) {
    fun inRange(other: Pair<Int, Int>): Boolean {
        return (abs(source.first - other.first) + abs(source.second - other.second)) <= radius
    }
}

fun readSensor(input: List<String>): List<Sensor> {
    val xPattern = Regex("x=(-?\\d*)")
    val yPattern = Regex("y=(-?\\d*)")
    return input.map { line ->
        val (x1, x2) = xPattern.findAll(line).map { it.groupValues[1].toInt() }.toList()
        val (y1, y2) = yPattern.findAll(line).map { it.groupValues[1].toInt() }.toList()
        val radius = abs(x1 - x2) + abs(y1 - y2)
        Sensor(x1 to y1, x2 to y2, radius)
    }
}

fun main() {
    fun part1(input: List<Sensor>, y: Int): Int {
        val covered = mutableSetOf<Int>()
        for (sensor in input) {
            val (x1, y1) = sensor.source
            val yDist = abs(y1 - y)
            if (yDist > sensor.radius) continue
            val xDist = sensor.radius - yDist
            covered += (x1 - xDist)..(x1 + xDist)
        }
        return covered.size - input.map { it.target }.filter { it.second == y }.toSet().size
    }

    fun part2(input: List<Sensor>, range: IntRange): Long {
        for (sensor in input) {
            // extend each sensor by 1, see if it's contained in other sensors or not
            for (i in -sensor.radius - 1 .. sensor.radius + 1) {
                val x = sensor.source.first + i
                val dy = sensor.radius - abs(i) + 1
                for (y in arrayOf(sensor.source.second - dy, sensor.source.second + dy)) {
                    if (x !in range) continue
                    if (y !in range) continue
                    if (input.all { !it.inRange(x to y) }) {
                        println("${x to y}")
                        return x * 4_000_000L + y
                    }
                }
            }
        }
        return 0
    }

    val test = readInput("Day15_test")
    val testSensors = readSensor(test)
    println(testSensors)
    println(part1(testSensors, 10))
    println(part2(testSensors, 0..20))

    val input = readInput("Day15")
    val inputSensors = readSensor(input)
    println(inputSensors)
    println(part1(inputSensors, 2_000_000))
    println(part2(inputSensors, 0..4_000_000))
}