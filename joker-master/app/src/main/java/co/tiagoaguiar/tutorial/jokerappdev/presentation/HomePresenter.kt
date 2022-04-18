package co.tiagoaguiar.tutorial.jokerappdev.presentation

import android.graphics.Color
import co.tiagoaguiar.tutorial.jokerappdev.data.CategoryRemoteDataSource
import co.tiagoaguiar.tutorial.jokerappdev.data.ListCategoryCallback
import co.tiagoaguiar.tutorial.jokerappdev.model.Category
import co.tiagoaguiar.tutorial.jokerappdev.view.HomeFragment

/*Esse ponto deveria ser passado via interface usando polimorfismo, mas para nao complicar demais
* nesse primeiro instante, o professor opitou por usar a classe concreta como construtor da HomePresenter,
* nos proximos modulos sera abordado usar nesse campo uma interface*/
class HomePresenter(
    private val view: HomeFragment,
    //Caso o segundo parametro seja omitido, sera instanciado automaticamente o objeto CategoryRemoteDataSource
    private val dataSource: CategoryRemoteDataSource = CategoryRemoteDataSource()
): ListCategoryCallback {

    /*LEMBRETE
    * A camada de view precisa ter uma conexao com o presenter e o presenter precisa ter uma conexao
    * com a camada de view. Isso acontece quando no HomeFragment eh instanciado a HomePresenter e colocado
    * como parametro o objeto vivo da HomeFragment 'presenter = HomePresenter(this)'*/

    //:::::::::::: METODO DE INPUT :::::::::::::
    fun findAllCategories() {
        //Aciona a progress bar
        view.showProgress()
        dataSource.findAllCategories(this)
    }

    //:::::::::::: METODO DE OUTPUT ::::::::::::: (success, falha, complete)

    /*A funcao eh executada quando houver sucesso na requisicao para o banco de dados, os dados serao
    * formatadaos e enviados para a HomeFragment. onSruccess recebera como parametro uma lista de
    * Strings que veio do banco de dados. Sera criado uma lista de Category com as informacoes da
    * lista do response*/
    override fun onSuccess(response: List<String>) {
        //Sera usando uma formatacao de cores
        val start = 40 //H: Matriz inicial
        val end = 190 //H: Matriz final
        val diff = (end - start)/ response.size

        val categories = response.mapIndexed { index, s ->
            val hsv = floatArrayOf(
                start + (index * diff).toFloat(), //H
                100f, //S
                100f //V
            )
            //Sera passado um array com os tres pontos que definem uma cor segundo o padrao hsv (h: matriz,
            // s: saturacao, v: valor) e convertido em uma cor.
            Category(s, Color.HSVToColor(hsv).toLong())
        }
        //Retorna uma nova lista depois que cada elemento da lista anterior foi modificado pela expressao
        // escolhida, nesse caso foi retornado uma nova lista de CategoryItem que tem como parametro o
        //cada item do Category
//        val categories = response.map { Category(it, 0xFFFF0000) }

        //Essa informacao sera usada pelo adapter no HomeFragment para criar os itens da fragment
        view.showCategories(categories)
    }

    //Espera uma mensagem de erro do servidor
    override fun onError(response: String) {
        //Dar um toast na homeFragment com uma mensagem de erro do servidor
        view.showFailure(response)
    }

    override fun onComplete() {
        //Requisita que a view esconda a progress bar
        view.hideProgress()
    }

}

/*
* IDEIA DO MODELO MVP
* 1. CICLO DE VIDA DO FRAGMENT FAZ A ACAO (CHAMAR O PRESENTER PEDINDO PARA BUSCAR AS CATEGORIAS)
* 2. PRESENTER PEDE A LISTA DE CATEGORIAS NO MODEL
* 3. O MODEL DELVOLVE UMA List<String>
* 4. O PRESENTER FORMATA ESSA LISTA (String) EM (CATEGORY (MODEL))
* 5. VIEW PEGA A List<Category> E CONVERTE PARA List<CategoryItem>

* Obs.: O modelo que era feito em outras aulas era o MVC no qual todas as atividades ficam na activity/
* fragment
* */