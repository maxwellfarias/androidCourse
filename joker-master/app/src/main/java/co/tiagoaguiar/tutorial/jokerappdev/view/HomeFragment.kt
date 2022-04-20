package co.tiagoaguiar.tutorial.jokerappdev.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.tiagoaguiar.tutorial.jokerappdev.CategoryItem
import co.tiagoaguiar.tutorial.jokerappdev.R
import co.tiagoaguiar.tutorial.jokerappdev.data.CategoryRemoteDataSource
import co.tiagoaguiar.tutorial.jokerappdev.model.Category
import co.tiagoaguiar.tutorial.jokerappdev.presentation.HomePresenter
import co.tiagoaguiar.tutorial.jokerappdev.view.jokeFragment.Companion.CATEGORY_KEY
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

        /*Faz a solicitacao para que o presenter encontre as categorias. Esse metodo chama o
        dataSource.findAllCategories(this) que eh um metodo privado que faz a requisicao para o
        banco de dados -> A resposta eh passada para o onSuccess, eh feito o tratamento dos dados
        para o formato desejado e o resultado eh devolvido chamando a funcao showCategories*
        A condicao adapter.itemCount == 0 faz com que seja chamado apenas os elementos se a lista
        estiver vazia, se a lista ja tem elementos, significa que uma requisicao foi feita, nao fazendo
        dessa forma que sempre que entrar em uma categoria de piada e voltar, seja recarregado uma nova
        lista de piadas duplicando as categorias
         */
        if (adapter.itemCount == 0) {
            presenter.findAllCategories()
        }
        //Liga o adapter generico ao recyclerView
        recyclerView.adapter = adapter

        /*setOnItemClickListener cria um evento de click para cada fragment da home*/
        adapter.setOnItemClickListener { item, view ->
            //Assim como eh passado informacaos entre activies atraves dos intents, sera feito algo
            //semelhante com os fragments atraves dos bundles
            val bundle = Bundle()
            //(item as CategoryItem) faz o cast do item para CagtegoryItem e depois pega a atributo name
            val categoryName = (item as CategoryItem).category.name
            //Os parametros sao a chave e o valor (informacao que se deseja passar para a proxima tela)
            bundle.putString(CATEGORY_KEY, categoryName)
            /*Para navegar para a proxima tela usa-se uma funcao padrao do kotlin findNavController
            e colocando como parametro o Id da acao de navegacao das telas que esta no mobile navigation
            junto com o buble que contem as string que sera passado para o outro fragment*/
            findNavController().navigate(R.id.action_nav_home_to_nav_joke, bundle)
        }

    }

    fun showCategories(response: List<Category>) {
        val categories = response.map { CategoryItem(it) }
//Adiciona as informacoes para cada celula que sera inflada quando for feita uma rolagem
        adapter.addAll(categories)
//Notifca que o elemento ja esta pronto
        adapter.notifyDataSetChanged()
    }

    fun showProgress() {
//Mostra a progress bar
        progressBar.visibility = View.VISIBLE
    }

    fun hideProgress() {
//Mostra a progress bar visivel
        progressBar.visibility = View.GONE
    }

    fun showFailure(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }
}