fun main() {
    fun part1(input: List<String>): Long {

/*
        var polymer = template

        //expand polymer
        for (i in 1..10) {
            polymer = polymer
                .windowed(2,1)                                                // get polymer pairs
                .map { StringBuilder(it) }                                             // turn to StringBuilder for inserting
                .onEach { it.insert(1, rules[it.toString()]!![0]) }              // insert rule result after first character (i.e. in the middle)
                .reduce { acc, sb -> acc.append(sb.toString().takeLast(2)) }        // concatenate expanded pairs by removing the first character of each subsequent group (because it is already the last character of the previous group
                .toString()
        }
        val polymerStats = polymer.groupingBy { it }.eachCount()

        return polymerStats.maxByOrNull { it.value }!!.value - polymerStats.minByOrNull { it.value }!!.value   */

        val (min, max) = getMinMax(developPolymer(10, input))
        return max - min
    }

    fun part2(input: List<String>): Long {
        val (min, max) = getMinMax(developPolymer(40, input))
        return max - min
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part2(testInput) == 2188189693529L)

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}

fun developPolymer(steps: Int, input: List<String>): MutableMap<Char, Long> {
    val template = input[0]

    // turn list of rules into map relating a polymer sequence to the insert element
    val rules = input
        .takeLastWhile { it.isNotEmpty() }
        .map { it.split(" -> ") }
        .associateBy({ it[0] }, { it[1] })

    //turn template to polymer consisting of pairs and their count
    var polymer = template
        .windowed(2)
        .groupingBy { it }.eachCount()
        .mapValues { it.value.toLong() }

    repeat(steps) {
        //each pair is turned into new pairs of first part + insert and insert + second part. Each new pair gets the same count from its parent
        //then regroup and add up
        polymer = polymer.flatMap { (a,c) ->
            val b = rules[a]!!
            listOf("${a[0]}$b" to c, "$b${a[1]}" to c)
        }.groupingBy { it.first }.fold(0L) { a, e -> a + e.second }
    }

    //get a list of letters and their counts from the polymer
    val letterList = polymer.toList()
        .flatMap { (p, c) -> listOf(p[0] to c, p[1] to c) }
        .groupingBy { it.first }.fold(0L) { a, e -> a + e.second }
        .toMutableMap()

    //first and last character of template are missing one instance (for min max step)
    letterList[template.first()] = letterList[template.first()]!! + 1
    letterList[template.last()] = letterList[template.last()]!! + 1

    return letterList
}

fun getMinMax(letterList: MutableMap<Char, Long>): Pair<Long, Long> {
    //reduce map to list of counts (since the element is not important)
    //values need to be halved, because each letter is counted twice. E.g.: AB -> AC, CB -> A=1+1, B=1+1, C=2
    val letterCount = letterList.map { it.value / 2 }

    //return Pair with min value as first and max value as second
    return Pair(letterCount.minOrNull()!!, letterCount.maxOrNull()!!)
}