package co.tiagoaguiar.codelab.myapplication.archives;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import co.tiagoaguiar.codelab.myapplication.R;

public class ListCalcActivity extends AppCompatActivity {

    private RecyclerView rvList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_calc);

        rvList = findViewById(R.id.recycler_view_list);
        rvList.setLayoutManager(new LinearLayoutManager(ListCalcActivity.this));
        Bundle extras = getIntent().getExtras();

        if (!extras.isEmpty()) {
            new Thread(() -> {

                List<Register> registers = SQLHelper.getInstance(this).getRegisterBy(extras.getString("type"));
                Log.d("test", "" + registers.toString());

                //Retorma para a Thread principal
                runOnUiThread(() -> {
                    ListCalcAdapter adapter = new ListCalcAdapter(registers);
                    rvList.setAdapter(adapter);
                });
            }).start();
        }
    }

    private class ListCalcAdapter extends RecyclerView.Adapter<ListCalcAdapter.ListCalcHolder> {

        private List<Register> registers;

        public ListCalcAdapter(List<Register> registers) {
            this.registers = registers;
        }


        @NonNull
        @Override
        public ListCalcHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ListCalcHolder(getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ListCalcActivity.ListCalcAdapter.ListCalcHolder holder, int position) {
            holder.bind(registers.get(position));
        }

        @Override
        public int getItemCount() {
            return registers.size();
        }

        private class ListCalcHolder extends RecyclerView.ViewHolder {
            public ListCalcHolder(@NonNull View itemView) {
                super(itemView);
            }

            public void bind(Register register) {
                String formatted = "";


                try {
                    //Cria o objeto do tipo SimpleDateFormat a fim de usa-lo para converter um text em um objeto tipo Date
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("pt", "BR"));

                    //Converte a string com a informacao da data contida no register para um formato Date
                    Date dateToFormat = sdf.parse(register.getCreatedDate());

                    //Cria o objeto do tipo SimpleDateFormat a fim de usa-lo para converter um Date em um texto com a data/hora formatada para o portugues
                    SimpleDateFormat sdfBrPattern = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", new Locale("pt", "BR"));

                    //Converte um objeto Date em um texto com a data/hora formatada para o portugues
                    formatted = sdfBrPattern.format(dateToFormat);

                    //Como tem apenas um TextView pode ser feita usando um cast para TextView a partir do Layout itemView e adiciona o texto na view
                    ((TextView) itemView).setText(getString(R.string.list_response, register.getResponse(), formatted));

                } catch (Exception e) {

                }

                    ((TextView) itemView).setOnClickListener(v -> {
                        switch (register.getType()) {
                            case "imc":
                                Intent intentImc = new Intent(ListCalcActivity.this, ImcActivity.class);
                                intentImc.putExtra("updateId", register.getId());
                                startActivity(intentImc);
                                break;
                            case "tmb":
                                Intent intentTmb = new Intent(ListCalcActivity.this, TmbActivity.class);
                                intentTmb.putExtra("updateId", register.getId());
                                startActivity(intentTmb);
                                break;
                            default:
                                throw new IllegalStateException("Unexpected value: " + register.getType());
                        }
                    });

                    ((TextView) itemView).setOnLongClickListener(v -> {

                        switch (register.getType()) {
                            case "imc":
                                onLongClickDeleteItem(register.getId(), register.getType());
                                break;
                            case "tmb":
                                onLongClickDeleteItem(register.getId(), register.getType());
                        }
                        return false;
                    });





            }

            void onClick(String type, int id) {


            }
        }

        void onLongClickDeleteItem(int id, String type) {
            AlertDialog alert = new AlertDialog.Builder(ListCalcActivity.this)
                    .setMessage("Tem certeza que quer deletar esse dado?")
                    .setPositiveButton(android.R.string.ok, (DialogInterface.OnClickListener) (dialogInterface, i) -> {

                        new Thread(()->{
                            int calcId;
                            calcId = SQLHelper.getInstance(ListCalcActivity.this).deleteItem(id);


                            runOnUiThread(() -> {
                                Intent intent = new Intent(ListCalcActivity.this, ListCalcActivity.class);
                                intent.putExtra("type", type);
                                startActivity(intent);

                                if (calcId > 0)
                                    Toast.makeText(ListCalcActivity.this, "Deletado com sucesso", Toast.LENGTH_SHORT).show();
                            });

                        }).start();
                    })
                    .setNegativeButton(android.R.string.cancel, (DialogInterface.OnClickListener) (dialogInterface, i) -> {
                    })
                    .create();
            alert.show();
        }
    }
}