typealias Herd = MutableSet<Point2D>

fun main() {
    fun part1(input: List<String>): Int {
        val width = input[0].length
        val height = input.size

        var position = getHerds(input)

        var steps = 0
        var newPosition: List<Herd> = listOf()

        while (true) {
            steps++
            newPosition = doStep(position, width, height)
            if (position == newPosition) break

            position = newPosition
        }

        return steps
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day25_test")
    check(part1(testInput) == 58)

    val input = readInput("Day25")
    println(part1(input))
    println(part2(input))
}

fun getHerds(input: List<String>): List<Herd> {
    val eastBounds = mutableSetOf<Point2D>()
    val southBounds = mutableSetOf<Point2D>()

    //initialize cucumber herds
    for (y in input.indices) {
        val row = input[y]
        for (x in row.indices) {
            when (row[x]) {
                '>' -> eastBounds.add(Point2D(x, y))
                'v' -> southBounds.add(Point2D(x, y))
            }
        }
    }

    return listOf(eastBounds, southBounds)
}

fun doStep(herds: List<Herd>, width: Int, height: Int): List<Herd> {
    val eastbounds = herds[0].toMutableSet()
    val southbounds = herds[1].toMutableSet()

    //move Eastbounds
    val eastMoves = eastbounds.associateBy({it},{moveCucumber(it,Point2D(1,0), width, height)}).filterNot { eastbounds.contains(it.value) || southbounds.contains(it.value) }
    for ((from, to) in eastMoves) eastbounds.replace(from, to)

    //move Southbounds
    val southMoves = southbounds.associateBy({it},{moveCucumber(it,Point2D(0,1), width, height)}).filterNot { eastbounds.contains(it.value) || southbounds.contains(it.value) }
    for ((from, to) in southMoves) southbounds.replace(from, to)

    return listOf(eastbounds, southbounds)
}

fun moveCucumber(cucumber: Point2D, direction: Point2D, width: Int, height: Int): Point2D {
    val destination = cucumber + direction
    destination.x = destination.x % width
    destination.y = destination.y % height

    return destination
}

fun <E> MutableSet<E>.replace(dis: E, dat: E) {
    this.remove(dis)
    this.add(dat)
}