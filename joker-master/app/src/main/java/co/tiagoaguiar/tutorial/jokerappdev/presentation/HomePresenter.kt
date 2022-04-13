package co.tiagoaguiar.tutorial.jokerappdev.presentation

import android.os.Handler
import android.os.Looper
import co.tiagoaguiar.tutorial.jokerappdev.CategoryItem
import co.tiagoaguiar.tutorial.jokerappdev.model.Category
import co.tiagoaguiar.tutorial.jokerappdev.view.HomeFragment

/*Esse ponto deveria ser passado via interface usando polimorfismo, mas para nao complicar demais
* nesse primeiro instante, o professor opitou por usar a classe concreta como construtor da HomePresenter,
* nos proximos modulos sera abordado usar nesse campo uma interface*/
class HomePresenter (private val view: HomeFragment) {

    /*LEMBRETE
    * A camada de view precisa ter uma conexao com o presenter e o presenter precisa ter uma conexao
    * com a camada de view. Isso acontece quando no HomeFragment eh instanciado a HomePresenter e colocado
    * como parametro o objeto vivo da HomeFragment 'presenter = HomePresenter(this)'*/

    //:::::::::::: METODO DE INPUT :::::::::::::
    fun findAllCategories() {
        //Aciona a progress bar
        view.showProgress()
        fakeRequest()
    }

    //:::::::::::: METODO DE OUTPUT ::::::::::::: (success, falha, complete)

    /*A funcao eh executada quando houver sucesso na requisicao para o banco de dados, os dados serao
    * formatadaos e enviados para a HomeFragment. onSuccess recebera como parametro uma lista de
    * Strings que veio do banco de dados. Sera criado uma lista de Category com as informacoes da
    * lista do response*/
    fun onSuccess (response: List<String>) {
        //Retorna uma nova lista depois que cada elemento da lista anterior foi modificado pela expressao
        // escolhida, nesse caso foi retornado uma nova lista de CategoryItem que tem como parametro o
        //cada item do Category
        var categories = response.map {Category(it, 0xFFFF0000)}

        //Essa informacao sera usada pelo adapter no HomeFragment para criar os itens da fragment
        view.showCategories(categories)
    }

    //Espera uma mensagem de erro do servidor
    fun onError (message:String) {
        //Dar um toast na homeFragment com uma mensagem de erro do servidor
        view.showFailure(message)
    }

    fun onComplete(){
        //Requisita que a view esconda a progress bar
        view.hideProgress()
    }

    //SIMULAR UM REQUISICAO HTTP
    private fun fakeRequest() {
        /*postDelay tem como parametro um bloco de codigo que sera executado de uma forma como delay
        e o segundo parametro eh o tempo em milisegundos que ele ira executar isso*/
        Handler(Looper.getMainLooper()).postDelayed({
            //Aqui sera simulado uma lista que seria devolvida pelo servidor
            val response = arrayListOf(
                "Categoria 1",
                "Categoria 2",
                "Categoria 3",
                "Categoria 4"

            )
            onSuccess(response)

            //onError("Falha da conexao, tente novamente mais tarde!")

            //Funcao chamada a fim de indicar que a requisicao ao db terminou e a progress bar ja pode
            //ser escondida
            onComplete()
        }, 2000)
    }
}

/*
* IDEIA DO MODELO MVP
* 1. CICLO DE VIDA DO FRAGMENT FAZ A ACAO (CHAMAR O PRESENTER PEDINDO PARA BUSCAR AS CATEGORIAS)
* 2. PRESENTER PEDE A LISTA DE CATEGORIAS NO MODEL
* 3. O MODEL DELVOLVE UMA List<String>
* 4. O PRESENTER FORMATA ESSA LISTA (String) EM (CATEGORY (MODEL))
* 5. VIEW PEGA A List<Category> E CONVERTE PARA List<CategoryItem>

* Obs.: O modelo que era feito em outras aulas era o MVC no qual todaos as atividades ficam na activity/
* fragment
* */