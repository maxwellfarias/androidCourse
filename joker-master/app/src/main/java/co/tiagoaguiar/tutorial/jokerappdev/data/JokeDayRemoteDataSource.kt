package co.tiagoaguiar.tutorial.jokerappdev.data

import co.tiagoaguiar.tutorial.jokerappdev.model.Joke
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException
import java.util.concurrent.ExecutionException

class JokeDayRemoteDataSource {
    fun findJokeDay (callback: JokeCallback) {
        HTTPClient.retrofit()
            .create(ChuckNorrisAPI::class.java)
            .findRandom()
            .enqueue(object: Callback<Joke>{
                override fun onResponse(call: Call<Joke>, response: Response<Joke>) {
                    if (response.isSuccessful) {
                        callback.onResponse(response.body()?: throw RuntimeException ("Erro no onResonse"))
                    } else {
                        val error = response.errorBody()?.toString()
                        callback.onError(error?: "Erro interno")
                    }
                    callback.onComplete()
                }

                override fun onFailure(call: Call<Joke>, t: Throwable) {
                    callback.onError(t?.message?:"Erro no servidor" )
                    callback.onComplete()
                }

            })
    }
}