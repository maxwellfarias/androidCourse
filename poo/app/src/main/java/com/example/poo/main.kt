package com.example.poo

import kotlin.jvm.internal.PropertyReference0Impl


fun main() {

    val function: (Int, Int) -> Int = ::sum
    generic(10,30, function)
    generic(10,40,::sum)
    generic(19,49) {x,y ->
        x+y
    }
    var array = intArrayOf(2,35,5)

}

fun generic(x: Int, y: Int, function: (Int, Int) -> Int) {
    println(function(x, y))
}

fun sum(x: Int, y: Int) = x + y
fun dim(x: Int, y: Int) = x - y