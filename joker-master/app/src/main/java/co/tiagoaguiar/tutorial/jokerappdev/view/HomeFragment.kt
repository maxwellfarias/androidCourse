package co.tiagoaguiar.tutorial.jokerappdev.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.tiagoaguiar.tutorial.jokerappdev.CategoryItem
import co.tiagoaguiar.tutorial.jokerappdev.R
import co.tiagoaguiar.tutorial.jokerappdev.model.Category
import co.tiagoaguiar.tutorial.jokerappdev.presentation.HomePresenter
import com.xwray.groupie.GroupieAdapter

class HomeFragment : Fragment() {

    //Cria-se um adapter generico
    private val adapter = GroupieAdapter()

    //Declarando a progressBar
    private lateinit var progressBar: ProgressBar

    //Fazendo a conexao com a camanda de apresentacao
    private lateinit var presenter: HomePresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Conecta a camada da view com a present
        presenter = HomePresenter(this)
    }

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

        //Pega a referencia da progress bar
        progressBar = view.findViewById(R.id.progress_bar)

        /*Faz a solicitacao para o presenter encontre as categorias. Esse metodo chama o fakeRequest
        que eh um metodo privado que faz a requisicao para o banco de dados -> A resposta eh passada para
        o onSuccess, eh feito o tratamento dos dados para o formato desejado e o resultado eh devolvido chamando
        a funcao showCategories*/
        presenter.findAllCategories()

        //Liga o adapter generico ao recyclerView
        recyclerView.adapter = adapter


    }

    fun showCategories(response: List<Category>) {
        val categories = response.map{CategoryItem(it)}
        //Adiciona as informacoes para cada celula que sera inflada quando for feita uma rolagem
        adapter.addAll(categories)
        //Notifca que o elemento ja esta pronto
        adapter.notifyDataSetChanged()
    }

    fun showProgress(){
        //Mostra a progress bar
        progressBar.visibility = View.VISIBLE
    }

    fun hideProgress() {
        //Mostra a progress bar visivel
        progressBar.visibility = View.GONE
    }

    fun showFailure(message: String) {
        Toast.makeText(requireContext(),message, Toast.LENGTH_LONG).show()
    }
}