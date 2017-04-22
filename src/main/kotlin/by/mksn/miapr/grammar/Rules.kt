package by.mksn.miapr.grammar

import java.util.*

private val RANDOM_DELTA = 3
private val RANDOM = Random()
fun randomDouble(min: Double, max: Double) =
        min + (max - min) * RANDOM.nextDouble()

abstract class Rule internal constructor(
        val startElementType: ElementType,
        val firstElementType: ElementType,
        val secondElementType: ElementType
) {

    abstract fun connect(first: Element, second: Element): Element

    abstract fun transformConnect(first: Element, second: Element): Element

    abstract fun isRulePositionPare(first: Element, second: Element): Boolean

    abstract fun getInstance(
            startElementType: ElementType,
            firstElementType: ElementType,
            secondElementType: ElementType): Rule

    abstract val name: String
}

class UpRule(
        startElementType: ElementType,
        firstElementType: ElementType,
        secondElementType: ElementType
) : Rule(startElementType, firstElementType, secondElementType) {

    companion object {
        val NONE = UpRule(ElementType.NONE, ElementType.NONE, ElementType.NONE);
    }

    override fun connect(first: Element, second: Element): Element {
        val resultLines = first.lines
        resultLines.addAll(second.lines)
        return Element(startElementType, resultLines, first.startPosition, second.endPosition)
    }

    override fun transformConnect(first: Element, second: Element): Element {
        makeSameLength(first, second)
        first.move(0.0, second.startPosition.y/* + randomDouble(0.0, 2.0)*/)
        return connect(first, second)
    }


    override fun isRulePositionPare(first: Element, second: Element) =
            second.startPosition.y - RANDOM_DELTA < first.endPosition.y

    override fun getInstance(startElementType: ElementType, firstElementType: ElementType, secondElementType: ElementType) =
            UpRule(startElementType, firstElementType, secondElementType)

    override val name = "Up"

    private fun makeSameLength(first: Element, second: Element) {
        val longestElement = getLongestElement(first, second)
        val shortestElement = getShortestElement(first, second)
        shortestElement.resize(longestElement.length / shortestElement.length, 1.0)
    }

    private fun getLongestElement(first: Element, second: Element) =
            if (first.length > second.length) first else second

    private fun getShortestElement(first: Element, second: Element) =
            if (first.length < second.length) first else second
}

class LeftRule(startElementType: ElementType, firstElementType: ElementType, secondElementType: ElementType) : Rule(startElementType, firstElementType, secondElementType) {

    companion object {
        val NONE = LeftRule(ElementType.NONE, ElementType.NONE, ElementType.NONE);
    }

    override fun connect(first: Element, second: Element): Element {
        val resultLines = first.lines
        resultLines.addAll(second.lines)
        val startY = Math.max(first.startPosition.y, second.startPosition.y)
        val endY = Math.min(first.endPosition.y, second.endPosition.y)
        val startPosition = Point(first.startPosition.x, startY)
        val endPosition = Point(second.endPosition.x, endY)
        return Element(startElementType, resultLines, startPosition, endPosition)
    }

    override fun transformConnect(first: Element, second: Element): Element {
        second.move(first.length/* + randomDouble(1.0, 5.0)*/, 0.0)
        return connect(first, second)
    }

    override fun isRulePositionPare(first: Element, second: Element) =
            first.endPosition.x - RANDOM_DELTA < second.startPosition.x

    override fun getInstance(startElementType: ElementType, firstElementType: ElementType, secondElementType: ElementType) =
            LeftRule(startElementType, firstElementType, secondElementType)

    override val name = "Left"
}

