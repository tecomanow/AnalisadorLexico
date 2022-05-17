package lexico

class Token(
    private val type : Int,
    private val text : String
) {

    companion object {

        const val TK_IDENTIFIER     = 0
        const val TK_NUMBER         = 1
        const val TK_ARIT_OPERATOR  = 2
        const val TK_LOGIC_OPERATOR = 3
        const val TK_UNARY_OPERATOR = 4
        const val TK_RELAC_OPERATOR = 5
        const val TK_OPEN_PAR       = 6
        const val TK_CLOSE_PAR      = 7
    }

    override fun toString(): String {
        return "Token [type = $type, text = $text]"
    }

}