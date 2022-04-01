package com.example.netflixremake.model

data class Category(
    var name: String = "",
    val movies: List<Movie> = arrayListOf()
)

data class Movie(
    var id: Int = 0,
    var coverUrl: String = "",
    var title: String = "",
    var desc: String = "",
    var cast: String = ""
)

data class MovieDetail(
    val movie: Movie,
    val movieSimilar: List<Movie>
)

