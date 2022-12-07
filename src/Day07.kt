import java.util.*

data class Directory(var totalSize: Long  = 0, val children: MutableMap<String, Directory> = mutableMapOf())

fun buildFolder(commands: List<String>): Map<String, Long> {
    val root = Directory()
    val path = Stack<Directory>()
    var currentDirectory = root

    var counter = 0
    while (counter < commands.size) {
        if (commands[counter].startsWith("$")) {
            val command = commands[counter].split(" ")
            when (command[1]) {
                "cd" -> {
                    currentDirectory = when(command[2]) {
                        ".." -> path.pop()
                        "/" -> root
                        else -> {
                            path.push(currentDirectory)
                            currentDirectory.children[command[2]]!!
                        }
                    }
                }
                "ls" -> {
                    counter++
                    while (counter < commands.size && !commands[counter].startsWith("$")) {
                        val lastOutput = commands[counter].split(" ")
                        if (lastOutput[0] == "dir") currentDirectory.children[lastOutput[1]] = Directory()
                        else currentDirectory.totalSize += lastOutput[0].toLong()
                        counter++
                    }
                    counter--
                }
            }
        }
        counter++
    }
    return calculateSize(root)
}

fun calculateSize(root: Directory,
                  path: String = "",
                  cache: MutableMap<String, Long> = mutableMapOf()): Map<String, Long> {
    root.children.forEach { calculateSize(it.value, "$path/${it.key}", cache)}
    cache[path] = root.totalSize + root.children.map { cache["$path/${it.key}"]!! }.sum()
    return cache
}

fun main() {
    fun part1(input: List<String>): Long {
        val folders = buildFolder(input)
        return folders.filter { (_, v) -> v <= 100_000 }.values.sum()
    }

    fun part2(input: List<String>): Long {
        val folders = buildFolder(input)
        val rootSize = folders[""]!!
        return folders.filter { (_, v) -> v > rootSize - 40_000_000L }.values.minOf { it }
    }

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}
