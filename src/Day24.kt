import java.util.*

fun main() {
    fun part1(input: List<String>): Int {
        val data = "13579246899999"

        val alu = SubmarineALU(input)
        alu.run(data)

        return alu.register["z"]!!
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day24_test")
    //check(part1(testInput) == 5)

    val input = readInput("Day24")
    println(part1(input))
    println(part2(input))
}
