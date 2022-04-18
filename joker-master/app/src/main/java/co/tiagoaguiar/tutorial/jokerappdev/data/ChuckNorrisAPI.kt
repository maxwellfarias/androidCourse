package co.tiagoaguiar.tutorial.jokerappdev.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

//Por baixo dos panos, o retrofit ira implementar essa interface
interface ChuckNorrisAPI {
    /* 1) Por baixo dos panos o retrofit ira ler a interface e gerara o codigo logico para que seja
     retornado uma lista de string. Será feita também a juncao do converter
     2) Eh usado o @GET para que se saiba o caminho, adicionando o conjunto que esta faltando
     */
    @GET("jokes/categories")
    //apiKey: String eh a variavel que recebera a chave de acesso para a requisicao
    //@Query("apiKey") adiciona o parametro '?apiKey='
    fun findAllCategories(@Query("apiKey") apiKey: String = HTTPClient.API_KEY): Call<List<String>>
}