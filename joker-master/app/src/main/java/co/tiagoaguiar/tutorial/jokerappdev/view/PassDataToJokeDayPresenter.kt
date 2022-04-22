package co.tiagoaguiar.tutorial.jokerappdev.view

import co.tiagoaguiar.tutorial.jokerappdev.model.Joke

interface PassDataToJokeDayPresenter {
    fun showJoke(joke: Joke)
    fun hideProgressbar()
    fun showProgressbar()
    fun showError(error: String)

}