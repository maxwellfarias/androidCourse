package co.tiagoaguiar.tutorial.jokerappdev.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.tiagoaguiar.tutorial.jokerappdev.CategoryItem
import co.tiagoaguiar.tutorial.jokerappdev.R
import co.tiagoaguiar.tutorial.jokerappdev.model.Category
import com.xwray.groupie.GroupieAdapter

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    //Metodo chamado ao criar a view do fragment_home, esse metodo pegara a referencia do layout inflado
    //(fragment_home)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_main)
        /*RequireContext() eh o metodo que eh utilizado para pegar o contexto em um fragment, se fosse
        em uma activity seria utilizado o this@...*/
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        //Cria-se um adapter generico
        val adapter = GroupieAdapter()
        //Liga o adapter generico ao recyclerView
        recyclerView.adapter = adapter
        //Adiciona as informacoes para cada celula que sera inflada quando for feita uma rolagem
        adapter.add(CategoryItem(Category("Categoria 1", 0xFFFF0000)))
        adapter.add(CategoryItem(Category("Categoria 2", 0xFF64c987)))
        adapter.add(CategoryItem(Category("Categoria 3", 0xFF47bb8c)))
        adapter.add(CategoryItem(Category("Categoria 4", 0xFF29ad8f)))
        //Notifca que o elemento ja esta pronta
        adapter.notifyDataSetChanged()

    }
}