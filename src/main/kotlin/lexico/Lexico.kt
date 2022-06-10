package lexico

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.sqrt

class Lexico(fileName: String) {

    private var content: CharArray? = null
    private var position = 0

    private val reserveTable = HashMap<String, Token>()
    private val symbolTable = HashMap<String, Token>()

    init {

        reserve((Token(Token.TK_LOG, "log")))

        try {
            val txtContent = String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8)
            content = txtContent.toCharArray()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun nextToken(): Token? {

        var currentChar: Char?
        var textName = ""

        if (isEndOfFile()) {
            return null
        }

        var state = 0
        while (!isEndOfFile()) {

            currentChar = getNextChar()

            when (state) {

                0 -> {
                    when {
                        isTerm(currentChar) -> {
                            state = 1
                            textName += currentChar
                        }
                        isDigit(currentChar) -> {
                            state = 3
                            textName += currentChar
                        }
                        isSpace(currentChar) -> {
                            state = 0
                        }
                        isBigger(currentChar) -> {
                            state = 5
                            textName += currentChar
                        }
                        isLess(currentChar) -> {
                            state = 16
                            textName += currentChar
                        }
                        isUnaryOperator(currentChar) -> {
                            state = 6
                            textName += currentChar
                        }
                        isEqualSimbol(currentChar) -> {
                            state = 11
                            textName += currentChar
                        }
                        isAndOperator(currentChar) -> {
                            state = 18
                            textName += currentChar
                        }
                        isOrOperator(currentChar) -> {
                            state = 19
                            textName += currentChar
                        }
                        isOpenPar(currentChar) -> {
                            state = 7
                            textName += currentChar
                        }
                        isClosePar(currentChar) -> {
                            state = 9
                            textName += currentChar
                        }
                        isPlusOperator(currentChar) -> {
                            state = 20
                            textName += currentChar
                        }
                        isSubOperator(currentChar) -> {
                            state = 21
                            textName += currentChar
                        }
                        isArithmeticOperator(currentChar) -> {
                            state = 23
                            textName += currentChar
                        }
                        else -> {
                            throw RuntimeException("Unrecognized symbol")
                        }
                    }
                }

                1 -> {
                    if (isTerm(currentChar) || isDigit(currentChar)) {
                        state = 1
                        textName += currentChar
                    } else if (isSpace(currentChar)
                        || isBigger(currentChar)
                        || isLess(currentChar)
                        || isPlusOperator(currentChar)
                        || isSubOperator(currentChar)
                        || isAndOperator(currentChar)
                        || isOrOperator(currentChar)
                        || isUnaryOperator(currentChar)
                        || isOpenPar(currentChar)
                        || isClosePar(currentChar)
                        || isArithmeticOperator(currentChar)
                        || isEqualSimbol(currentChar)
                    ) {
                        state = 2
                    } else {
                        throw RuntimeException("Unrecognized symbol")
                    }
                }

                2 -> {
                    backToPreviousChar()
                    var t: Token? = reserveTable[textName]
                    t?.let { return t }

                    t = symbolTable[textName]
                    t?.let { return t }

                    t = Token(Token.TK_IDENTIFIER, textName)
                    putInSymbolTable(t)
                    return t
                }

                3 -> {
                    if (isDigit(currentChar)) {
                        state = 3
                        textName += currentChar
                    } else if(isDot(currentChar)) {
                        state = 3
                        textName += currentChar
                    } else if (isSpace(currentChar)
                        || isBigger(currentChar)
                        || isLess(currentChar)
                        || isPlusOperator(currentChar)
                        || isSubOperator(currentChar)
                        || isAndOperator(currentChar)
                        || isOrOperator(currentChar)
                        || isUnaryOperator(currentChar)
                        || isOpenPar(currentChar)
                        || isClosePar(currentChar)
                        || isArithmeticOperator(currentChar)
                        || isEqualSimbol(currentChar)
                    ) {
                        state = 4
                    } else {
                        throw RuntimeException("Token not allowed")
                    }
                }

                4 -> {
                    backToPreviousChar()
                    return Token(
                        Token.TK_NUMBER,
                        textName
                    )
                }

                5 -> {
                    if (isEqualSimbol(currentChar)) {
                        state = 17
                        textName += currentChar
                    } else {
                        state = 15
                    }
                }

                6 -> {
                    return Token(
                        Token.TK_UNARY_OPERATOR,
                        textName
                    )
                }

                7 -> {
                    if (isOpenPar(currentChar)) {
                        state = 7
                        textName += currentChar
                    } else {
                        state = 8
                    }
                }

                8 -> {
                    backToPreviousChar()
                    return Token(
                        Token.TK_OPEN_PAR,
                        textName
                    )
                }

                9 -> {
                    if (isClosePar(currentChar)) {
                        state = 9
                        textName += currentChar
                    } else {
                        state = 10
                    }
                }

                10 -> {
                    backToPreviousChar()
                    return Token(
                        Token.TK_CLOSE_PAR,
                        textName
                    )
                }

                11 -> {
                    if(isEqualSimbol(currentChar)){
                        state = 17
                        textName += currentChar
                    } else{
                        state = 14
                    }
                    /*if (isEqualSimbol(currentChar)) {
                        state = 14
                        textName += currentChar
                    } else if (isRelationalOperator(currentChar)) {
                        state = 12
                        textName += currentChar
                    } else {
                        state = 12
                    }*/

                }

                12 -> {
                    backToPreviousChar()
                    return Token(
                        Token.TK_RELAC_OPERATOR,
                        textName
                    )
                }

                13 -> {
                    //backToPreviousChar()
                    return Token(
                        Token.TK_LOGIC_OPERATOR,
                        textName
                    )
                }

                14 -> {
                    backToPreviousChar()
                    return Token(
                        Token.TK_ASSIGNMENT,
                        textName
                    )
                }

                15 -> {
                    backToPreviousChar()
                    return Token(
                        Token.TK_RELAC_OPERATOR,
                        textName
                    )
                }

                16 -> {
                    if (isEqualSimbol(currentChar)) {
                        state = 17
                        textName += currentChar
                    } else if(isBigger(currentChar)) {
                        state = 17
                        textName += currentChar
                    } else {
                        state = 15
                    }
                }

                17 -> {
                    return Token(
                        Token.TK_RELAC_OPERATOR,
                        textName
                    )
                }

                18 -> {
                    if(isAndOperator(currentChar)){
                        state = 13
                        textName += currentChar
                    }else{
                        //LEXICAL ERROR
                    }
                }

                19 -> {
                    if(isOrOperator(currentChar)){
                        state = 13
                        textName += currentChar
                    }else{
                        state = 22
                        textName += currentChar
                    }
                }

                20 -> {
                    if(isDigit(currentChar)){
                        state = 3
                        textName += currentChar
                    }else{
                        state = 22
                        //textName += currentChar
                    }
                }

                21 -> {
                    if(isDigit(currentChar)){
                        state = 3
                        textName += currentChar
                    }else{
                        state = 22
                        //LEXICAL ERROR
                    }
                }

                22 -> {
                    backToPreviousChar()
                    return Token(
                        Token.TK_ARIT_OPERATOR,
                        textName
                    )
                }

                23 -> {
                    //backToPreviousChar()
                    return Token(
                        Token.TK_ARIT_OPERATOR,
                        textName
                    )
                }

            }

            position++
        }

        return null
    }

    //private fun isRelationalOperator(c: Char): Boolean = c == '>' || c == '<' || c == '='
    //
    //private fun isLogicOperator(c: Char): Boolean = c == '&' || c == '|'

    private fun isDigit(c: Char): Boolean = c in '0'..'9'
    private fun isTerm(c: Char): Boolean = (c in 'a'..'z') || (c in 'A'..'Z')
    private fun isSpace(c: Char): Boolean = c == ' ' || c == '\t' || c == '\n' || c == '\r'

    private fun isBigger(c: Char): Boolean = c == '>'
    private fun isLess(c: Char): Boolean = c == '<'

    private fun isPlusOperator(c: Char): Boolean = c == '+'
    private fun isSubOperator(c: Char): Boolean = c == '-'
    private fun isArithmeticOperator(c: Char): Boolean = c == '*' || c == '/' || c == '^'

    private fun isAndOperator(c: Char): Boolean = c == '&'
    private fun isOrOperator(c: Char): Boolean = c == '|'

    private fun isUnaryOperator(c: Char): Boolean = c == '¬'
    private fun isOpenPar(c: Char): Boolean = c == '('
    private fun isClosePar(c: Char): Boolean = c == ')'
    private fun isEqualSimbol(c: Char): Boolean = c == '='

    private fun isDot(c: Char): Boolean = c == '.'

    private fun getNextChar(): Char = content!![position]
    private fun isEndOfFile(): Boolean = position == content!!.size
    private fun backToPreviousChar() = position--


    private fun putInSymbolTable(t: Token) {
        symbolTable[t.getText()] = t
    }

    private fun reserve(t: Token) {
        reserveTable[t.getText()] = t
    }

}