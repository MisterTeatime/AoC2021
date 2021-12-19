fun main() {
    fun part1(input: List<String>): Int {
        val test = parseSFN(input[0])

        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day18_test")
    check(part1(testInput) == 5)

    val input = readInput("Day18")
    println(part1(input))
    println(part2(input))
}

/*****
 * Turns a string representation of a valid Snailfishnumber (SFN) into a SFN.
 * A SFN starts with [ and ends with ]. It contains two parts, which can be literal or another SFN
 */

fun parseSFN(input: String): CompoundSFN {
    val content = input.drop(1).dropLast(1)
    val index = getSeparatorIndex(content)
    val (before, after) = content.splitAt(index)

    val left = getSNF(before)
    val right = getSNF(after)

    return CompoundSFN(left, right)
}

sealed class SFNType {
    abstract val magnitude: Long
}

data class LiteralSFN(var value: Int): SFNType() {
    override val magnitude: Long
        get() = value.toLong()

    override fun toString(): String {
        return value.toString()
    }
}

data class CompoundSFN(val left: SFNType, val right: SFNType): SFNType() {
    override val magnitude: Long
        get() = 3 * left.magnitude + 2 * right.magnitude

    override fun toString(): String {
        return "[$left,$right]"

    }
}

/****
 * finds the index of the separating comma.
 * Brackets are counted. The first comma after all pairs are complete the first time, is the separator of this number
 */
fun getSeparatorIndex(input: String): Int {
    var brackets = 0
    for (i in input.indices) {
        when {
            (input[i] == '[') -> brackets += 1
            (input[i] == ']') -> brackets -= 1
            (input[i] == ',' && brackets == 0) -> return i
        }
    }
    return -1
}

/******
 * splits String at index removing the character at index
 */
fun String.splitAt(idx: Int): Pair<String, String> {
    return Pair(this.substring(0, idx), this.substring(idx + 1))
}

fun getSNF(input: String): SFNType {
    return when {
        (input.length == 1) -> LiteralSFN(input.toInt())
        else -> parseSFN(input)
    }
}