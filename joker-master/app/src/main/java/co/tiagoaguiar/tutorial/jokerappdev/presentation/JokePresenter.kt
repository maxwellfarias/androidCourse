package co.tiagoaguiar.tutorial.jokerappdev.presentation

import co.tiagoaguiar.tutorial.jokerappdev.data.JokeCallback
import co.tiagoaguiar.tutorial.jokerappdev.data.JokeRemoteDataSource
import co.tiagoaguiar.tutorial.jokerappdev.model.Joke
import co.tiagoaguiar.tutorial.jokerappdev.view.JokeFragment

class JokePresenter (
    private val view : JokeFragment,
    private val dataSource: JokeRemoteDataSource = JokeRemoteDataSource()
        ): JokeCallback {

    fun findBy(categoryName: String) {
        view.showProgress()
        //Liga o JokePresenter a classe que faz a requisicao para o db
        dataSource.findBy(categoryName, this)
    }
    override fun onResponse(response: Joke) {
        view.showJoke(response)
    }

    override fun onError(response: String) {
        view.showFailure(response)
    }

    override fun onComplete() {
        view.hideProgress()
    }

}