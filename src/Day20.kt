fun main() {
    fun part1(input: List<String>): Int {
        val iea = input[0].map {
            when (it) {
                '#' -> '1'
                '.' -> '0'
                else -> '0'
            }
        }
        val inputImage = Area.toBinaryArea(input.drop(2), one = '#', zero = '.')

        val testPoints = Point(2,2).neighbors8AndSelf().map { inputImage.at(it) ?: 0}.joinToString("").toInt(2)


        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day20_test")
    check(part1(testInput) == 5)

    val input = readInput("Day20")
    println(part1(input))
    println(part2(input))
}
