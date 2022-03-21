package com.example.netflixremake.kotlin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.netflixremake.MovieActivity
import com.example.netflixremake.R
import com.example.netflixremake.model.Category
import com.example.netflixremake.model.Movie
import com.example.netflixremake.util.CategoryTask
import com.example.netflixremake.util.ImageDownloadTask
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.category_item.view.*
import kotlinx.android.synthetic.main.movie_item.view.*

class MainActivity : AppCompatActivity()  {
    //Sera usado o lateinit para que o programa aloque um espaco na memoria para a variavel mainAdapter somente quando o metodo onCreate for
    // executado. Como a variavel foi declarada como private, nao correra o risco de acessar essa variavel sem que ela esteja viva no programa, pois
    //sempre que se estancia um objeto da MainActivity, eh executado logo em seguida o metodo onCreate e consequentemente eh atribuido o valor no MainAdapter
    //
    private lateinit var mainAdapter : MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val categories = arrayListOf<Category>()
        mainAdapter = MainAdapter(categories)
        rv_view_main.adapter = mainAdapter
        rv_view_main.layoutManager = LinearLayoutManager(this)

        val categoryTask = CategoryTask(this)
        /* O  categoryTask.setCategoryLoader() espera um objeto que implemente a funcao onResult da interface. Quando ha somente uma funcao a ser
        implementada na interface eh possivel usar a funcao lambda para implementa-la. Aparentemente eh passado um objeto anonimo que implementa o
         onResult sendo possivel na funcao lambda dizer como sera a implementacao*/
        categoryTask.setCategoryLoader() { categories ->
            mainAdapter.categories.clear()
            mainAdapter.categories.addAll(categories)
            mainAdapter.notifyDataSetChanged()
        }
        categoryTask.execute("https://tiagoaguiar.co/api/netflix/home")

    }

    private inner class MainAdapter (val categories: MutableList<Category>) : RecyclerView.Adapter<CategoryHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
            return CategoryHolder(
                layoutInflater.inflate(
                    R.layout.category_item,
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
            val category = categories[position]
            holder.bind(category)
        }

        override fun getItemCount(): Int =
            categories.size //Forma aternativa do corpo dessa funcao------> '{return categories.size}'
    }

    //Para que a CategoryHolder enxergasse a 'private inner class MainAdapter' foi necessario coloca-la como interna tambem. Classes internas
    
    private inner class CategoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //RecyclerView.ViewHolder(itemView) seria como o "super" no java: 'super(itemView);'
        fun bind (category:Category) {
            //Nao eh mais necessario usar setText ou getText, o '.text' substitui essa funcao, pois todos os setters e getters estao inferidos
            itemView.text_view_title.text = category.name
            itemView.rv_movie.adapter = MovieAdapter(category.movies)
            itemView.rv_movie.layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.HORIZONTAL, false)
        }

    }

    private class MovieHolder (itemView: View, val onClick: ((Int) -> Unit)?) : RecyclerView.ViewHolder(itemView) {
        //((Int) -> Unit)?, '?' foi colocada que aceita ou nao null, pois a interface pode ou nao ser implementada
        fun bind (movie: Movie) {
            ImageDownloadTask(itemView.image_view_cover)
                .execute(movie.coverUrl)
            itemView.image_view_cover.setOnClickListener {
                onClick?.invoke(adapterPosition) //O metodo invoca a funcao
            }
        }
    }

    private inner class MovieAdapter (val movies: List<Movie>): RecyclerView.Adapter<MovieHolder> () {
        //Sera criado uma variavel que recebe uma funcao, esta sera passado como parametro para MovieHolder
        val onClick: ((Int) -> Unit)? = { position ->
            if(movies[position].id <= 3) {
                val intent = Intent(this@MainActivity, MovieActivity::class.java) //MovieActivity::class.java eh como se fosse MovieActivity.java
                intent.putExtra("id", movies[position].id)
                startActivity(intent)
            }

        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
            return MovieHolder(layoutInflater.inflate(R.layout.movie_item, parent, false),
            onClick)
        }

        override fun onBindViewHolder(holder: MovieHolder, position: Int) {
            val movie = movies[position]
            holder.bind(movie)
        }

        override fun getItemCount(): Int = movies.size
    }

}

