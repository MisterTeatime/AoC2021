import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.math.absoluteValue

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

typealias Graph = Map<String, List<String>>
typealias Path = List<String>
typealias Fold = Pair<Char, Int>

data class Point (var x: Int, var y: Int) {
    operator fun plus(inc: Point) = Point(this.x + inc.x, this.y + inc.y)
    operator fun minus(inc: Point) = Point(this.x - inc.x, this.y - inc.y)

    fun neighbors4(): List<Point> {
        val neighborPoints = listOf(Point(1,0), Point(0,1), Point(-1, 0), Point(0,-1))
        return neighborPoints.map { this + it }
    }

    fun neighbors8(): List<Point> {
        val neighborPoints = listOf(
            Point(1,0),
            Point(1,1),
            Point(0,1),
            Point(-1,1),
            Point(-1,0),
            Point(-1,-1),
            Point(0,-1),
            Point(1,-1)
        )
        return neighborPoints.map { this + it }
    }

    fun distanceTo(other: Point) = (this.x - other.x).absoluteValue + (this.y - other.y).absoluteValue
}

class Line(coords: List<String>) {
    val start: Point
    val end: Point

    init {
        val startPoint = coords[0].split(",")
        val endPoint = coords[1].split(",")
        this.start = Point(startPoint[0].toInt(), startPoint[1].toInt())
        this.end = Point(endPoint[0].toInt(), endPoint[1].toInt())
    }

    fun isHorizontal() = start.y == end.y
    fun isVertical() = start.x == end.x
    fun contains(point: Point) = point.y == getYAt(point.x)
    fun getYAt(x: Int) = ((end.y - start.y)/(end.x - start.x)) * (x - start.x) + start.y

    fun getAllIntegerPoints(): List<Point> {
        return when {
            isHorizontal() -> {
                when {
                    (start.x < end.x) -> IntRange(start.x, end.x).map{ p -> Point(p, start.y) }
                    else -> IntRange(end.x, start.x).map{ p -> Point(p, start.y) }
                }
            }
            isVertical() -> {
                when {
                    (start.y < end.y) -> IntRange(start.y, end.y).map{ p -> Point(start.x, p) }
                    else -> IntRange(end.y, start.y).map { p -> Point(start.x, p) }
                }
            }
            else -> {
                when {
                    (start.x < end.x) -> IntRange(start.x, end.x).map{ p -> Point(p, getYAt(p)) }
                    else -> IntRange(end.x, start.x).map{ p -> Point(p, getYAt(p)) }
                }
            }
        }
    }

    override fun toString(): String {
        return "(${start.x}, ${start.y}) -> (${end.x}, ${end.y})"
    }
}

class MyStack<E>(vararg items: E): Collection<E> {
    private val elements = items.toMutableList()

    fun push(element: E) = elements.add(element)
    fun peek(): E = elements.last()
    fun pop(): E = elements.removeAt(elements.size-1)
    override fun isEmpty() = elements.isEmpty()
    override fun contains(element: E) = elements.contains(element)
    override fun containsAll(elements: Collection<E>) = elements.containsAll(elements)
    override fun toString() = "Stack(${elements.joinToString()})"
    override fun iterator() = elements.iterator()
    override val size: Int
        get() = elements.size
}

data class Area(var points: List<MutableList<Int>>) {
    fun at(p: Point): Int? {
        return points.getOrElse(p.y) { listOf() }.getOrNull(p.x)
    }

    fun setValue(p: Point, v: Int) {
        if (at(p) != null) points[p.y][p.x] = v
    }

    companion object Factory {
        fun toArea(input: List<String>): Area {
            return Area(input.map { it.map { ch -> ch.toString().toInt() }.toMutableList() })
        }
    }
}