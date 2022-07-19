package co.tiagoaguiar.fitnesstracker

import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Px
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.tiagoaguiar.fitnesstracker.model.Calc
import java.text.SimpleDateFormat
import java.util.*

class ListCalcActivity : AppCompatActivity(), OnListClickListener {


    // FIXME: adapter e result precisa estar no escopo para podermos usá-lo na hora de excluir o item
    private lateinit var adapter: ListCalcAdapter
    private lateinit var result: MutableList<Calc>
    private var isSelectMode = false
    private var selectedItems = mutableListOf<Calc>()

    private lateinit var rv: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_calc)


        result = mutableListOf<Calc>()

        // FIXME: passando o this que é Activity que implementa o OnListClickListener
        adapter = ListCalcAdapter(result, this)
        rv = findViewById(R.id.rv_list)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter


        val type =
            intent?.extras?.getString("type") ?: throw IllegalStateException("type not found")
        // buscar no banco esse tipo
        Thread {
            val app = application as App
            val dao = app.db.calcDao()
            val response = dao.getRegisterByType(type)

            runOnUiThread {
                result.addAll(response)
                adapter.notifyDataSetChanged()
                /*
                Quando um ArrayAdapter é construído, ele mantém a referência para a lista que foi
                passada. Se você passar uma lista que era membro de uma atividade e alterar esse membro
                de atividade posteriormente, o ArrayAdapter ainda está mantendo uma referência ao Lista
                original. O Adaptador não sabe que você alterou a Lista na Atividade.
                */
            }
        }.start()
    }

    // FIXME: função de callback que irá disparar o evento de click no item da lista do Adapter
    override fun onClick(adapterPosition: Int, view: View, calc: Calc) {

        if (isSelectMode) {

           selectMultItems(view, calc)

        } else {
            when(calc.type) {
                "imc" -> {
                    val intent = Intent(this, ImcActivity::class.java)
                    // FIXME: passando o ID do item que precisa ser atualizado, ou seja, na outra tela
                    // FIXME: vamos buscar o item e suas propriedades com esse ID
                    intent.putExtra("updateId", calc.id)
                    startActivity(intent)
                }
                "tmb" -> {
                    val intent = Intent(this, TmbActivity::class.java)
                    intent.putExtra("updateId", calc.id)
                    startActivity(intent)
                }
            }
            finish() //Mata a atual atividade, dessa forma, quando se clicar no botao de voltar, ira para
            //o menu princiapal, uma vez que essa atividade nao ira existir mais
        }



    }

    // FIXME: função de callback que irá disparar o evento de long-click no item da lista do Adapter
    override fun onLongClick(position: Int, calc: Calc, view: View) {
        isSelectMode = true
       selectMultItems(view, calc)



        // FIXME: pergunta se realmente quer excluir
        /*AlertDialog.Builder(this)
            .setMessage(getString(R.string.delete_message))
            .setNegativeButton(android.R.string.cancel) { dialog, which ->
            }
            .setPositiveButton(android.R.string.ok) { dialog, which ->
                Thread {
                    val app = application as App
                    val dao = app.db.calcDao()

                    // FIXME: exclui o item que foi clicado com long-click
                    val response = dao.delete(calc)

                    if (response > 0) {
                        runOnUiThread {
                            // FIXME: remove da lista e do adapter o item
                            result.removeAt(position)
                            adapter.notifyItemRemoved(position)
                        }
                    }
                }.start()

            }
            .create()
            .show()*/
    }

    private inner class ListCalcAdapter(
        private val listCalc: MutableList<Calc>,
        private val listener: OnListClickListener
    ) : RecyclerView.Adapter<ListCalcAdapter.ListCalcViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListCalcViewHolder {
            val view = layoutInflater.inflate(R.layout.list_item, parent, false)
            return ListCalcViewHolder(view)
        }

        override fun onBindViewHolder(holder: ListCalcViewHolder, position: Int) {
            val itemCurrent = listCalc[position]
            holder.bind(itemCurrent)
        }

        override fun getItemCount(): Int {
            return listCalc.size
        }

        private inner class ListCalcViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(item: Calc) {

                val textHealthStatus = itemView.findViewById<TextView>(R.id.healthy_status)
                val textData = itemView.findViewById<TextView>(R.id.text_date)
                val container = itemView as LinearLayout


                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt", "BR"))
                val data = sdf.format(item.createdDate)

                textData.setText(data)
                textHealthStatus.setText("%.2f".format(item.res))

                container.setOnClickListener() {
                    listener.onClick(adapterPosition, itemView, item)
                }

                container.setOnLongClickListener {
                    listener.onLongClick(adapterPosition, item, itemView)
                    true
                }
            }
        }

    }

    private fun converterDpToPixels (dp:Float): Int {
        val r: Resources = resources
        val px = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            r.displayMetrics
        )
        return px.toInt()
    }

    private fun selectMultItems (view: View, calc:Calc) {

            if (selectedItems.contains(calc)) {
                view.setBackgroundResource(R.drawable.bg_item_list)

                val param = view.layoutParams as ViewGroup.MarginLayoutParams
                param.marginEnd = converterDpToPixels(15f)
                view.layoutParams = param

                selectedItems.remove(calc)
                showTopBar()
            } else {

                val param = view.layoutParams as ViewGroup.MarginLayoutParams
                param.marginEnd = 0
                view.layoutParams = param


                view.setBackgroundResource(R.drawable.bg_selected_item_list)
                selectedItems.add(calc)
                showTopBar()
            }

            if (selectedItems.size == 0) isSelectMode = false
    }

    private fun showTopBar (){
        val callback = object : ActionMode.Callback {

            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                menuInflater.inflate(R.menu.contextual_action_bar, menu)
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                return false
            }

            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                return when (item?.itemId) {
                    R.id.share -> {
                        Toast.makeText( this@ListCalcActivity,"Share Selected", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.delete -> {
                        // Handle delete icon press
                        true
                    }
                    R.id.more -> {
                        // Handle more item (inside overflow menu) press
                        true
                    }
                    else -> false
                }
            }

            override fun onDestroyActionMode(mode: ActionMode?) {
            }
        }

        val actionMode = startSupportActionMode(callback)
        if (selectedItems.size == 0) actionMode?.finish() //When there are no items from the
        // selected list, ActionBar will disappear

        else actionMode?.title = "${selectedItems.size} selected"


    }

}