//delimiter of a chunk with start as key and end as value
val chunks = mapOf(
    '(' to ')',
    '[' to ']',
    '{' to '}',
    '<' to '>'
)

//scoring table for syntax checkers
val corruptScore = mapOf(
    ')' to 3,
    ']' to 57,
    '}' to 1197,
    '>' to 25137
)

//scoring table for autocompleter
val autocompleteScore = mapOf(
    ')' to 1L,
    ']' to 2L,
    '}' to 3L,
    '>' to 4L
)
fun main() {
    fun part1(input: List<String>): Int {
        return input.filter { isCorrupt(it) }.sumOf { corruptScore[getFirstCorrupt(it)]!! }
    }

    fun part2(input: List<String>): Long {
        val autoCompleteScores = input.filterNot { isCorrupt(it) }.map { getAutocompleteScore(it) }.sorted()
        return autoCompleteScores[autoCompleteScores.size/2]
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part2(testInput) == 288957L)

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}

fun getFirstCorrupt(line: String): Char? {
    val lastSeen = MyStack<Char>()

    for (i in line.indices) {
        when {
            line[i] in chunks.keys -> lastSeen.push(line[i])        //a new chunk begins, push the start character onto the stack
            line[i] == chunks[lastSeen.peek()] -> lastSeen.pop()    //current character matches the last opened chunk, remove start character from stack
            else -> return line[i]                                  //line is corrupt
        }
    }
    return null   //nothing found, line not corrupt
}

fun isCorrupt(line: String): Boolean = getFirstCorrupt(line) != null

fun getAutocompleteScore(line: String): Long {
    val lastSeen = MyStack<Char>()

    for (i in line.indices) {
        when {
            line[i] in chunks.keys -> lastSeen.push(line[i])
            line[i] == chunks[lastSeen.peek()] -> lastSeen.pop()
        }
    }
    return lastSeen.toList().reversed().map { autocompleteScore[chunks[it]]!! }.fold(0L) { acc, e -> acc * 5 + e}
}
