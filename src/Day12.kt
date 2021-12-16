fun main() {
    fun part1(input: List<String>): Int {
        return toGraph(input).findPaths(smallVisits = 0).size
    }

    fun part2(input: List<String>): Int {
        return toGraph(input).findPaths(smallVisits = 1).size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test1")
    check(part2(testInput) == 36)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}

fun toGraph(input: List<String>) = input
        .map { it.trim().split(Regex("\\W+")) }
        .flatMap { listOf(it, it.reversed()) }
        .groupBy({ it[0] }, { it[1] })

fun Graph.findPaths(name: String = "start", path: Path = listOf(), smallVisits: Int): List<Path> {
    return when {
        name == "end" -> listOf(path + name)
        name == "start" && path.isNotEmpty() -> emptyList()
        path.noRevisit(name, smallVisits) -> emptyList()
        else -> targets(name).flatMap { findPaths(it, path + name, smallVisits) }
    }
}

fun Path.noRevisit(name: String, smallVisits: Int) = contains(name) && name.isSmall() &&
        groupingBy { it }.eachCount().count { it.key.isSmall() && it.value >= 2 } == smallVisits

fun Graph.targets(name: String) = get(name)!!

fun String.isSmall() = this[0].isLowerCase()
