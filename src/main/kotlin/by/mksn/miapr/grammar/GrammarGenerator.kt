package by.mksn.miapr.grammar

class GrammarGenerator(drawnElements: List<Element>) {

    val grammar = Grammar()
    private val rulesVocabulary = arrayListOf<Rule>()
    private var elementNumber = 1


    private fun connectElementToGrammar(elements: MutableList<Element>): ElementType {
        if (elements.size == 1) {
            return elements[0].elementType
        }
        var resultRule: RuleSearchResult? = null
        //???
        for (candidate in elements) {
            resultRule = searchRule(candidate, elements)
            if (resultRule != null) {
                break
            }
        }

        if (resultRule == null) {
            throw InvalidElementException()
        }

        val result = ElementType("O" + elementNumber++.toString())
        grammar.addElementType(result)
        grammar.addRule(resultRule.getRule(result))

        return result
    }

    private fun searchRule(candidate: Element, elements: MutableList<Element>): RuleSearchResult? {
        for (rule in rulesVocabulary) {
            if (isFirstInRule(rule, candidate, elements)) {
                elements.remove(candidate)
                val firstElementType = candidate.elementType
                val secondElementType = connectElementToGrammar(elements)
                return RuleSearchResult(rule, firstElementType, secondElementType)
            }
            if (isSecondInRule(rule, candidate, elements)) {
                elements.remove(candidate)
                val firstElementType = connectElementToGrammar(elements)
                val secondElementType = candidate.elementType
                return RuleSearchResult(rule, firstElementType, secondElementType)
            }
        }

        return null
    }

    private fun isFirstInRule(rule: Rule, candidate: Element, elements: List<Element>) =
            elements.none { isDifferentElementFirstInRule(rule, candidate, it) }

    private fun isSecondInRule(rule: Rule, candidate: Element, elements: List<Element>) =
            elements.none { isDifferentElementSecondInRule(rule, candidate, it) }

    private fun isDifferentElementFirstInRule(rule: Rule, candidate: Element, element: Element) =
            !candidate.startPosition.isSame(element.startPosition)
                    && !candidate.endPosition.isSame(element.endPosition)
                    && !rule.isRulePositionPare(candidate, element)

    private fun isDifferentElementSecondInRule(rule: Rule, candidate: Element, element: Element) =
            !candidate.startPosition.isSame(element.startPosition)
                    && !candidate.endPosition.isSame(element.endPosition)
                    && !rule.isRulePositionPare(element, candidate)

}

class RuleSearchResult(
        private val rule: Rule,
        private val firstElementType: ElementType,
        private val secondElementType: ElementType) {

    fun getRule(startElementType: ElementType) =
            rule.getInstance(startElementType, firstElementType, secondElementType)
}