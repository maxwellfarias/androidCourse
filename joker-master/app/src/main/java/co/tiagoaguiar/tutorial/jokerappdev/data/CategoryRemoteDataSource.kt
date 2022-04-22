package co.tiagoaguiar.tutorial.jokerappdev.data

import android.os.Handler
import android.os.Looper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//Classe que realizara chamadas do servidor
class CategoryRemoteDataSource {

    //findAllCategories espera como parametro algum objeto que implemente essa interface
    fun findAllCategories(callback: ListCategoryCallback) {
        HTTPClient.retrofit()
            .create(ChuckNorrisAPI::class.java)
            .findAllCategories()
            .enqueue(object : Callback<List<String>> {
                override fun onResponse(
                    call: Call<List<String>>,
                    response: Response<List<String>>
                ) {
                    if (response.isSuccessful) {
                        val categories = response.body()
                        callback.onSuccess(categories ?: emptyList())
                    } else {
                        val error = response.errorBody()?.toString()
                        callback.onError(error?: "Erro interno")
                    }
                    callback.onComplete()
                }

                override fun onFailure(call: Call<List<String>>, t: Throwable) {
                    callback.onError(t.message?: "Erro do servidor desconhecido")
                    //?????? Se o mensage nao for nullo voce executa o let
                    callback.onComplete()
                }

            })
    }
}