package com.example.poo

class OuterClass {
    private val descOuter = "Classe Outer"

    inner class Inner() {
        private val descInner = "Classe Inner"

        fun printDesc() {
            println("Classe Outer: $descOuter | Classe Inner: $descInner")
        }

    }


}