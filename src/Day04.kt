fun main() {
    fun part1(input: List<String>): Int {

        val numbers = input[0].split(",")
        val boards = input.drop(2).windowed(5,6).map{ BingoBoard(it) }

        for (num in numbers) {
            boards.forEach { b ->
                b.checkNumber(num.toInt()) //check the number
                if (b.bingo) return b.boardSum * num.toInt() //board got bingo, so return its remaining sum times number called
            }
        }

        return 0
    }

    fun part2(input: List<String>): Int {

        val numbers = input[0].split(",")
        val boards = input.drop(2).windowed(5,6).map{ BingoBoard(it) }.toMutableList()

        for (num in numbers) {
            val bingoed: MutableList<BingoBoard> = mutableListOf()
            boards.forEach { b ->
                b.checkNumber(num.toInt())
                if (b.bingo) {
                    when (boards.size) {
                        1 -> return b.boardSum * num.toInt()
                        else -> bingoed.add(b)
                    }
                }
            }
            if (bingoed.size > 0) boards.removeAll(bingoed)     //remove all boards, which bingoed
        }

        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
