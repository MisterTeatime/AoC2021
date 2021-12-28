class BingoBoard (input: List<String>){
    private var lines = mutableListOf<BingoLine>()
    private val processedInput = input.map{ it -> it.split(" ").filter{ s -> s.isNotEmpty()}}
    var boardSum = input.joinToString(" ").split(" ").filterNot { it.isEmpty() }.sumOf { it.toInt() }

    val bingo: Boolean
        get() = lines.any { it.complete }

    init {
        for (l in processedInput) {
            lines.add(BingoLine(l)) //add each horizontal line to the list of bingo lines
        }

        for (i in 0..4) {
            lines.add(BingoLine(processedInput.map { it[i] })) //add each vertical line to the list of bingo lines
        }
    }

    /****
     * checks, if the called number is on the board.
     * Each line checks the called number. On a hit deduct to called number from the board sum
     */
    fun checkNumber(num: Int) {
        val check = lines.map { it.check(num) }.filter { it }
        if (check.isNotEmpty())
            boardSum -= num
    }

    override fun toString(): String {
        return lines.toString()
    }
}

class BingoLine (input: List<String>) {
    private val line: List<Int> //members of this BingoLine
    var completed: Int //amount of members already checked

    init {
        this.line = input.map { it.toInt() }
        this.completed = 0
    }

    val complete: Boolean
        get() = completed == 5

    fun check(number: Int): Boolean {
        if (line.contains(number)) {
            completed++
            return true
        }
        return false
    }

    override fun toString(): String {
        return line.toString()
    }
}