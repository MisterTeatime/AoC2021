fun main() {
    fun part1(input: List<String>): Int {
        val displays = input.map { JumbledDisplay(it.split(" ").filter { s -> s.isNotEmpty()})}
        return displays.flatMap { it.digits }.count { it.length <= 4 || it.length == 7 }
    }

    fun part2(input: List<String>): Int {
        val displays = input.map { JumbledDisplay(it.split(" ").filter { s -> s.isNotEmpty()})}

        for (d in displays) d.untangle()

        return displays.map { it.number.toInt() }.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part2(testInput) == 61229)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
