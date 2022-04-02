package com.example.netflixremake.model

import com.google.gson.annotations.SerializedName

/*@SerializedName("category") SerializedName serve para que o jSOnObject seja mapeado, o seu valor
sera a chave do que se deseja no jSON e em seguida eh declarado uma variavel de acordo com o tipo
do valor que a chave faz referencia
O SerializedName foi colocado somente em dados links e listas de objetos*/
data class Categories(@SerializedName("category") val categories: List<Category>)

data class Category(@SerializedName("title") var name: String = "",
                    @SerializedName("movie") var movies: List<Movie> = arrayListOf())

data class Movie(var id: Int = 0,
                 @SerializedName("cover_url") var coverUrl: String = "",
                 var title: String = "",
                 var desc: String = "",
                 var cast: String = "")


data class MovieDetail(
    var id: Int = 0,
    @SerializedName("cover_url") var coverUrl: String = "",
    var title: String = "",
    var desc: String = "",
    var cast: String = "",
    @SerializedName("movie") val moviesSimilar: List<Movie>)
