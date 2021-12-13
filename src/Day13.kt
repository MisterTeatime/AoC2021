typealias Fold = Pair<Char, Int>

fun main() {
    fun part1(input: List<String>): Int {
        val points = getPoints(input)
        val folds = getFoldingInstructions(input)

        fold(points, folds[0])

        return points.size
    }

    fun part2(input: List<String>): Int {

        val points = getPoints(input)
        val folds = getFoldingInstructions(input)

        for (i in folds.indices) {
            fold(points, folds[i])
        }
        printOutPoints(points)
        return 0 //not relevant output
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 17)
    part2(testInput)

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}

fun getPoints(input: List<String>) = input
    .takeWhile { it.isNotEmpty() }
    .map { it.split(",") }
    .map { Point(it[0].toInt(), it[1].toInt())}
    .toMutableSet()

fun getFoldingInstructions(input: List<String>) = input
        .takeLastWhile { it.isNotEmpty() }
        .map { it.split("=") }
        .map { Fold(it[0].last(),it[1].toInt()) }

fun fold(points: MutableSet<Point>, fold: Fold) {
    when (fold.first) {
        'x' -> {
            val movingPoints = points.filter { it.x > fold.second }
            points.removeAll(movingPoints.toSet())                                          //remove points, that will be moved
            points.addAll(movingPoints.map { Point(2 * fold.second - it.x, it.y) } )     //add folded points
        }
        'y' -> {
            val movingPoints = points.filter { it.y > fold.second }
            points.removeAll(movingPoints.toSet())                                          //remove points, that will be moved
            points.addAll(movingPoints.map { Point(it.x, 2 * fold.second - it.y) } )     //add folded points

        }
    }
}

/***
 * turns a set of points into a "readable" form.
 * Every point is rendered as "*". By finding the max indices for x and y the "missing" points (points not in list, but below the max indices) are rendered with " "
 */
fun printOutPoints(points: MutableSet<Point>) {
    println("-".repeat(points.maxOf { it.x + 1}))   //just for readability
    for (y in 0..points.maxOf { it.y }) {
        val line = mutableListOf<String>()
        for (x in 0..points.maxOf { it.x }) {
            line.add(
                when {
                    (points.any { it.x == x && it.y == y }) -> "*"      //if there is a points with the current coordinates, print a "*"
                    else -> " "                                         //in any other case print a " "
                }
            )
        }
        println(line.joinToString(""))
    }
    println("-".repeat(points.maxOf { it.x + 1})) //just for readability
}