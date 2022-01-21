package co.tiagoaguiar.codelab.myapplication.archives;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import co.tiagoaguiar.codelab.myapplication.R;

public class ImcActivity extends AppCompatActivity {

    private EditText editHeight;
    private EditText editWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imc);

        editHeight = findViewById(R.id.edit_imc_height);
        editWeight = findViewById(R.id.edit_imc_weight);
        Button btnClickCalc = findViewById(R.id.btn_imc_send);

        btnClickCalc.setOnClickListener(v -> {
            //Procure sempre iniciar pela falha sendo tratada de modo inicial
            if (validation()) {
                Toast.makeText(ImcActivity.this, R.string.fields_message,Toast.LENGTH_SHORT).show();
                return;
            }

            String sHeight = editHeight.getText().toString();
            String sWeight =  editWeight.getText().toString();

            int height = Integer.parseInt(sHeight);
            int weight = Integer.parseInt(sWeight);

            double imc = calculateImc (height, weight);

            int imcResponseId = responseImc(imc);


            // MOSNTRANDO UM ALERTA NA TELA
            //Sempre de preferencia para usar coisas que estao dentro do androidX.appCompac
            AlertDialog dialog = new AlertDialog.Builder(ImcActivity.this)
                    // Abaixo declara-se a string e a variavel no getString(referencia da string, variavel), a string precisa estar pronta para receber uma variavel e faz-se isso adicionando
                    // o seguinte codigo:   <string name="imc_response">Imc: %1$.2f</string>      %1$.2F (Eh a forma de dizer que o android que queremos
                    //passar um valor com duas casas decimais )
                    .setTitle(getString(R.string.imc_response, imc))
                    .setMessage(imcResponseId)

                    //REVISAAAAAAAR!!!!: Parametros eh um texto e um objeto
                    .setPositiveButton(android.R.string.ok, (dialog1, which) -> {})
                    /*   A formato acima esta na formatacao labda
                   .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })*/

                    .setNegativeButton(R.string.save, (dialog12, which) -> {
                        //Sera feito uma fazer esse procedimento em uma thread separada (diferente da thread principal chamada de Thread da interface grafica)
                        //Caso isso nao ocorra, o aplicativo ficara com a tela congelada ate o procedimento ser concluido, prejudicando dessa forma a experiencia
                        //do usuario.


                        new Thread(() -> {

                            int updateId = 0;
                            //Verifica se o objeto que deve conter os valores para o update eh diferente de null, se sim, eh pego valor updateId passado
                            //No ListCalcActivity.class
                            if (getIntent().getExtras() != null) {
                                updateId = getIntent().getIntExtra("updateId", 0);

                            }
                            long calcId;

                            //verifica se eh update ou create
                            if (updateId > 0) {
                                calcId =  SQLHelper.getInstance(ImcActivity.this).updateItem("imc", imc, updateId);

                            } else {
                                calcId = SQLHelper.getInstance(ImcActivity.this).addItem("imc", imc);
                            }

                            //Volta a executar a Thread principal

                            runOnUiThread(() -> {
                                if (calcId > 0) {Toast.makeText(ImcActivity.this, R.string.calc_saved, Toast.LENGTH_SHORT).show();
                                    openCalcActivity();}
                            });
                        }).start();


                    })
                    //cria
                    .create();
            //mostra
            dialog.show();


            //ESCONDENDO O TECLADO

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editHeight.getWindowToken(),0);
            imm.hideSoftInputFromWindow(editWeight.getWindowToken(), 0);

        });

    }

    //Inflando o menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //para inflar o menu eh necessario informar o local do layout e o segundo parametro eh o menu que se quer inflar (parametro do onCreateOptionsMenu)
        getMenuInflater().inflate(R.menu.menu,menu);
       return true;
    }

    //Metodo para escutar eventos de clicks em menus
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Se tiver mais de um botao no menu, pode ser feito switch - case, caso contrario faz somente um if

        switch (item.getItemId()) {
            case R.id.menu_id:
                openCalcActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }





    }

    private void openCalcActivity() {
        //Abrindo uma segunda atividade que ira listar as informacoes que o usuario tem registrado
        Intent intent = new Intent(ImcActivity.this, ListCalcActivity.class);

        //Passando dados para uma segunda tela atraves de um metodo que tem como parametros CHAVE e VALOR
        intent.putExtra("type", "imc");
        startActivity(intent);
    }

    @StringRes
    // Usando annotations: O retorno dessa funcao precisa ser obrigatoriamente um arquivo de string da pasta de resource.
    private int responseImc (double imc) {
        if (imc < 15)
            return R.string.imc_severely_low_weight;
        else if (imc < 16)
            return R.string.imc_very_low_weight;
        else if (imc < 18.5)
            return R.string.imc_low_weight;
        else if (imc < 25)
        return R.string.normal;
        else if (imc < 30 )
            return  R.string.imc_high_weight;
        else if (imc < 35)
            return R.string.imc_severely_high_weight;
        else if (imc < 40)
            return R.string.imc_extreme_weight;

        else
            return R.string.imc_extreme_weight;
    }

    private double calculateImc (int height, int weight) {
        return weight / ((double) height/100 * height/100);
    }

    private boolean validation () {
        // Poderia ser feito dois returns, um com if e outro com else, mas somente um return com uma condicao resume isso retornando ou true ou false
        return  (editHeight.getText().toString().isEmpty()
                || editHeight.getText().toString().startsWith("0")
                || editWeight.getText().toString().isEmpty()
                || editWeight.getText().toString().startsWith("0"));
    }
}