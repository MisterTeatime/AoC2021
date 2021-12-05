fun main() {
    fun part1(input: List<String>): Int {

//        val test = input.slice(2..6).map { it -> it.split(" ").filter { s -> s.isNotEmpty()}}
//        println(test)
//        for (i in 0..4) {
//            println(test.map {it -> it[i]})
//        }
        val numbers = input[0].split(",")
        val boards = input.drop(2).windowed(5,6).map{ BingoBoard(it) }

        println(numbers)
        println(boards)

        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 4512)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
