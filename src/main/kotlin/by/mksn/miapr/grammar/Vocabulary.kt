package by.mksn.miapr.grammar

interface Type {
    fun isTerminal(): Boolean
}

open class ElementType(val name: String) : Type {

    companion object {
        val NONE = ElementType("NONE")
    }

    override fun isTerminal() = false

    fun isSame(compared: ElementType) =
            this.name == compared.name
}

class TerminalElementType(name: String, private val standardLine: Line) : ElementType(name) {

    override fun isTerminal() = true

    val standardElement: Element
        get() = Element(this, Line(
                Point(standardLine.from.x, standardLine.from.y),
                Point(standardLine.to.x, standardLine.to.y)
        ))
}