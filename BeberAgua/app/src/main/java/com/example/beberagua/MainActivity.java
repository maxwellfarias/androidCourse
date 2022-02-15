package com.example.beberagua;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlarmManager;
import android.app.NotificationManager;
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

import java.util.Calendar;

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

            /*Esse objeto foi criado a fim de colocar as informacoes da hora e minutos do vindos da marcacao do timerPicker, em seguida esse tempo
            sera convertido
            em milisegundos*/
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);

            //REGISTRANDO O PRIMEIRO ALARME

            //Criando a intencao
            Intent notificationIntent = new Intent(MainActivity.this, NotificationPublisher.class);
            //Adicionando as informacoes que serao mandadas para a NotificationPublisher
            notificationIntent.putExtra(NotificationPublisher.KEY_NOTIFICATION_ID, 1);
            notificationIntent.putExtra(NotificationPublisher.KEY_NOTIFICATION, "Hora de beber água");
            /*Criando o broadcast (que eh uma intencao pendente). Os broadcasts sao recursos de transmissao de dados em background-> context, requisicao
             ID (quem eh quem esta chamando), intencao (nesse caso uma intencao de notification), flag
             com um metodo de atualizacao, nesse caso FLAG_CANCEL_CURRENT o qual indica que se ja existir uma atual pendingItent, esta deve ser cancelada
              antes de gerar uma nova*/
            PendingIntent broadcast = PendingIntent.getBroadcast(MainActivity.this, 0, notificationIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT);


            //Pegando o alarme para manipula-lo
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            /*Configura as informacoes da repeticao em um horario "nao tao exato", essa repeticao acha o melhor horario para disparar a fim de nao
            sobrecarregar o sitema do android.
            * Parametros: AlarmManager.RTC_WAKEUP-> Manda acordar o celular; hora de disparo em milisegundos (x60 para conveter os minutos no interval em
            segundos); intervalo de disparo; Operacao em si*/
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), interval * 60 * 1000, broadcast);


        } else {
            //Se em outra parte do codigo for mudado a cor desse mesmo botao, sera conservado o drawble, e sera mudado somente a sua cor, nao mostrando
            // mais a cor do drawble
            btnNotify.setBackgroundResource(R.drawable.bg_button_background_accent);

            btnNotify.setText(R.string.btn_notify_text);
            activated = false;

            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("activated");
            editor.remove("interval");
            editor.remove("hour");
            editor.remove("minute");
            editor.apply();

            //Cancelando as notificacoes do aplicativo
            Intent notificationIntent = new Intent (MainActivity.this, NotificationPublisher.class);
            PendingIntent broadcast = PendingIntent.getBroadcast(MainActivity.this, 0, notificationIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService (Context.ALARM_SERVICE);
            alarmManager.cancel(broadcast);


        }

        Log.d("test", "Hour: " + hour + " Minute: " + minute + " Inteval: " + interval);
    }


}