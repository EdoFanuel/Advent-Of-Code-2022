fun main() {
    val lowercase = 'a'..'z'
    val uppercase = 'A'..'Z'

    fun part1(input: List<String>): Int {
        var score = 0
        for (line in input) {
            val left = line.substring(0 until line.length / 2).toCharArray().toSet()
            val right = line.substring(line.length / 2 until line.length).toCharArray().toSet()
            for (c in left) {
                if (c in right) {
                    score += if (c in lowercase) lowercase.indexOf(c) + 1 else 0
                    score += if (c in uppercase) uppercase.indexOf(c) + 27 else 0
                }
            }
        }
        return score
    }

    fun part2(input: List<String>): Int {
        var score = 0
        for (i in input.indices step 3) {
            val top = input[i].toCharArray().toSet()
            val mid = input[i + 1].toCharArray().toSet()
            val bot = input[i + 2].toCharArray().toSet()
            for (c in top) {
                if (c in mid && c in bot) {
                    score += if (c in lowercase) lowercase.indexOf(c) + 1 else 0
                    score += if (c in uppercase) uppercase.indexOf(c) + 27 else 0
                }
            }
        }
        return score
    }

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
