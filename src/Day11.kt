fun processFlashes(a: Area): Int {
    val candidates = getLoadedOctopuses(a)
    val triggered = mutableSetOf<Point>()

    while (candidates.isNotEmpty()) {
        val candidate = candidates.removeFirst()
        triggered.add(candidate)
        a.setValue(candidate, 0)

        val neighbors = candidate
            .neighbors8()
            .filterNot { triggered.contains(it) }
            .filter { a.at(it) != null }
            .onEach { increaseEnergy(a, it)}
            .filter { (a.at(it) ?: 0) > 9 }
            .filterNot { candidates.contains(it) }
        //increaseEnergy(a, neighbors)
        candidates.addAll(neighbors)
    }

    return triggered.size
}

fun increaseAllEnergy(a: Area) {
    for (rowIdx in a.points.indices) {
        val row = a.points[rowIdx]
        for (colIdx in row.indices) {
            a.points[rowIdx][colIdx] += 1
        }
    }
}

fun increaseEnergy(a: Area, p: Point) {
    a.setValue(p, (a.at(p) ?: 0) + 1)
}

fun getLoadedOctopuses(a: Area): MutableList<Point> {
    val loaded = mutableListOf<Point>()
    for (rowIdx in a.points.indices) {
        val row = a.points[rowIdx]
        for (colIdx in row.indices) {
            val candidate = Point(colIdx, rowIdx)
            if ((a.at(candidate) ?: 0) > 9) loaded.add(candidate)
        }
    }
    return loaded
}

fun main() {
    fun part1(input: List<String>): Int {
        val area = Area.toArea(input)
        var flashes = 0
        
        for (i in 1..100) {
            increaseAllEnergy(area)
            flashes += processFlashes(area)
        }
        
        return flashes
    }

    fun part2(input: List<String>): Int {
        val area = Area.toArea(input)
        var flashes = 0
        var step = 0

        while (flashes < 100) {
            step++
            increaseAllEnergy(area)
            flashes = processFlashes(area)
        }

        return step
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part2(testInput) == 195)

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}
