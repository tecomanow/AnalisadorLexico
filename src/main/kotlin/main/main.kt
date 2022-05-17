package main

import lexico.Lexico
import lexico.Token

fun main() {

    val lex = Lexico("assets/entrada.txt")
    var token: Token?

    do {
        token = lex.nextToken()
        token?.let {
            println(token)
        }
    } while (token != null)

}