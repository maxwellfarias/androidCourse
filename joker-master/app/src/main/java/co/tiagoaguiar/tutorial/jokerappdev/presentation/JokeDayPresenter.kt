package co.tiagoaguiar.tutorial.jokerappdev.presentation

import co.tiagoaguiar.tutorial.jokerappdev.data.JokeCallback
import co.tiagoaguiar.tutorial.jokerappdev.data.JokeDayRemoteDataSource
import co.tiagoaguiar.tutorial.jokerappdev.data.JokeRemoteDataSource
import co.tiagoaguiar.tutorial.jokerappdev.model.Joke
import co.tiagoaguiar.tutorial.jokerappdev.view.PassDataToJokeDayPresenter

class JokeDayPresenter(
    var jokeDay: PassDataToJokeDayPresenter,
    val dataSource: JokeDayRemoteDataSource = JokeDayRemoteDataSource()
) : JokeCallback {
    fun findJoke() {
        jokeDay.showProgressbar()
        dataSource.findJokeDay(this)
    }

    override fun onResponse(response: Joke) {
        jokeDay.showJoke(response)
    }

    override fun onError(response: String) {
        jokeDay.showError(response)
    }

    override fun onComplete() {
        jokeDay.hideProgressbar()
    }


}