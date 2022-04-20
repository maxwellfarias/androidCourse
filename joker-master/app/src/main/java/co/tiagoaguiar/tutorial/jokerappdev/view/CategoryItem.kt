package co.tiagoaguiar.tutorial.jokerappdev

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import co.tiagoaguiar.tutorial.jokerappdev.R
import co.tiagoaguiar.tutorial.jokerappdev.model.Category
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

//Representa o item da celula especifica. Sera vinculado o modelo de dados com o layout estatico
class CategoryItem (val category: Category): Item<CategoryItem.CategoryViewHolder>() {
    //A view faz referencia ao item_category.xml (Linear Layout) especificado em 'override fun getLayout() = R.layout.item_category'
    class CategoryViewHolder(view: View): GroupieViewHolder(view)
    //Retorna um objeto vivo categoryViewHolder. ItemView aparentemente eh obtido como o retornodo getLayout() = R.layout.item_category
    override fun createViewHolder(itemView: View): CategoryViewHolder =  CategoryViewHolder(itemView)

    override fun bind(viewHolder: CategoryViewHolder, position: Int) {
        //Acessa o textView e modifica o nome da categoria
        viewHolder.itemView.findViewById<TextView>(R.id.txt_category).text = category.name
        //O kotlin nao aceita fazer a conversao direto no inteiro, eh preciso fazer colocar um long
        // no parametro e depois converter para o inteiro
        viewHolder.itemView.findViewById<LinearLayout>(R.id.container_category).setBackgroundColor(category.bgColor.toInt())
    }

    //Associa e Retorna o layout especifico da minha celula
    override fun getLayout() = R.layout.item_category

}