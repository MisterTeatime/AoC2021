fun main() {
    fun getMostCommonBitAt(input: List<String>, index: Int): Char
    {
        val counts = input.groupingBy { it[index] }.eachCount()
        return when {
            counts['0']!! > counts['1']!! -> '0'
            else -> '1'
        }
    }

    fun getLeastCommonBitAt(input: List<String>, index: Int): Char
    {
        val counts = input.groupingBy { it[index] }.eachCount()
        return when {
            counts['0']!! > counts['1']!! -> '1'
            else -> '0'
        }
    }

    fun getGamma(input: List<String>): Int {
        val digits = mutableListOf<Int>()

        for (i in 0 until input[0].length)
        {
            //digits.add(input.groupingBy { it[i]}.eachCount().maxByOrNull { it.value }?.key.toString().toInt())
            digits.add(getMostCommonBitAt(input, i).toString().toInt())
        }
        return digits.joinToString("").toInt(2)
    }

    fun getEpsilon(input: List<String>): Int {
        val digits = mutableListOf<Int>()

        for (i in 0 until input[0].length)
        {
            //digits.add(input.groupingBy { it[i]}.eachCount().minByOrNull { it.value }?.key.toString().toInt())
            digits.add(getLeastCommonBitAt(input, i).toString().toInt())
        }

        return digits.joinToString("").toInt(2)
    }

    fun getOxy(input: List<String>): Int {
        var candidates = input.toMutableList()
        for (i in 0 until input[0].length)
        {
            candidates = candidates.filter{it[i] == getMostCommonBitAt(candidates, i)} as MutableList<String>
            if (candidates.size == 1) break
        }
        return candidates[0].toInt(2)
    }

    fun getCO2(input: List<String>): Int {
        var candidates = input.toMutableList()
        for (i in 0 until input[0].length)
        {
            candidates = candidates.filter{it[i] == getLeastCommonBitAt(candidates, i)} as MutableList<String>
            if (candidates.size == 1) break
        }
        return candidates[0].toInt(2)
    }

    fun part1(input: List<String>): Int {
        return getGamma(input) * getEpsilon(input)
    }

    fun part2(input: List<String>): Int {
        return getOxy(input) * getCO2(input)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part2(testInput) == 230)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
