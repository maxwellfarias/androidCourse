package com.example.poo

class Car {

    var color: String
        get() = "Returning color $color"
        set(value) {
            color = "New color "+value
        }
}