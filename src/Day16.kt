import kotlin.math.max

data class CacheKey(val current: List<String>, val openedValve: Set<String>, val remainingTime: Int) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CacheKey

        if (current != other.current) return false
        if (openedValve != other.openedValve) return false
        if (remainingTime != other.remainingTime) return false

        return true
    }

    override fun hashCode(): Int {
        var result = current.hashCode()
        result = 31 * result + openedValve.hashCode()
        result = 31 * result + remainingTime
        return result
    }
}

fun traverseDFS(key: CacheKey, power: Map<String, Long>, connections: Map<String, Set<String>>, cache: MutableMap<CacheKey, Long>): Long {
    val time = key.remainingTime - 1
    if (time < 0) return 0
    if (key.openedValve == power.filter { (_, v) -> v > 0}.keys) return 0 // Everything already opened, exit recursion
    if (cache[key] != null) return cache[key]!!
    for (pos in key.current) {
        if (pos !in key.openedValve && power[pos]!! > 0) {
            val pressure = power[pos]!! * time
            val openedValve = mutableSetOf<String>()
            openedValve += key.openedValve
            openedValve += key.current
            cache[key] = max(cache[key] ?: 0, pressure + traverseDFS(
                CacheKey(key.current, openedValve, time),
                power, connections, cache
            )) // turn on the valve
        }
        for (next in connections[pos]!!) {
            cache[key] = max(cache[key] ?: 0, traverseDFS(
                CacheKey(listOf(next), key.openedValve, time),
                power, connections, cache
            ))
        }
    }
    return cache[key]!!
}

fun main() {
    val pattern = "Valve ([A-Z]{2}) has flow rate=(\\d+); tunnels? leads? to valves? ([A-Z, ]*)"

    fun part1(input: List<String>): Long {
        val power = mutableMapOf<String, Long>()
        val graph = mutableMapOf<String, Set<String>>()
        for (line in input) {
            val (_, source, flow, connections) = line.match(pattern)!!.groupValues
            power[source] = flow.toLong()
            graph[source] = connections.split(", ").toSet()
        }
        return traverseDFS(
            CacheKey(listOf("AA"), mutableSetOf(), 30),
            power,
            graph,
            mutableMapOf()
        )
    }

    fun part2(input: List<String>): Long {
        val power = mutableMapOf<String, Long>()
        val graph = mutableMapOf<String, Set<String>>()
        for (line in input) {
            val (_, source, flow, connections) = line.match(pattern)!!.groupValues
            power[source] = flow.toLong()
            graph[source] = connections.split(", ").toSet()
        }
        return 0
    }

    val test = readInput("Day16_test")
    println(part1(test))
    println(part2(test))


    val input = readInput("Day16")
    println(part1(input))
    println(part2(input))
}
