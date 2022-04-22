package co.tiagoaguiar.tutorial.jokerappdev.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import co.tiagoaguiar.tutorial.jokerappdev.R
import co.tiagoaguiar.tutorial.jokerappdev.model.Joke
import co.tiagoaguiar.tutorial.jokerappdev.presentation.JokePresenter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso

class JokeFragment : Fragment() {

    //Cria uma constante estatica como chave
    companion object {
        const val CATEGORY_KEY = "category"
    }

    private lateinit var progressBar: ProgressBar
    private lateinit var textView: TextView
    private lateinit var imageView: ImageView

    private lateinit var presenter: JokePresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = JokePresenter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_joke, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Pega a informacao do nome da category passada pela homeFragment via bundle
        val categoryName = arguments?.getString(CATEGORY_KEY)

        categoryName?.let { categoryName ->
            /*activity?.findViewById<Toolbar> porque esse fragment compartilha da mesma toobar da mainActivit,
            O fragmento eh a parte interna e a atividade eh toda a casca do app. Eh feita a mundanca do
            titulo da barra superior para o nome da categoria*/
            activity?.findViewById<Toolbar>(R.id.toolbar)?.title = categoryName

            //Pegando referencia das views
            progressBar = view.findViewById(R.id.joke_progress_bar)
            textView = view.findViewById(R.id.txt_joke)
            imageView = view.findViewById(R.id.img_joke)
            view.findViewById<FloatingActionButton>(R.id.fab).setOnClickListener(){
                presenter.findBy(categoryName)
            }

            //Passa a informacao para o presenter sobre qual eh o nome da categoria, essa informacao sera
            //usada para fazer uma requisicao no servidor a fim de buscar as informacoes da piada.
            presenter.findBy(categoryName)
        }
    }

    //Metodo que devolve uma piada
    fun showJoke(joke: Joke) {
        textView.text = joke.text
        /*Eh usado a biblioteca Picasso para baixar a imagem de acordo com a url e atribui essa imagem
        ao imageView. Os picasso armazena em memoria as urls que foram usadas, dessa forma nao demora
        muito para carrega uma imagem que ja foi usada.*/
        Picasso.get().load(joke.iconUrl).into(imageView)
    }

    fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    fun showFailure(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

}