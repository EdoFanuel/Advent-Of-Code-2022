class Day19 {
    private val pattern =
        "Blueprint (\\d+): Each ore robot costs (\\d+) ore. Each clay robot costs (\\d+) ore. Each obsidian robot costs (\\d+) ore and (\\d+) clay. Each geode robot costs (\\d+) ore and (\\d+) obsidian."
    private val maxCapacity = 100

    data class Material(val ore: Int, val clay: Int, val obsidian: Int, val geode: Int) {
        operator fun plus(other: Material): Material = Material(
            this.ore + other.ore,
            this.clay + other.clay,
            this.obsidian + other.obsidian,
            this.geode + other.geode
        )

        operator fun unaryMinus(): Material = Material(-this.ore, -this.clay, -this.obsidian, -this.geode)
        operator fun minus(other: Material): Material = this + -other
        fun gte(other: Material) =
            this.ore >= other.ore && this.clay >= other.clay && this.obsidian >= other.obsidian && this.geode >= other.geode
        fun lte(other: Material) =
            this.ore <= other.ore && this.clay <= other.clay && this.obsidian <= other.obsidian && this.geode <= other.geode

    }

    data class Robot(val input: Material, val output: Material)
    data class State(val available: Material, val production: Material, val time: Int)

    fun traverse(state: State, blueprint: Map<String, Robot>, cache: MutableMap<State, Int> = mutableMapOf()): Int {
        if (state.time == 0) return state.available.geode
        if (state !in cache) {
            cache[state] = evaluate(blueprint, state).maxOf { afterBuild ->
                val afterProduce = afterBuild.copy(
                    time = afterBuild.time - 1,
                    available = afterBuild.available + state.production,
                    production = afterBuild.production
                )
                traverse(afterProduce, blueprint, cache)
            }
        }
        return cache[state]!!
    }

    private fun evaluate(blueprint: Map<String, Robot>, state: State): List<State> {
        val current = state.available
        val prev = state.available - state.production

        // We consider building a miner only if we couldn't build it in the previous step
        if (!canBuild(blueprint["geode"]!!, prev) && canBuild(blueprint["geode"]!!, current)) {
            return listOf(buildRobot(state, blueprint["geode"]!!))
        }
        val postConstructStates = mutableListOf<State>()
        for (type in listOf("obsidian", "clay", "ore")) {
            if (!canBuild(blueprint[type]!!, prev) && canBuild(blueprint[type]!!, current)) {
                postConstructStates += buildRobot(state, blueprint[type]!!)
            }
        }
        postConstructStates += state // do nothing
        return postConstructStates
    }

    private fun canBuild(robot: Robot, material: Material) = material.gte(robot.input)

    private fun buildRobot(state: State, robot: Robot) = state.copy(
        time = state.time,
        available = state.available - robot.input,
        production = state.production + robot.output
    )

    fun readInput(input: String): Pair<Int, Map<String, Robot>> {
        val matches = input.match(pattern)!!.groupValues
        val id = matches[1].toInt()
        val oreRobot = Robot(
            Material(matches[2].toInt(), 0, 0, 0),
            Material(1, 0, 0, 0)
        )
        val clayRobot = Robot(
            Material(matches[3].toInt(), 0, 0, 0),
            Material(0, 1, 0, 0)
        )
        val obsidianRobot = Robot(
            Material(matches[4].toInt(), matches[5].toInt(), 0, 0),
            Material(0, 0, 1, 0)
        )
        val geodeRobot = Robot(
            Material(matches[6].toInt(), 0, matches[7].toInt(), 0),
            Material(0, 0, 0, 1)
        )
        return id to mapOf(
            "ore" to oreRobot,
            "clay" to clayRobot,
            "obsidian" to obsidianRobot,
            "geode" to geodeRobot
        )
    }
}

fun main() {
    val day = Day19()

    fun part1(input: List<String>): Long {
        var result = 0L
        for (line in input) {
            val (id, robots) = day.readInput(line)
            val maxGeode = day.traverse(
                Day19.State(
                    Day19.Material(0, 0, 0, 0),
                    Day19.Material(1, 0, 0, 0),
                    24
                ),
                robots
            )
            println("$id, $maxGeode")
            result += id * maxGeode
        }
        return result
    }

    fun part2(input: List<String>): Long {
        var result = 1L
        for (line in if (input.size >= 3) input.subList(0, 3) else input) {
            val (id, robots) = day.readInput(line)
            val maxGeode = day.traverse(
                Day19.State(
                    Day19.Material(0, 0, 0, 0),
                    Day19.Material(1, 0, 0, 0),
                    32
                ),
                robots
            )
            println("$id, $maxGeode")
            result *= maxGeode
        }
        return result
    }

    val test = readInput("Day19_test")
    println("=== Part 1 (test) ===")
    println(part1(test))
    println("=== Part 2 (test) ===")
    println(part2(test))

    val input = readInput("Day19")
    println("=== Part 1 (puzzle) ===")
    println(part1(input))
    println("=== Part 2 (puzzle) ===")
    println(part2(input))
}
