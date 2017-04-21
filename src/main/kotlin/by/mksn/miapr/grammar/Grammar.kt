package by.mksn.miapr.grammar

import java.lang.Math.abs


private val HORIZONTAL_LINE = "horizontal"
private val VERTICAL_LINE = "vertical"
private val SLASH_LINE = "slash"
private val BACK_SLASH_LINE = "back_slash"

class Grammar {

    private val rules: MutableMap<String, Rule> = mutableMapOf()
    var startElementType: ElementType? = null

    companion object {
        private val dictionary: MutableMap<String, ElementType> = mutableMapOf(
                HORIZONTAL_LINE to TerminalElementType(HORIZONTAL_LINE, Line(Point(.0, .0), Point(10.0, .0))),
                VERTICAL_LINE to TerminalElementType(VERTICAL_LINE, Line(Point(.0, .0), Point(.0, 10.0))),
                SLASH_LINE to TerminalElementType(SLASH_LINE, Line(Point(.0, .0), Point(10.0, 10.0))),
                BACK_SLASH_LINE to TerminalElementType(BACK_SLASH_LINE, Line(Point(10.0, .0), Point(.0, 10.0))))

        fun getTerminalElement(line: Line): Element {
            val elementName = getTerminalElementName(line)
            val elementType = dictionary[elementName]!!
            return Element(elementType, line)
        }

        private fun getTerminalElementName(line: Line): String {
            val deltaX = line.from.x - line.to.x
            val deltaY = line.from.y - line.to.y
            if (abs(deltaY) < 1) {
                return HORIZONTAL_LINE
            }
            if (abs(deltaX) < 1) {
                return VERTICAL_LINE
            }

            if (abs(deltaX / deltaY) < 0.2) {
                return VERTICAL_LINE
            }

            if (abs(deltaY / deltaX) < 0.2) {
                return HORIZONTAL_LINE
            }
            val highPoint = if (line.to.y > line.from.y) line.to else line.from
            val lowPoint = if (line.to.y < line.from.y) line.to else line.from
            if (highPoint.x < lowPoint.x) {
                return BACK_SLASH_LINE
            }
            return SLASH_LINE
        }

    }

    fun generateElement()
            = generateElement(startElementType!!)

    private fun generateElement(elementType: ElementType): Element {
        if (elementType.isTerminal()) {
            return (elementType as TerminalElementType).standardElement
        }
        val rule = rules[elementType.name]!!
        return rule.transformConnect(
                generateElement(rule.firstElementType),
                generateElement(rule.secondElementType)
        )
    }

    fun addElementType(elementType: ElementType) {
        dictionary.put(elementType.name, elementType)
    }

    fun addRule(rule: Rule) {
        rules.put(rule.startElementType.name, rule)
    }

    fun imageIsCorrect(elements: List<Element>): Boolean {
        val correctLines = generateElement().lines
        val correctElements = correctLines.map { getTerminalElement(it) }
        if (correctElements.size != elements.size) {
            return false
        }
        return correctElements.indices.any { elements[it].isSameType(correctElements[it].elementType) }
    }

    override fun toString(): String {
        val result = StringBuilder()
        rules.values.forEach({
            result.append("${it.startElementType.name} -> " +
                    "${it.name}(${it.firstElementType.name}, " +
                    "${it.secondElementType.name}); ")
        })
        return result.toString()
    }
}