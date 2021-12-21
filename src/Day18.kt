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

sealed class SnailfishNumber {
    abstract val magnitude: Long
    abstract fun deepCopy(): SnailfishNumber

    fun add(other: SnailfishNumber): CompoundSFN {
        return CompoundSFN(this, other)
    }
}

data class LiteralSFN(var value: Int): SnailfishNumber() {
    override val magnitude: Long
        get() = value.toLong()

    override fun deepCopy(): SnailfishNumber {
        return this.copy()
    }

    override fun toString(): String {
        return value.toString()
    }
}

data class CompoundSFN(var left: SnailfishNumber, var right: SnailfishNumber): SnailfishNumber() {
    override val magnitude: Long
        get() = 3 * left.magnitude + 2 * right.magnitude

    override fun deepCopy(): SnailfishNumber {
        return this.copy(left.deepCopy(), right.deepCopy())
    }

    override fun toString(): String {
        return "[$left,$right]"

    }
}

data class ExplodeResult(val shouldNuke: Boolean)

class DoneException : Throwable()

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

fun getSNF(input: String): SnailfishNumber {
    return when {
        (input.length == 1) -> LiteralSFN(input.toInt())
        else -> parseSFN(input)
    }
}

fun doExplode(number: SnailfishNumber) {
    var leftValue: LiteralSFN? = null
    var valueToAdd: Int? = null
    var shouldStillExplode = true

    fun explode(sfn: SnailfishNumber, depth: Int): ExplodeResult {
        println("Exploding $sfn")
        when (sfn) {
            is CompoundSFN -> {
                if (depth == 4) {
                    if (shouldStillExplode) {
                        leftValue?.let { it.value += (sfn.left as LiteralSFN).value}
                        valueToAdd = (sfn.right as LiteralSFN).value
                        shouldStillExplode = false
                        return ExplodeResult(shouldNuke = true)
                    } else {
                        explode(sfn.left, depth + 1)
                        explode(sfn.right, depth + 1)
                    }
                } else {
                    if (explode(sfn.left, depth +  1).shouldNuke) {
                        println("$sfn needs explosion of Left=${sfn.left}")
                        println("Before $number")
                        sfn.left = LiteralSFN(0)
                        println("After $number")
                    }
                    if (explode(sfn.right, depth + 1).shouldNuke) {
                        println("$sfn needs explosion of Right=${sfn.right}")
                        println("Before $number")
                        sfn.right = LiteralSFN(0)
                        println("After $number")
                    }
                }
            }
            is LiteralSFN -> {
                leftValue = sfn
                valueToAdd?.let {
                    println("Add $it to $sfn")
                    sfn.value += it
                    println("Current SFN $number")
                    throw DoneException()
                }
            }
        }
        return ExplodeResult(shouldNuke = false)
    }

    explode(number, 0)
}

fun split(sfn: SnailfishNumber): CompoundSFN? {
    when (sfn) {
        is CompoundSFN -> {
            split(sfn.left)?.let {
                println("Split ${sfn.left} into $it")
                sfn.left = it
                throw DoneException()
            }
            split(sfn.right)?.let {
                sfn.right = it
                throw DoneException()
            }
        }
        is LiteralSFN -> {
            if (sfn.value >= 10) {
                val left = sfn.value / 2
                val right = kotlin.math.ceil(sfn.value.toDouble() / 2.0).toInt()
                return CompoundSFN(LiteralSFN(left), LiteralSFN(right))
            }
        }
    }
    return null
}