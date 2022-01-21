package co.tiagoaguiar.codelab.myapplication.archives;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import co.tiagoaguiar.codelab.myapplication.R;

public class TmbActivity extends AppCompatActivity {
    private EditText editHeight;
    private EditText editWeight;
    private EditText editAge;
    private Spinner spinner;
    private Button btnTmbSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tbm);

        editHeight = findViewById(R.id.edit_tmb_height);
        editWeight = findViewById(R.id.edit_tmb_weight);
        editAge = findViewById(R.id.edit_tmb_age);
        spinner = findViewById(R.id.spinner_tmb_lifestyle);
        btnTmbSend = findViewById(R.id.btn_tmb_send);

        btnTmbSend.setOnClickListener((v) -> {
            if (validation()) {
                Toast.makeText(TmbActivity.this, R.string.fields_message, Toast.LENGTH_SHORT).show();
                return;
            }

            String sHeight = editHeight.getText().toString();
            String sWeight = editWeight.getText().toString();
            String sAge = editAge.getText().toString();

            int height = Integer.parseInt(sHeight);
            int weight = Integer.parseInt(sWeight);
            int age = Integer.parseInt(sAge);

            double result = calculateTmb(height, weight, age);
            double tmb = tmbResponse(result);


            AlertDialog dialog = new AlertDialog.Builder(TmbActivity.this)
                    .setMessage(getString(R.string.tmb_response, tmb))
                    .setNegativeButton(android.R.string.ok, (dialogInterface, i) -> dialogInterface.dismiss())
                    .setPositiveButton(R.string.save, (dialogInterface, i) -> {

                        new Thread(() -> {
                            long calcId;
                            int updateId = 0;

                            //Verifica se o objeto que deve conter os valores para o update eh diferente de null, se sim, eh pego valor updateId passado
                            //No ListCalcActivity.class
                            if (getIntent().getExtras() != null) {
                                updateId = getIntent().getIntExtra("updateId", 0);
                            }

                            //verifica se eh update ou create
                            if (updateId > 0) {
                                calcId = SQLHelper.getInstance(TmbActivity.this).updateItem("tmb", tmb, updateId);
                            } else {
                                //Adicionando o item
                                calcId = SQLHelper.getInstance(TmbActivity.this).addItem("tmb", result);
                            }
                            runOnUiThread(() -> {
                                if (calcId > 0) {
                                    Toast.makeText(TmbActivity.this, R.string.calc_saved, Toast.LENGTH_SHORT).show();
                                    openTmbActivity();
                                }
                            });
                        }).start();
                    })
                    .create();
            dialog.show();

            View view = this.getCurrentFocus();
            if (view != null) {

                //Esconde o teclado quando clica no botao calcular
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editAge.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(editWeight.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(editHeight.getWindowToken(), 0);
            }




        });


    }

    private double tmbResponse(double result) {
        int index = spinner.getSelectedItemPosition();

        switch (index) {
            case 0:
                return result * 1.2;
            case 1:
                return result * 1.375;
            case 2:
                return result * 1.55;
            case 3:
                return result * 1.725;
            case 4:
                return result * 1.9;
            default:
                return 0;
        }
    }

    private double calculateTmb(int height, int weight, int age) {
        return 66 + (weight * 13.8) + (height * 5) + (age * 6.8);
    }

    public void openTmbActivity() {
        Intent intent = new Intent(TmbActivity.this, ListCalcActivity.class);
        intent.putExtra("type", "tmb");
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_id:
                openTmbActivity();
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private boolean validation() {
        // Poderia ser feito dois returns, um com if e outro com else, mas somente um return com uma condicao resume isso retornando ou true ou false
        return (editHeight.getText().toString().isEmpty()
                || editHeight.getText().toString().startsWith("0")
                || editWeight.getText().toString().isEmpty()
                || editWeight.getText().toString().startsWith("0"))
                || editAge.getText().toString().isEmpty()
                || editAge.getText().toString().startsWith("0");
    }

}