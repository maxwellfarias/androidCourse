package com.example.netflixremake.kotlin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide
import com.example.netflixremake.R
import com.example.netflixremake.model.Categories
import com.example.netflixremake.model.Category
import com.example.netflixremake.model.Movie
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.category_item.view.*
import kotlinx.android.synthetic.main.movie_item.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    //Sera usado o lateinit para que o programa aloque um espaco na memoria para a variavel mainAdapter somente quando o metodo onCreate for
    // executado. Como a variavel foi declarada como private, nao correra o risco de acessar essa variavel sem que ela esteja viva no programa, pois
    //sempre que se estancia um objeto da MainActivity, eh executado logo em seguida o metodo onCreate e consequentemente eh atribuido o valor no MainAdapter
    //
    private lateinit var mainAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val categories = arrayListOf<Category>()
        mainAdapter = MainAdapter(categories)
        rv_view_main.adapter = mainAdapter
        rv_view_main.layoutManager = LinearLayoutManager(this)


         /*O  categoryTask.setCategoryLoader() espera um objeto que implemente a funcao onResult da interface. Quando ha somente uma funcao a ser
        implementada na interface eh possivel usar a funcao lambda para implementa-la. Aparentemente eh passado um objeto anonimo que implementa o
         onResult sendo possivel na funcao lambda dizer como sera a implementacao*/

        /*val categoryTask = CategoryTask(this)
        categoryTask.setCategoryLoader() { categories ->
            mainAdapter.categories.clear()
            mainAdapter.categories.addAll(categories)
            mainAdapter.notifyDataSetChanged()
        }
        categoryTask.execute("https://tiagoaguiar.co/api/netflix/home")*/

        retrofit().create(NetflixAPI::class.java)
            .listCategories()
            .enqueue(object : Callback<Categories> {
                override fun onFailure(call: Call<Categories>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<Categories>, response: Response<Categories>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            mainAdapter.categories.clear()
                            mainAdapter.categories.addAll(it.categories)
                            mainAdapter.notifyDataSetChanged()
                        }
                    }
                }

            })

    }

    private inner class MainAdapter(val categories: MutableList<Category>) :
        RecyclerView.Adapter<CategoryHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder =
            CategoryHolder(
                layoutInflater.inflate(
                    R.layout.category_item,
                    parent,
                    false
                )
            )

        //Se apenas conter uma unica linha de comando, as chaves podem ser substituidas pelo sinal '=' e em seguida colocada a linha de comando
        override fun onBindViewHolder(holder: CategoryHolder, position: Int) =
            holder.bind(categories[position])

        override fun getItemCount(): Int =
            categories.size //Forma aternativa do corpo dessa funcao------> '{return categories.size}'
    }

    //Para que a CategoryHolder enxergasse a 'private inner class MainAdapter' foi necessario coloca-la como interna tambem. Classes internas

    private inner class CategoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //RecyclerView.ViewHolder(itemView) seria como o "super" no java: 'super(itemView);'

        //with (itemView) diz para a funcao que tudo o que for acessado diretamente, sera atrelado ao itemView, assim evitaria como muito codigo
        // repetido -> itemView.rv_movie.adapter; itemView.text_view_title.text
        fun bind(category: Category) = with(itemView) {
            //Nao eh mais necessario usar setText ou getText, o '.text' substitui essa funcao, pois todos os setters e getters estao inferidos
            text_view_title.text = category.name
            rv_movie.adapter = MovieAdapter(category.movies) { movie ->
                if (movie.id > 3) {
                    Toast.makeText(
                        this@MainActivity,
                        "Funcionalidade nao implementada",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    val intent = Intent(
                        this@MainActivity,
                        MovieActivity::class.java
                    ) //MovieActivity::class.java eh como se fosse MovieActivity.java
                    //no campo de imports- > import com.example.netflixremake.R foi apagado a parte que fazer referencia a classe MovieActivity em java,
                    //deixando apenas o R, assim o programa entende que o MovieActivity::class.java faz referencia agora a classe do kotlin, pois esta no mesmo
                    //pacote
                    intent.putExtra("id", movie.id)
                    startActivity(intent)
                }
            }
            rv_movie.layoutManager =
                LinearLayoutManager(this@MainActivity, RecyclerView.HORIZONTAL, false)
        }
    }

    //O metodo onClick foi impletamentado por meio de funcao lambda:
    // rv_movie.adapter = MovieAdapter(category.movies) { movie -> ...}
    private inner class MovieHolder(itemView: View, val onClick: ((Movie) -> Unit)?) :
        RecyclerView.ViewHolder(itemView) {
        //((Int) -> Unit)?, '?' foi colocada que aceita ou nao null, pois a interface pode ou nao ser implementada
        fun bind(movie: Movie) = with(itemView) {
            /* Forma antiga para carregar imagens:
            ImageDownloadTask(image_view_cover)
                 .execute(movie.coverUrl)*/
            //Nova forma utilizando o Glide:
            Glide.with(context)
                .load(movie.coverUrl) //Url que sera carregada
                .placeholder(R.drawable.placeholer_bg) //Pode ser passado um arquivo de recursos como o drawable para usar como replace, substituindo
                //a cor preta que tinha sido passada antes
                .into(image_view_cover) //diz qual eh a imageView que sera adicionado o recurso

            image_view_cover.setOnClickListener {
                onClick?.invoke(movie) //O metodo invoca a funcao
            }
        }
    }

    private inner class MovieAdapter(
        val movies: List<Movie>,
        private val listener: ((Movie) -> Unit)?
    ) : RecyclerView.Adapter<MovieHolder>() {
        //Sera criado uma variavel que recebe uma funcao, esta sera passado como parametro para MovieHolder

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder =
            MovieHolder(
                layoutInflater.inflate(R.layout.movie_item, parent, false),
                listener
            )

        override fun onBindViewHolder(holder: MovieHolder, position: Int) =
            holder.bind(movies[position])

        override fun getItemCount(): Int = movies.size
    }


}