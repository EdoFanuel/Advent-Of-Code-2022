import java.util.*

data class Directory(var totalSize: Long  = 0, val children: MutableMap<String, Directory> = mutableMapOf())

fun buildFolder(commands: List<String>): Map<String, Long> {
    val root = Directory()
    val path = Stack<Directory>()
    var currentDirectory = root

    for (line in commands) {
        val tokens = line.split(" ")
        when (tokens[0]) {
            // $ cd <path> or $ ls
            "$" -> {
                if (tokens[1] == "cd") {
                    currentDirectory = when(tokens[2]) {
                        ".." -> path.pop()  // go back one folder up
                        "/" -> root         // go back all the way up
                        else -> {
                            path.push(currentDirectory) // store current folder to go back later
                            currentDirectory.children[tokens[2]]!! // move to specified folder
                        }
                    }
                }
                else {
                    // ls command. Not doing anything since we'll handle the outputs on the next loop
                }
            }
            // output of ls
            "dir" -> currentDirectory.children[tokens[1]] = Directory() // dir <dir_name>
            else -> currentDirectory.totalSize += tokens[0].toLong() // <file_size> <file_name>
        }
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
