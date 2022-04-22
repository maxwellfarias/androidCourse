package co.tiagoaguiar.tutorial.jokerappdev.view

import android.os.Bundle
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
import co.tiagoaguiar.tutorial.jokerappdev.presentation.JokeDayPresenter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso

class JokeDayFragment : Fragment(), PassDataToJokeDayPresenter {
    private lateinit var presenter: JokeDayPresenter
    private lateinit var progressBar: ProgressBar
    private lateinit var imageView: ImageView
    private lateinit var textView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = JokeDayPresenter(this)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_joke_day, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //activity?.findViewById<Toolbar>(R.id.toolbar)?.title = getString(R.string.menu_joke_day)
        imageView = view.findViewById(R.id.img_joke_day)
        textView = view.findViewById(R.id.txt_joke_day)
        progressBar = view.findViewById(R.id.joke_day_progress_bar)

        presenter.findJoke()

        view.findViewById<FloatingActionButton>(R.id.fab_joke_day).setOnClickListener(){
            presenter.findJoke()
        }
    }

    override fun showJoke(joke: Joke) {
        Picasso.get().load(joke.iconUrl).into(imageView)
        textView.text = joke.text
    }

    override fun hideProgressbar() {
       progressBar.visibility = View.GONE
    }

    override fun showProgressbar() {
        progressBar.visibility = View.VISIBLE
    }

    override fun showError(error: String) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
    }
}