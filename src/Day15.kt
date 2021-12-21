typealias CavePath = List<Point>

fun main() {
    fun part1(input: List<String>): Long {
        val cave = Area.toIntArea(input)

        val start = Point(0,0)
        val end = Point(cave.points[0].size - 1,cave.points.size - 1)

        val solution = findOptimalPath(start, end, cave)

        return solution.second
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput) == 40L)

    val input = readInput("Day15")
    println(part1(input))
    println(part2(input))
}

fun findOptimalPath(start: Point, end:Point, a: Area<Int>): Pair<CavePath, Long> {
    //contains points discovered, that still must be evaluated. A point is related to its estimated distance to end
    val openPoints = mutableMapOf(start to getEstimateCost(start, end))

    //points already checked and evaluated
    val closedPoints = mutableSetOf<Point>()

    //Relates a point to its cost to reach from start
    val costFromStart = mutableMapOf(start to 0L)

    //Relates a point to its successor with the lowest cost
    val cameFrom = mutableMapOf<Point, Point>()

    while (openPoints.isNotEmpty()) {
        val (currentPoint, _) = openPoints.minByOrNull { it.value }!!

        if (currentPoint == end) {
            val path = generatePath(currentPoint, cameFrom)
            val totalCost = costFromStart[currentPoint]!! + a.at(currentPoint)!! - a.at(start)!!

            return Pair(path, totalCost)
        }

        openPoints.remove(currentPoint)
        closedPoints.add(currentPoint)

        currentPoint.neighbors4()
            .filterNot { a.at(it) == null }             //remove all neighbors outside the area
            .filterNot { closedPoints.contains(it) }    //remove all neighbors already evaluated
            .forEach {
                val cost = costFromStart[currentPoint]!! + a.at(it)!!

                if (cost < (costFromStart[it] ?: Long.MAX_VALUE)) { //this will only hold true, if the point was discovered (then there is a cost from start) and the cheapest path is still more expensive than the current
                    if (!openPoints.contains(it)) {
                        openPoints[it] = cost + getEstimateCost(it, end)
                    }

                    cameFrom[it] = currentPoint
                    costFromStart[it] = cost
                }
            }
    }

    return Pair(listOf(), Long.MAX_VALUE)
}

fun generatePath(pos: Point, cameFrom: MutableMap<Point, Point>): CavePath {
    val path = mutableListOf(pos)
    var current = pos

    while (cameFrom.contains(current)) {
        current = cameFrom.getValue(current)
        path.add(0, current)
    }

    return path
}

fun getEstimateCost(from: Point, to: Point) = from.distanceTo(to).toLong()