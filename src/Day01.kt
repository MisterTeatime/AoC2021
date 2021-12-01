fun main() {
    fun part1(input: List<String>): Int {
        return input.windowed(2, 1).map { (first, last) -> if (first.toInt() < last.toInt()) 1 else 0 }.sum()
    }

    fun part2(input: List<String>): Int {
        return input.map { it.toInt() }.windowed(3,1).map { it.sum()}.windowed(2,1).map { (first, last) -> if (first < last) 1 else 0}.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part2(testInput) == 5)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
