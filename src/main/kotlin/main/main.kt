package main

import lexico.Lexico
import lexico.Token

fun main(args: Array<String>) {

    val lex = Lexico("assets/entrada.txt")
    var token : Token? = null

/*    do {
        val c = lex.nextChar()
        print(c)
    }while (true)*/

    do {
        token = lex.nextToken()
        token?.let {
            println(token)
        }
    } while (token != null)

}