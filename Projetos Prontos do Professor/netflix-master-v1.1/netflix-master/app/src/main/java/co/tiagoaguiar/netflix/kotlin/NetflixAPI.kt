package co.tiagoaguiar.netflix.kotlin

import co.tiagoaguiar.netflix.kotlin.model.Categories
import co.tiagoaguiar.netflix.kotlin.model.MovieDetail
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


/**
 *
 * Agosto, 26 2019
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
interface NetflixAPI {

    @GET("home")
    fun listCategories(): Call<Categories>

    @GET("{id}")
    fun getMovieBy(@Path("id") id: Int): Call<MovieDetail>

}

fun retrofit() : Retrofit {
    return Retrofit.Builder()
            .baseUrl("https://tiagoaguiar.co/api/netflix/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}
