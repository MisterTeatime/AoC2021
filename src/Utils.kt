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

data class Point2D (var x: Int, var y: Int) {
    operator fun plus(inc: Point2D) = Point2D(x + inc.x, y + inc.y)
    operator fun minus(inc: Point2D) = Point2D(x - inc.x, y - inc.y)

    fun neighbors4(): List<Point2D> {
        val neighborPoints = listOf(Point2D(1,0), Point2D(0,1), Point2D(-1, 0), Point2D(0,-1))
        return neighborPoints.map { this + it }.sortedWith(Point2DComparator())
    }

    fun neighbors8(): List<Point2D> {
        val neighborPoints = listOf(
            Point2D(1,0),
            Point2D(1,1),
            Point2D(0,1),
            Point2D(-1,1),
            Point2D(-1,0),
            Point2D(-1,-1),
            Point2D(0,-1),
            Point2D(1,-1)
        )
        return neighborPoints.map { this + it }.sortedWith(Point2DComparator())
    }

    fun neighbors4AndSelf(): List<Point2D> {
        val neighborPoints = neighbors4().toMutableList()
        neighborPoints.add(this)
        return neighborPoints.sortedWith(Point2DComparator())
    }

    fun neighbors8AndSelf(): List<Point2D> {
        val neighborPoints = neighbors8().toMutableList()
        neighborPoints.add(this)
        return neighborPoints.sortedWith(Point2DComparator())
    }

    fun distanceTo(other: Point2D) = (this.x - other.x).absoluteValue + (this.y - other.y).absoluteValue

    class Point2DComparator: Comparator<Point2D> {
        override fun compare(p0: Point2D?, p1: Point2D?): Int {
            return if (p0 == null || p1 == null)
                0
            else {
                when {
                    (p0.y > p1.y) -> 1
                    (p0.y < p1.y) -> -1
                    else -> {
                        when {
                            (p0.x > p1.x) -> 1
                            (p0.x < p1.x) -> -1
                            else -> 0
                        }
                    }
                }
            }
        }
    }

}

class Line(coords: List<String>) {
    val start: Point2D
    val end: Point2D

    init {
        val startPoint = coords[0].split(",")
        val endPoint = coords[1].split(",")
        this.start = Point2D(startPoint[0].toInt(), startPoint[1].toInt())
        this.end = Point2D(endPoint[0].toInt(), endPoint[1].toInt())
    }

    fun isHorizontal() = start.y == end.y
    fun isVertical() = start.x == end.x
    fun contains(point: Point2D) = point.y == getYAt(point.x)
    fun getYAt(x: Int) = ((end.y - start.y)/(end.x - start.x)) * (x - start.x) + start.y

    fun getAllIntegerPoints(): List<Point2D> {
        return when {
            isHorizontal() -> {
                when {
                    (start.x < end.x) -> IntRange(start.x, end.x).map{ p -> Point2D(p, start.y) }
                    else -> IntRange(end.x, start.x).map{ p -> Point2D(p, start.y) }
                }
            }
            isVertical() -> {
                when {
                    (start.y < end.y) -> IntRange(start.y, end.y).map{ p -> Point2D(start.x, p) }
                    else -> IntRange(end.y, start.y).map { p -> Point2D(start.x, p) }
                }
            }
            else -> {
                when {
                    (start.x < end.x) -> IntRange(start.x, end.x).map{ p -> Point2D(p, getYAt(p)) }
                    else -> IntRange(end.x, start.x).map{ p -> Point2D(p, getYAt(p)) }
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

data class Area<T>(var points: List<MutableList<T>>) {
    fun at(p: Point2D): T? {
        return points.getOrElse(p.y) { listOf() }.getOrNull(p.x)
    }

    fun setValue(p: Point2D, v: T) {
        if (at(p) != null) points[p.y][p.x] = v
    }

    companion object Factory {
        fun toIntArea(input: List<String>): Area<Int> {
            return Area(input.map { it.map { ch -> ch.toString().toInt() }.toMutableList() })
        }

        fun toBinaryArea(input: List<String>, one: Char, zero: Char): Area<Int> {
            return Area(input.map {
                it.map { c ->
                    when (c) {
                        one -> 1
                        zero -> 0
                        else -> 0
                    }
                }.toMutableList()
            })
        }
    }
}

data class Point3D(val x: Int, val y: Int, val z: Int) {
    operator fun plus(inc: Point3D) = Point3D(x + inc.x, y + inc.y, z + inc.z)
    operator fun minus(inc: Point3D) = Point3D(x - inc.x, y - inc.y, z - inc.z)
}