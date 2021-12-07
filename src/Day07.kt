import kotlin.math.*

fun main() {
    fun part1(input: List<String>): Int {
        val crabs = input[0].split(",").map { it.toInt() }.sorted()
        val median = crabs[crabs.size / 2]
        return crabs.sumOf { crab -> abs(crab - median) }
    }

    fun part2(input: List<String>): Int {
        val crabs = input[0].split(",").map { it.toInt() }
        val mean = (crabs.sum() / crabs.size)
        return crabs.sumOf { crab ->
            val distance = abs(crab - mean)
            (distance * (distance + 1))/2
        }
    }

    // test if implementation meets criteria from the description, like:
    //val testInput = readInput("Day07_test")
    //check(part2(testInput) == 168)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}
