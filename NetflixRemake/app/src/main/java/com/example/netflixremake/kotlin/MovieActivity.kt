package com.example.netflixremake.kotlin

import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.target.Target
import com.example.netflixremake.R
import com.example.netflixremake.model.Movie
import com.example.netflixremake.util.ImageDownloadTask
import com.example.netflixremake.util.MovieDetailTask
import kotlinx.android.synthetic.main.activity_movie.*
import kotlinx.android.synthetic.main.category_item.*

import kotlinx.android.synthetic.main.movie_item_similar.view.*

class MovieActivity : AppCompatActivity() {
    private lateinit var movieSimilarAdapter: MovieSimilarAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)


        //Descompacta o conteudo de uma variavel que aceita nullo
        intent.extras?.let {
            val id = it.getInt("id")
            val task = MovieDetailTask(this)
            task.setMovieDetailLoader { movieDetail ->
                text_view_title_cover.text = movieDetail.title
                text_view_cast.text = getString(R.string.cast, movieDetail.cast)
                text_view_desc.text = movieDetail.desc

                val movies = arrayListOf<Movie>()
                movieSimilarAdapter = MovieSimilarAdapter(movies)
                rv_similar.adapter = movieSimilarAdapter
                rv_similar.layoutManager = GridLayoutManager(this, 3)

                /*
                Forma anterior de se carregar uma imagem:
                O metodo apply faz com que possa ser chamado os metodos do imageDownloadTask sem a necessidade de instanciar o ImageDownloadTask
                ImageDownloadTask(image_view_cover_play).apply {
                    setShadowEnabled(true)
                    execute(movieDetail.movie.coverUrl)
                }*/

                //Adicionando a imagem com sombra ao fundo
                Glide.with(this)
                    .load(movieDetail.coverUrl)
                    //Serve para ouvir eventos quando estiver preparado para fazer insert do bitmap e tambem para ouvir alguma falha
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            return true //foi colocado true, porque nao sera renderizado nada
                        }

                        //Pegua o recurso do bitmap e transforma em um drawable
                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            //Cria um drawable com as mesmas propriedades do shadows, o 'as LayerDrawable?' eh a forma que é feita o cast para LayerDrawable
                            val drawable: LayerDrawable? = ContextCompat.getDrawable(baseContext, R.drawable.shadows) as LayerDrawable?
                            drawable?.let {
                                //Muda para a imagem que foi carregada atraves do link (resource faz referencia para a imagem carregada).
                                drawable.setDrawableByLayerId(R.id.cover_drawable, resource)
                                //target faz referencia a imageView passada no '.into()' e esta eh convertida para DrawableImageViewTarget
                                // em seguida eh settado o drawable que foi manipulado nas linha acima
                                (target as DrawableImageViewTarget).view.setImageDrawable(drawable)
                            }
                            return true
                        }

                    })
                    .into(image_view_cover_play)

                movieSimilarAdapter.movies.clear()
                movieSimilarAdapter.movies.addAll(movieDetail.moviesSimilar)
                movieSimilarAdapter.notifyDataSetChanged()

            }
            task.execute("https://tiagoaguiar.co/api/netflix/$id")
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private class MovieSimilarHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Movie) {
            with(itemView) {
                /*ImageDownloadTask(image_view_cover_similar).execute(movie.coverUrl)*/
                Glide.with(this)
                    .load(movie.coverUrl)
                    //Modifica o fundo do aquivo 'R.drawable.placeholer_bg', este esta sendo usando como
                    //imagem de fundo pelo ImageView do movie_item. Ou seja, modificando a sua imagem, modificara
                    //tambem o fundo do imageView
                    .placeholder(R.drawable.placeholer_bg)
                    .into(image_view_cover_similar)
            }

        }

    }

    private inner class MovieSimilarAdapter(val movies: MutableList<Movie>) :
        RecyclerView.Adapter<MovieSimilarHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            MovieSimilarHolder(layoutInflater.inflate(R.layout.movie_item_similar, parent, false))


        override fun onBindViewHolder(holder: MovieSimilarHolder, position: Int) =
            holder.bind(movies[position])

        override fun getItemCount() = movies.size
    }
}