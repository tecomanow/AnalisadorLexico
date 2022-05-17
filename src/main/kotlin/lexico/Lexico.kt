package lexico

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

class Lexico(fileName: String) {

    private var content: CharArray? = null
    private var position = 0

    init {
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
                        isArithmeticOperator(currentChar) -> {
                            state = 5
                            textName += currentChar
                        }
                        isUnaryOperator(currentChar) -> {
                            state = 6
                            textName += currentChar
                        }
                        isRelationalOperator(currentChar) -> {
                            state = 11
                            textName += currentChar
                        }
                        isLogicOperator(currentChar) -> {
                            state = 13
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
                        || isArithmeticOperator(currentChar)
                        || isLogicOperator(currentChar)
                        || isRelationalOperator(currentChar)
                        || isUnaryOperator(currentChar)
                        || isOpenPar(currentChar)
                        || isClosePar(currentChar)) {
                        state = 2
                    }
                }

                2 -> {
                    backToPreviousChar()
                    return Token(
                        Token.TK_IDENTIFIER,
                        textName
                    )
                }

                3 -> {
                    if (isDigit(currentChar)) {
                        state = 3
                        textName += currentChar
                    } else if (!isTerm(currentChar)) {
                        state = 4
                    } else {
                        throw RuntimeException("Unrecognized Number")
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
                    return Token(
                        Token.TK_ARIT_OPERATOR,
                        textName
                    )
                }

                6 -> {
                    return Token(
                        Token.TK_UNARY_OPERATOR,
                        textName
                    )
                }

                7 -> {
                    if (isOpenPar(currentChar)){
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
                    if (isClosePar(currentChar)){
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
                    if(isRelationalOperator(currentChar)){
                        state = 12
                        textName += currentChar
                    }else {
                        state = 12
                    }
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

            }

            position++
        }

        return null
    }

    private fun isDigit(c: Char): Boolean = c in '0'..'9'
    private fun isTerm(c: Char): Boolean = (c in 'a'..'z') || (c in 'A'..'Z')
    private fun isSpace(c: Char): Boolean = c == ' ' || c == '\t' || c == '\n' || c == '\r'
    private fun isRelationalOperator(c: Char): Boolean = c == '>' || c == '<' || c == '='
    private fun isArithmeticOperator(c: Char): Boolean = c == '+' || c == '-' || c == '*' || c == '/' || c == '^'
    private fun isLogicOperator(c: Char): Boolean = c == '&' || c == '|'
    private fun isUnaryOperator(c: Char): Boolean = c == '¬'
    private fun isOpenPar(c: Char): Boolean = c == '('
    private fun isClosePar(c: Char): Boolean = c == ')'

    private fun getNextChar(): Char {
        val c = content!![position]
        return c
    }

    private fun isEndOfFile(): Boolean = position == content!!.size

    private fun backToPreviousChar() = position--

}