import kotlin.math.max

class Day16 {
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
}

fun generateAction(pos: String, openedValve: Set<String>, power: Map<String, Long>, connections: Map<String, Set<String>>): Set<String> {
    val result = mutableSetOf<String>()
    if (pos !in openedValve && power[pos]!! > 0) {
        result += "open $pos"
    }
    for (next in connections[pos]!!) {
        result += "goto $next"
    }
    return result
}

fun generateMultipleActions(positions: List<String>, openedValve: Set<String>, power: Map<String, Long>, connections: Map<String, Set<String>>, i: Int = 0): List<List<String>> {
    if (i >= positions.size) return listOf()

    val playerAction = generateAction(positions[i], openedValve, power, connections)
    if (i == positions.lastIndex) return playerAction.map { listOf(it) }
    val result = mutableListOf<List<String>>()
    for (action in playerAction) {
        val valves = mutableSetOf<String>()
        valves += openedValve
        val (command, target) = action.split(" ")
        if (command == "open") valves += target
        for (nextActions in generateMultipleActions(positions, valves, power, connections, i + 1)) {
            for (nextAction in nextActions) {
                result += listOf(action, nextAction)
            }
        }
    }
    return result
}

fun traverseDFS(key: Day16.CacheKey, power: Map<String, Long>, connections: Map<String, Set<String>>, cache: MutableMap<Day16.CacheKey, Long>): Long {
    val time = key.remainingTime - 1
    if (time < 0) return 0 // time's up. exit recursion
    if (key.openedValve == power.filter { (_, v) -> v > 0}.keys) return 0 // Everything already opened, exit recursion
    if (cache[key] != null) return cache[key]!! // calculation already done, return value stored
    for (actions in generateMultipleActions(key.current, key.openedValve, power, connections)) {
        val nextPosition = Array(key.current.size) { "" }
        val openedValve = mutableSetOf<String>()
        var pressureGain = 0L
        openedValve += key.openedValve
        for ((i, act) in actions.withIndex()) {
            val (command, pos) = act.split(" ")
            when (command) {
                "open" -> {
                    pressureGain += power[pos]!! * time
                    openedValve += pos
                    nextPosition[i] = key.current[i] // no change
                }
                "goto" -> {
                    nextPosition[i] = pos
                }
            }
        }
        cache[key] = max(cache[key] ?: 0, pressureGain + traverseDFS(
            Day16.CacheKey(nextPosition.toList(), openedValve, time),
            power, connections, cache
        ))
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
            Day16.CacheKey(listOf("AA"), mutableSetOf(), 30),
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
        return traverseDFS(
            Day16.CacheKey(listOf("AA", "AA"), mutableSetOf(), 26),
            power,
            graph,
            mutableMapOf()
        )
    }

    val test = readInput("Day16_test")
    println(part1(test))
    println(part2(test))


    val input = readInput("Day16")
    println(part1(input))
    println(part2(input))
}
