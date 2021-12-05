class Line(coords: List<String>) {
    val start: Point
    val end: Point

    init {
        val startPoint = coords[0].split(",")
        val endPoint = coords[1].split(",")
        this.start = Point(startPoint[0].toInt(), startPoint[1].toInt())
        this.end =  Point(endPoint[0].toInt(), endPoint[1].toInt())
    }

    fun isHorizontal() = start.y == end.y
    fun isVertical() = start.x == end.x
    fun contains(point: Point) = point.y == getYAt(point.x)
    fun getYAt(x: Int) = ((end.y - start.y)/(end.x - start.x)) * (x - start.x) + start.y

    fun getAllIntegerPoints(): List<Point> {
        return when {
            isHorizontal() -> {
                when {
                    (start.x < end.x) -> IntRange(start.x, end.x).map{ p -> Point(p, start.y)}
                    else -> IntRange(end.x, start.x).map{ p -> Point(p, start.y)}
                }
            }
            isVertical() -> {
                when {
                    (start.y < end.y) -> IntRange(start.y, end.y).map{ p -> Point(start.x, p)}
                    else -> IntRange(end.y, start.y).map { p -> Point(start.x, p)}
                }
            }
            else -> {
                when {
                    (start.x < end.x) -> IntRange(start.x, end.x).map{ p -> Point(p, getYAt(p))}
                    else -> IntRange(end.x, start.x).map{ p -> Point(p, getYAt(p))}
                }
            }
        }
    }

    override fun toString(): String {
        return "(${start.x}, ${start.y}) -> (${end.x}, ${end.y})"
    }
}