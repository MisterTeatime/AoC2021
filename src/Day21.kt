fun main() {
    fun part1(input: List<String>): Int {
        val die = DeterministicDie(100)

        val positionP1 = input[0].takeLastWhile { it != ' ' }.toInt()
        val positionP2 = input[1].takeLastWhile { it != ' ' }.toInt()
        val p1 = Player("P1", positionP1, 0, die)
        val p2 = Player("P2", positionP2, 0, die)

        while (true) {
            p1.doTurn()
            if (p1.score >= 1000) break
            p2.doTurn()
            if (p2.score >= 1000) break
        }

        return when {
            (p1.score > p2.score) -> p2.score * die.totalRolls
            else -> p1.score * die.totalRolls
        }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day21_test")
    check(part1(testInput) == 739785)

    val input = readInput("Day21")
    println(part1(input))
    println(part2(input))
}

data class Player(val name: String, var position: Int, var score: Int = 0, val die: Die) {
    override fun toString(): String {
        return "$name at ${when (position) { 0 -> 10; else -> position}} with $score points."
    }

    fun doTurn() {
        var steps = 0
        repeat(3) {             //each player gets to roll the die three times per turn
            steps += die.roll()
        }

        move(steps)
    }

    private fun move(steps: Int) {
        position = (position + steps) % 10
        score += when (position){
            0 -> 10
            else -> position
        }
        //println(this)
    }
}

sealed class Die(sides: Int) {
    var rolls = 0
    abstract fun roll(): Int
    val totalRolls: Int
        get() = rolls
}

data class DeterministicDie(val sides: Int): Die(sides) {
    override fun roll(): Int {
        rolls++
        return when (rolls % sides){
            0 -> sides
            else -> rolls % sides
        }
    }

}