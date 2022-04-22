package co.tiagoaguiar.tutorial.jokerappdev.data

import co.tiagoaguiar.tutorial.jokerappdev.model.Joke
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JokeRemoteDataSource {
    fun findBy(categoryName: String, callback: JokeCallback) {
        HTTPClient.retrofit()
            .create(ChuckNorrisAPI::class.java)
            .findBy(categoryName)
            .enqueue(object : Callback<Joke> {
                override fun onResponse(call: Call<Joke>, response: Response<Joke>) {
                    if (response.isSuccessful) {
                        val joke = response.body()
                        //Se o Joke for nulo, sera lancada uma exception
                        callback.onResponse(joke ?: throw RuntimeException("Piada nao encontrada"))
                    } else {
                        val error = response.errorBody()?.toString()
                        callback.onError(error ?: "Erro bla bla bla")
                    }
                    callback.onComplete()

                }

                override fun onFailure(call: Call<Joke>, t: Throwable) {
                    callback.onError(t.message ?: "Error do servidor")
                    callback.onComplete()
                }

            })
    }
}