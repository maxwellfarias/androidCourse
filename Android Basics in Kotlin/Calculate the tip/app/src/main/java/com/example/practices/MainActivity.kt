package com.example.practices

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.practices.databinding.MainActivityBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {

    private lateinit var btnCalc: Button
    private lateinit var editCalc: TextInputEditText
    //binding sera um objeto que fara referencia a todas as minhas view dessa tela, dessa forma nao
    //eh mais necessario usar o findViewById, tornando o servico mais pratico
    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Cria um objeto com todas as referencias das views para o binding
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btnCalc = binding.btnCalculate
        editCalc = binding.costOfServiceEditText

        /*Nao eh possivel fazer debug dentro de uma funcao labda, por isso precisei fazer o metodo
        * onClickListener sem a funcao lamdba*/
        btnCalc.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                //Torna o campo do valor do servio obrigatorio
                if(binding.costOfServiceEditText.text?.isEmpty() == true) {
                    Toast.makeText(this@MainActivity, R.string.alert_edit_text, Toast.LENGTH_LONG).show()
                    return
                }
                //Torna o campo de avaliacao do servio obrigatorio
                if (binding.radioBtnServiceRate.checkedRadioButtonId == -1) {
                    Toast.makeText(this@MainActivity, R.string.alert_radio_btn, Toast.LENGTH_LONG).show()
                    return
                }
                calculateTip ()


                //Faz com que o teclado desapareca quando for clicado no botao de calcular
                val view: View? = this@MainActivity.currentFocus
                view?.let {
                    val imm: InputMethodManager =
                        getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(editCalc.windowToken, 0)
                }
            }

        })
        /*btnCalc.setOnClickListener {
            if(binding.editTextTipTime.text.isEmpty()) {
                Toast.makeText(this, R.string.alert_edit_text, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            calculateTip ()


            //Faz com que o teclado desapareca quando for clicado no botao de calcular
            val view: View? = this.currentFocus
            view?.let {
                val imm: InputMethodManager =
                    getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(editCalc.windowToken, 0)
            }
        }
*/
    }

    private fun calculateTip() {
        val textResponse = binding.textResponse
        val costIntext = binding.costOfServiceEditText.text.toString()
        val cost = costIntext.toDouble()

        val tipPercent = when (binding.radioBtnServiceRate.checkedRadioButtonId) {
            R.id.radio_btn_amazing -> 0.2
            R.id.radio_btn_good -> 0.18
            else -> 0.15
        }

        var tip = cost * tipPercent

       if (binding.switchRoundUp.isChecked) tip =  kotlin.math.ceil(tip)

        //Converte o tip para uma string formatada em dolar
        val formattedTip = NumberFormat.getCurrencyInstance().format(tip)
        textResponse.text = getString(R.string.calc_response, formattedTip)

        textResponse.visibility = TextView.VISIBLE
    }



}