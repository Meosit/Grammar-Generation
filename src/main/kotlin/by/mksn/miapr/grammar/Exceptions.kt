package by.mksn.miapr.grammar

class InvalidElementException(
        message: String? = null,
        cause: Throwable? = null
) : RuntimeException() {
    override fun toString() = "Can't create grammar"
}