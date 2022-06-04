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
        const val TK_FALSE          = 8
        const val TK_TRUE           = 9
        const val TK_FOR            = 10
        const val TK_IF             = 11
        const val TK_ELSE           = 12
        const val TK_ASSIGNMENT     = 13
        const val TK_LOG            = 14
        const val TK_ROOT           = 15
    }

    override fun toString(): String {
        return "Token [type = ${getTypeContext(type)}, text = $text]"
    }

    private fun getTypeContext(type: Int): String {
        return when (type) {

            0 -> "ID"
            1 -> "NUM"
            2 -> "OP_ARIT"
            3 -> "OP_LOGIC"
            4 -> "OP_UNARY"
            5 -> "OP_RELAC"
            6 -> "OPEN_PAR"
            7 -> "CLOSE_PAR"
            8 -> "FALSE"
            9 -> "TRUE"
            10 -> "FOR"
            11 -> "IF"
            12 -> "ELSE"
            13 -> "OP_ASSIG"
            14 -> "OP_LOG"
            15 -> "OP_ROOT"

            else -> ""
        }
    }

    fun getText() : String{
        return text
    }

}