package com.example.beberagua;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btnNotify;
    private EditText editTextInterval;
    private TimePicker timePicker;
    private int hour, minute, interval;
    private Boolean activated;

    //Local database for storing small amounts of data
    private SharedPreferences preferences;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnNotify = findViewById(R.id.btn_notify);
        editTextInterval = findViewById(R.id.txt_edit_editMinutes);
        timePicker = findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);
        preferences = getSharedPreferences("db", Context.MODE_PRIVATE);
        activated = preferences.getBoolean("activated", false);

        if (activated) {
            btnNotify.setBackgroundResource(R.drawable.bg_button_background);
            btnNotify.setText(R.string.pause);

            hour = preferences.getInt("hour", timePicker.getCurrentHour());
            timePicker.setCurrentHour(hour);
            minute = preferences.getInt("minute", timePicker.getCurrentMinute());
            timePicker.setCurrentMinute(minute);
            interval = preferences.getInt("interval", 0);
            editTextInterval.setText(String.valueOf(interval));

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void clickNotify(View view) {
        String sInterval = editTextInterval.getText().toString();
        if (sInterval.isEmpty()) {
            Toast.makeText(this, R.string.msg_error, Toast.LENGTH_LONG).show();
            return;
        }

        interval = Integer.parseInt(sInterval);
        hour = timePicker.getCurrentHour();
        minute = timePicker.getCurrentMinute();

        if (!activated) {
            btnNotify.setBackgroundResource(R.drawable.bg_button_background);
            btnNotify.setText(R.string.pause);
            activated = true;

            SharedPreferences.Editor editor = preferences.edit();

            editor.putBoolean("activated", activated);
            editor.putInt("interval", interval);
            editor.putInt("hour", hour);
            editor.putInt("minute", minute);

            //Confirm changes in db
            editor.apply();



                //REGISTRANDO O PRIMEIRO ALARME


            //Criando a intencao
            Intent notificationIntent = new Intent(MainActivity.this, NotificationPublisher.class);
            //Adicionando as informacoes que serao mandadas para a NotificationPublisher
            notificationIntent.putExtra(NotificationPublisher.KEY_NOTIFICATION_ID, 1);
            notificationIntent.putExtra(NotificationPublisher.KEY_NOTIFICATION, "Hora de beber água");
            //Criando o broadcast (que eh uma intencao pendente). Os broadcasts sao recursos de transmissao de dados em background-> context, requisicao
            // ID (quem eh quem esta chamando), intencao (nesse caso uma intencao de notification), flag
            // com um metodo de atualizacao, nesse caso foi colocado para ele atualizar caso seja chamado novamente
            PendingIntent broadcast = PendingIntent.getBroadcast(MainActivity.this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


            //Definindo o horario que sera chamado -> Pega o tempo atual mais o tempo em segundos que eh digitado na tela (o interval foi convertido de
            // segundos para milisegundos)
            long futureInMillis = SystemClock.elapsedRealtime() + (interval * 1000);
            //Pegando o alarme para manipula-lo
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            //Passando as configuracoes do alarme, nesse caso sera o tipo de tempo que ira despertar no celular com o tempo passado(ELAPSED_REALTIME_WAKEUP).
            //, tempo no futuro em milisegundos
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, broadcast);

        } else {
            //Se em outra parte do codigo for mudado a cor desse mesmo botao, sera conservado o drawble, e sera mudado somente a sua cor, nao mostrando mais
            //a cor do drawble
            btnNotify.setBackgroundResource(R.drawable.bg_button_background_accent);

            btnNotify.setText(R.string.btn_notify_text);
            activated = false;

            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("activated");
            editor.remove("interval");
            editor.remove("hour");
            editor.remove("minute");
            editor.apply();
        }

        Log.d("test", "Hour: " + hour + " Minute: " + minute + " Inteval: " + interval);
    }


}