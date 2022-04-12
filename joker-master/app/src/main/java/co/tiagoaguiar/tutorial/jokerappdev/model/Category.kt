package co.tiagoaguiar.tutorial.jokerappdev.model
//Esse eh o codigo logico que vai representar o layout
data class Category (
    val name: String,
    val bgColor: Long //Sera usado um padrao hexadecimal -> 0XAARRGGBB(Alpha, R, G,B)
        )
