package co.tiagoaguiar.tutorial.jokerappdev.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//Sera uma classe que ira contruir a requisicao do retrofit em formato singleton, ou seja, eh um
// objeto que fica vivo durante toda a aplicacao
object HTTPClient {
    private const val BASE_URL = "https://api.tiagoaguiar.co/jokerapp/"
    //Chave da API fornecida por ter um email cadastrado na plataforma do curso
    const val API_KEY = "54d850df-8a8f-482f-a3c7-48eb35b70d0f"


    /*Cria o client http. Esse cliente faz com que no meio da chamada realizada para o servidor,
    esse client ira interceptar a chamada a fim de obtermos informacoes sobre ela e analisar com
     mais detalhes algum possivel problema que possa acontencer nas chamadas. Para acessar as
     informacoes, basta it em Logcat, verbose e filtrar por OkHttp*/
    private fun httpClient(): OkHttpClient {
        //Cria o interceptador
        val logging = HttpLoggingInterceptor()
        //Especifica o nivel de acesso do Log. Nesse caso sera logado o corpo da chamada que esta sendo
        //feita
        logging.level = HttpLoggingInterceptor.Level.BODY

        //Cria o client http.
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    //retorna um retrofit construido
    fun retrofit(): Retrofit = Retrofit.Builder()
        //Adicona a url que sera feita a requisicao para API
        .baseUrl(BASE_URL)
        /*Cria um conversor json. A sua finalidade é coverter a estrutura string json em um objeto
         vivo. Para esse aplicativo, sera convertido para uma string mesmo.*/
        .addConverterFactory(GsonConverterFactory.create())
        //Adiciona o client que ira interceptar a chamada a fim de obtermos informacoes sobre ela
        .client(httpClient())
        .build()
}