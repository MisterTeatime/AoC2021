class BingoBoard (input: List<String>){
    private var lines = mutableListOf<BingoLine>()
    private val processedInput = input.map{ it -> it.split(" ").filter{ s -> s.isNotEmpty()}}

    init {
        for (l in processedInput) {
            lines.add(BingoLine(l))
        }

        for (i in 0..4) {
            lines.add(BingoLine(processedInput.map { it[i] }))
        }
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

    fun isComplete(): Boolean = completed == 5
    fun check(number: String) {
        if (line.contains(number.toInt())) completed++
    }

    override fun toString(): String {
        return line.toString()
    }
}