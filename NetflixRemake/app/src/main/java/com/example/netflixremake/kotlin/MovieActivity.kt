package com.example.netflixremake.kotlin

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.netflixremake.R
import com.example.netflixremake.model.Movie
import com.example.netflixremake.util.ImageDownloadTask
import com.example.netflixremake.util.MovieDetailTask
import kotlinx.android.synthetic.main.activity_movie.*

import kotlinx.android.synthetic.main.movie_item_similar.view.*

class MovieActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        //Descompacta o conteudo de uma variavel que aceita nullo
        intent.extras?.let {
            val id = it.getInt("id")
            val task = MovieDetailTask(this)
            task.setMovieDetailLoader {

            }
            task.execute("//tiagoaguiar.co/api/netflix/$id")
            //Nao precisou do findViewById, foi encontrado direto usando toolbar que eh o id da toolbar
            setSupportActionBar(toolbar)
            supportActionBar?.let { toolbar ->
                //Habilita o icone de voltar
                toolbar.setDisplayHomeAsUpEnabled(true)
                //Muda o icone de voltar
                toolbar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
                //Oculta o titulo
                toolbar.title = null
            }
        }
    }
    private class MovieSimilarHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind (movie: Movie)  {
            with(itemView) {
                ImageDownloadTask(image_view_cover_similar).execute(movie.coverUrl)}

        }

    }
    private inner class MovieSimilarAdapter (val movies : List<Movie>): RecyclerView.Adapter<MovieSimilarHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            MovieSimilarHolder(layoutInflater.inflate(R.layout.movie_item_similar, parent, false))


        override fun onBindViewHolder(holder: MovieSimilarHolder, position: Int) {

        }
        override fun getItemCount() = movies.size


    }
}