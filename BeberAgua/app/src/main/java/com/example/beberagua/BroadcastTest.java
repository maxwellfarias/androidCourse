package com.example.beberagua;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BroadcastTest extends AppCompatActivity {
    private CustomReceiver mReceiver = new CustomReceiver();

    //Identificador unico para fazer uma transmissao personalizada
    private static final String ACTION_CUSTOM_BROADCAST = BuildConfig.APPLICATION_ID+".ACTION_CUSTOM_BROADCAST";
    private Button btnBroadcast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_test);

        btnBroadcast = findViewById(R.id.sendBroadcast);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);

        // Register the receiver using the activity context.
        this.registerReceiver(mReceiver, intentFilter);

        //Registra o receptor com a itent de acao personalizada
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(mReceiver, new IntentFilter (ACTION_CUSTOM_BROADCAST));

        btnBroadcast.setOnClickListener(customBroadcast);




    }

    @Override
    protected void onDestroy() {
        //os receptores dinâmicos devem ser desregistrados quando não forem mais necessários para economizar recursos do sistema e evitar vazamentos
        this.unregisterReceiver(mReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    private View.OnClickListener customBroadcast = v-> {
        //Crie um novo Intent, com sua string de ação personalizada como argumento.
        Intent customBroadIntent = new Intent(ACTION_CUSTOM_BROADCAST);
        //Após a Intentdeclaração personalizada, envie a transmissão usando a LocalBroadcastManagerclasse:
        LocalBroadcastManager.getInstance(this).sendBroadcast(customBroadIntent);
    };
}