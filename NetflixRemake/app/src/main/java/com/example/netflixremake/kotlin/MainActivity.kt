package com.example.netflixremake.kotlin

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.netflixremake.R
import com.example.netflixremake.model.Category
import com.example.netflixremake.util.CategoryTask
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.category_item.view.*

class MainActivity : AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val categories = arrayListOf<Category>()
        rv_view_main.adapter = MainAdapter(categories)
        rv_view_main.layoutManager = LinearLayoutManager(this)

        val categoryTask = CategoryTask(this)
        /* O  categoryTask.setCategoryLoader() espera um objeto que implemente a funcao onResult da interface. Quando ha somente uma funcao a ser
        implementada na interface eh possivel usar a funcao lambda para implementa-la. Aparentemente eh passado um objeto anonimo que implementa o
         onResult sendo possivel na funcao lambda dizer como sera a implementacao*/
         categoryTask.setCategoryLoader() { categories ->
            categories.size
        }
        categoryTask.execute("https://tiagoaguiar.co/api/netflix/home")

    }

    private inner class MainAdapter (val categories: List<Category>) : RecyclerView.Adapter<CategoryHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
            return CategoryHolder(
                layoutInflater.inflate(R.layout.category_item,
                    parent,
                    false)
            )
        }

        override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
            val categorie = categories[position]
            holder.bind(categorie)
        }

        override fun getItemCount(): Int = categories.size //Forma aternativa do corpo dessa funcao------> '{return categories.size}'
    }


    class CategoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //RecyclerView.ViewHolder(itemView) seria como o "super" no java: 'super(itemView);'
        fun bind (category:Category) {
            //Nao eh mais necessario usar setText ou getText, o '.text' substitui essa funcao, pois todos os setters e getters estao inferidos
            itemView.text_view_title.text = category.name
        }

    }
}