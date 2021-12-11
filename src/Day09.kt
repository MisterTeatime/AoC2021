fun isLowPoint(p: Point, area: Area): Boolean {
    val height = area.at(p) ?: Int.MAX_VALUE
    return p.neighbors4()
        .map { area.at(it) ?: Int.MAX_VALUE }
        .none { neighborHeight -> neighborHeight <= height }
}

fun findAllLowPoints(area: Area): List<Point> {
    val lowPoints = mutableListOf<Point>()

    for (rowIdx in area.points.indices) {
        val row = area.points[rowIdx]
        for (colIdx in row.indices) {
            val candidate = Point(colIdx, rowIdx)
            if (isLowPoint(candidate, area)) {
                lowPoints.add(candidate)
            }
        }
    }
    return lowPoints
}

fun discoverBasinSize(p: Point, a: Area) : Int {
    val basin = mutableSetOf(p)
    val candidates = mutableListOf(p)

    while (candidates.isNotEmpty()) {
        val candidate = candidates.removeFirst()
        basin.add(candidate)
        val candidateHeight = a.at(candidate) ?: Int.MAX_VALUE
        val neighbors = candidate
            .neighbors4()
            .filterNot { basin.contains(it) }
            .filter { a.at(it) != null }
            .filter { (a.at(it) ?: Int.MAX_VALUE) >= candidateHeight }
            .filter { (a.at(it) ?: Int.MAX_VALUE) < 9}
        candidates.addAll(neighbors)
    }
    return basin.size
}

fun findAllBasinSizes(area: Area) : List<Int> {
    return findAllLowPoints(area).map { discoverBasinSize(it, area) }
}

fun main() {
    fun part1(input: List<String>): Int {
        val area = Area.toArea(input)
        return findAllLowPoints(area).sumOf { area.at(it)!! + 1 }
    }

    fun part2(input: List<String>): Int {
        return findAllBasinSizes(Area.toArea(input)).sorted().reversed().take(3).fold(1) { x, y -> x * y }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part2(testInput) == 1134)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))


}
