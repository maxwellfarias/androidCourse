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
        //Cria-se a instancia do retrofit
        HTTPClient.retrofit()
            /*::class.java eh uma reflexao, ou seja, sera lido a interface ChuckNorrisAPI e sera
            criada uma classe concreta a partir dessa interface*/
            .create(ChuckNorrisAPI::class.java)
            //Medoto de retorno a List<String> definida na inteface
            .findAllCategories()
            /* 1) .enqueue enfilera, fazendo com que seja uma chamada assicrona. O metodo devolvera
            um callback com as opcoes de sucesso e falha (onResponse e onFailure).
                2) O retorno do callback tem que ser igual ao retorno do findAllCategories na interface
                 ChuckNorrisAPI */
            .enqueue(object : Callback<List<String>> {
                override fun onResponse(
                    call: Call<List<String>>,
                    response: Response<List<String>>
                ) {
                    if (response.isSuccessful) {
                        /*Se a resposta for verdadeira, categories recebera a lista de strings com as
                            categorias. O .body extrai as informacoes do response*/
                        val categories = response.body()
                        /*A lista eh opcional, pois se der uma excecao, ela sera nula. Por isso sera colocado
                        como padrao para casos nullos uma lista vazia*/
                        callback.onSuccess(categories ?: emptyList())
                    } else {
                        /*Esse erro acontece quando o proprio app faz a chamada incorreta, aparentemente
                            sao erros com codigo abaixo de 500*/
                        val error = response.errorBody()?.string()
                        /*response.errorBody() esta retornando uma mensagem um pouco estranha, entao eh preciso
                        * configurar eventualmente essa mensagem ou ver se esta faltando alguma coisa.
                        * para ver qual URL e tambem obter outras informacoes mais detalhadas atravez do log
                        * a fim de identificar esse possivel erro, sera preciso dizer para o Retrofit
                        * que eu quero fazer o log dessas mensagens para que seja vizualizado o que esta
                        * sendo chamado a fim de saber se tem alguma coisa errada ou nao; Para realizar
                        * esse ponto, sera adicionada uma nova dependencia 'okhttp3' no gradle*/
                        callback.onError(error ?: "Erro desconhecido")
                    }
                    callback.onComplete()
                }

                //Pega o erro oriundo do servidor
                override fun onFailure(call: Call<List<String>>, t: Throwable) {
                    /*Eh utilizado o operador elvis '?:' que indica que se o valor for nullo, então
                    sera usado a mensagem padrao "erro interno"*/
                    callback.onError(t.message ?: "Erro interno")
                    callback.onComplete()
                }

            })
    }
}