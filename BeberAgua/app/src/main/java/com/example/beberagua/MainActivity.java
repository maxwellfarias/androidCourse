package com.example.beberagua;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final String KEY_NOTIFY = "key_notify";
    private static final String KEY_HOUR = "key_hour";
    private static final String KEY_MINUTE = "key_minute";
    private static final String KEY_INTERVAL = "key_interval";


    private Button btnNotify;
    private EditText editTextInterval;
    private TimePicker timePicker;
    private Boolean activated;
    private SharedPreferences storage;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Local database for storing small amounts of data
        storage = getSharedPreferences("db", Context.MODE_PRIVATE);

        btnNotify = findViewById(R.id.btn_notify);
        editTextInterval = findViewById(R.id.txt_edit_editMinutes);
        timePicker = findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);

        activated = storage.getBoolean(KEY_NOTIFY, false);
        setUpUI(activated, storage);
        btnNotify.setOnClickListener(notifyListener);

        Button btnBroadcast = findViewById(R.id.btn_broadcast);
        btnBroadcast.setOnClickListener(broadcastTest);
    }

    //ESTUDANDO O CICLO DE VIDA DE UMA ACTIVITY NO ANDROID


    @Override
    protected void onStart() {
        super.onStart();
        Log.i("Teste", "onStart");
        //Eh executado ao termino no onCreate, indicando que todas as configuraçoes ja foram realizadas e o app esta pronto para o uso identificando
        // dessa forma que alguem abriu a tela do app
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("Teste", "onRestart");
        //Acao atividada quando as seguintes acoes sequenciais sao executadas: onStop, onStart e onResume.
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Teste", "onStop");
        //Eh acionado quando o app para de crakear
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Teste", "onDestroy");
        //Eh executado quando o app eh encerrado. Pode ser útil para realizar uma sincronizacao semelhante a do anki ao termino do app.
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Teste", "onResume");
        //Eh executada quando o app eh executado pela primeira vez ou quando outra janela estava sendo executado e o usuario retorna para essa janela
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Teste", "onPause");
        //Acao executado quando o usuario esta em outra tela
    }

    private void setUpUI(Boolean activated, SharedPreferences storage) {
        if (activated) {
            btnNotify.setBackgroundResource(R.drawable.bg_button_background);
            btnNotify.setText(R.string.pause);
            timePicker.setCurrentHour(storage.getInt(KEY_HOUR, timePicker.getCurrentHour()));
            timePicker.setCurrentMinute(storage.getInt(KEY_MINUTE, timePicker.getCurrentMinute()));
            editTextInterval.setText(String.valueOf(storage.getInt(KEY_INTERVAL, 0)));
        } else {
            btnNotify.setBackgroundResource(R.drawable.bg_button_background_accent);
            btnNotify.setText(R.string.btn_notify_text);
        }

    }

    private void alert(int resId) {
        Toast.makeText(MainActivity.this, resId, Toast.LENGTH_SHORT).show();
    }

    private boolean intervalIsValid() {
        String sInterval = editTextInterval.getText().toString();
        if (sInterval.isEmpty()) {
            alert(R.string.msg_error);
            return false;
        } else if (sInterval.equals("0")) {
            alert(R.string.zero_value);
            return false;
        }
        return true;
    }

    private void updateStorage(boolean added, int hour, int minute, int interval) {
        SharedPreferences.Editor editor = storage.edit();
        editor.putBoolean(KEY_NOTIFY, added);

        if (added) {
            editor.putInt(KEY_HOUR, hour);
            editor.putInt(KEY_MINUTE, minute);
            editor.putInt(KEY_INTERVAL, interval);
        } else {
            editor.remove(KEY_HOUR);
            editor.remove(KEY_MINUTE);
            editor.remove(KEY_INTERVAL);
        }
        editor.apply();
    }

    private void setUpNotification(boolean added, int hour, int minute, int interval) {
        Intent notificationIntent = new Intent(MainActivity.this, NotificationPublisher.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if (added) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);


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

            /*Configura as informacoes da repeticao em um horario "nao tao exato", essa repeticao acha o melhor horario para disparar a fim de nao
            sobrecarregar o sistema do android.
            * Parametros: AlarmManager.RTC_WAKEUP-> Manda acordar o celular; hora de disparo em milisegundos (x60 para conveter os minutos no interval em
            segundos); intervalo de disparo; Operacao em si*/
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), interval * 60 * 1000, broadcast);


        } else {

            //Cancelando as notificacoes do aplicativo
            PendingIntent broadcast = PendingIntent.getBroadcast(MainActivity.this, 0, notificationIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT);
            alarmManager.cancel(broadcast);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private View.OnClickListener notifyListener = v -> {

        if (!intervalIsValid()) return;

        int interval = Integer.parseInt(editTextInterval.getText().toString());
        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();

        if (!activated) {

            updateStorage(true, hour, minute, interval);
            setUpUI(true, storage);
            setUpNotification(true, hour, minute, interval);
            alert(R.string.notified);

            activated = true;

        } else {
            updateStorage(false, 0, 0, 0);
            setUpUI(false, storage);
            alert(R.string.alarm_off);
            setUpNotification(false, 0, 0, 0);

            activated = false;
        }


    };

    private View.OnClickListener broadcastTest = v -> {
      startActivity(new Intent(this, BroadcastTest.class));
    };
}