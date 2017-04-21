package by.mksn.miapr.grammar

data class Point(var x: Double, var y: Double) {

    fun move(deltaX: Double, deltaY: Double) {
        x += deltaX
        y += deltaY
    }

    companion object {
        val NONE = Point(Double.NaN, Double.NaN)
    }

    fun isSame(other: Point)
            = other.x > x - 0.01 && other.x < x + 0.01 && other.y > y - 0.01 && other.y < y + 0.01
}

data class Line(var from: Point, var to: Point) {
    fun move(deltaX: Double, deltaY: Double) {
        from.move(deltaX, deltaY)
        to.move(deltaX, deltaY)
    }

    fun resize(xScale: Double, yScale: Double, centralPoint: Point) {
        val xDelta = (to.x - from.x) * xScale
        val yDelta = (to.y - from.y) * yScale

        val xStartDelta = (from.x - centralPoint.x) * xScale
        val yStartDelta = (from.y - centralPoint.y) * yScale

        from = Point(centralPoint.y + xStartDelta, centralPoint.y + yStartDelta)
        to = Point(from.x + xDelta, from.y + yDelta)
    }
}

class Element(val elementType: ElementType) {
    val lines: MutableList<Line> = arrayListOf()

    var startPosition: Point = Point.NONE
        private set

    var endPosition: Point = Point.NONE
        private set

    constructor(elementType: ElementType, line: Line) : this(elementType) {
        lines.add(line)

        val xFrom = line.from.x
        val xTo = line.to.x
        val yFrom = line.from.y
        val yTo = line.to.y
        startPosition = Point(Math.min(xFrom, xTo), Math.max(yFrom, yTo))
        endPosition = Point(Math.max(xFrom, xTo), Math.min(yFrom, yTo))
    }

    constructor(elementType: ElementType, lines: List<Line>, startPosition: Point, endPosition: Point) : this(elementType) {

        this.lines.addAll(lines)
        this.startPosition = startPosition
        this.endPosition = endPosition
    }

    val length: Double
        get() = Math.abs(endPosition.x - startPosition.x)

    val height: Double
        get() = Math.abs(endPosition.y - startPosition.y)

    fun move(xDelta: Double, yDelta: Double) {
        startPosition.move(xDelta, yDelta)
        endPosition.move(xDelta, yDelta)
        for (line in lines) {
            line.move(xDelta, yDelta)
        }
    }

    fun resize(xScale: Double, yScale: Double) {
        val deltaX = (endPosition.x - startPosition.x) * xScale
        val deltaY = (endPosition.y - startPosition.y) * yScale

        endPosition = Point(startPosition.x + deltaX, startPosition.y + deltaY)
        for (line in lines) {
            line.resize(xScale, yScale, startPosition)
        }
    }

    fun isSameType(compared: ElementType) = elementType.isSame(compared)
}
