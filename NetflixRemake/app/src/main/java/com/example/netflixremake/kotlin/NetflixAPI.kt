package com.example.netflixremake.kotlin

import com.example.netflixremake.model.Categories
import com.example.netflixremake.model.MovieDetail
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface NetflixAPI {
    @GET("home")
    fun listCategories(): Call<Categories> //Define o tipo de retorno da chamada que eh a classe Categories que contem uma lista de Category

    @GET ("{id}")
    fun getMovieBy(@Path("id")id: Int): Call<MovieDetail>
    //getMovieBy recebera como parametro um id inteiro que sera colocado no final do link para pegar as informacoes do movieDetail


}
//Funcao global
fun retrofit(): Retrofit =
    Retrofit.Builder()
        .baseUrl("https://tiagoaguiar.co/api/netflix/") //URL da API, mas sem a ultima parte home, esta se encontra no @GET("home")
        .addConverterFactory(GsonConverterFactory.create()) //Converte os Jsons normais pelo frameWork do proprio google
        .build()