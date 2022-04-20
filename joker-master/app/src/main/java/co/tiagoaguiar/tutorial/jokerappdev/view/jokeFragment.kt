package co.tiagoaguiar.tutorial.jokerappdev.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import co.tiagoaguiar.tutorial.jokerappdev.R

class jokeFragment : Fragment() {

    //Cria uma constante estatica como chave
    companion object {
        const val CATEGORY_KEY = "category"
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
        //activity?.findViewById<Toolbar> porque esse fragment compartilha da mesma toobar da mainActivit,
        //O fragmento eh a parte interna e a atividade eh toda a casca do app
        activity?.findViewById<Toolbar>(R.id.toolbar)?.title = categoryName
    }
}