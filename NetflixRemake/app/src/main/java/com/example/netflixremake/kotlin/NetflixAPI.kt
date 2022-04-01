package com.example.netflixremake.kotlin

import com.example.netflixremake.model.Categories
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface NetflixAPI {
    @GET("home")
    fun listCategories(): Call<Categories> //Define o tipo de retorno da chamada que eh a classe Categories que contem uma lista de Category

}
//Funcao global
fun retrofit(): Retrofit =
    Retrofit.Builder()
        .baseUrl("https://tiagoaguiar.co/api/netflix/") //URL da API, mas sem a ultima parte home, esta se encontra no @GET("home")
        .addConverterFactory(GsonConverterFactory.create()) //Converte os Jsons normais pelo frameWork do proprio google
        .build()