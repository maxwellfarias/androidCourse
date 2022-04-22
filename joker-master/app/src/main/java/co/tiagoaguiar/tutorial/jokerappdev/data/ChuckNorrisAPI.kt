package co.tiagoaguiar.tutorial.jokerappdev.data

import co.tiagoaguiar.tutorial.jokerappdev.model.Joke
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

//Por baixo dos panos, o retrofit ira implementar essa interface, ela tem como funcao completar a URL
//que foi declarada ao contruir o objeto Retrofit 'private const val BASE_URL = "https://api.tiagoaguiar.co/jokerapp/"'
interface ChuckNorrisAPI {
    /* 1) Por baixo dos panos o retrofit ira ler a interface e gerara o codigo logico para que seja
     retornado uma lista de string. Será feita também a juncao do converter
     2) Eh usado o @GET para que se saiba o caminho, adicionando o conjunto que esta faltando, cada GET
     se liga a funcao que vem depois, ficando a URL completa da seguinte forma:
     https://api.tiagoaguiar.co/jokerapp/ + @GET(...) + informacoes do parametro da funcao abaixo do GET
     */
    @GET("jokes/categories")
    //@Query("apiKey") adiciona o parametro '?apiKey='
    //apiKey: String eh a variavel que recebera a chave de acesso para a requisicao
    fun findAllCategories(@Query("apiKey") apiKey: String = HTTPClient.API_KEY ): Call<List<String>>

    @GET("jokes/random")
    fun findBy(@Query("category") categoryName: String, @Query("apiKey") apiKey: String = HTTPClient.API_KEY): Call<Joke>
}













