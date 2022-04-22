package co.tiagoaguiar.tutorial.jokerappdev.view

import co.tiagoaguiar.tutorial.jokerappdev.model.Category

interface PassDataToHomePresenter {
    fun showCategories(response: List<Category>)
    fun showProgress()
    fun hideProgress()
    fun showFailure(message: String)
}