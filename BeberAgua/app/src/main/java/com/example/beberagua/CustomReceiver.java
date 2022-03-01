package com.example.beberagua;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;


public class CustomReceiver extends BroadcastReceiver {

    private static final String ACTION_CUSTOM_BROADCAST = BuildConfig.APPLICATION_ID+".ACTION_CUSTOM_BROADCAST";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("test", ""+context);
        String toastMessage = "";
        String intentAction = intent.getAction();
        if (intentAction != null) {
            switch (intentAction) {
                case Intent.ACTION_POWER_CONNECTED:
                    toastMessage = "Dispositivo Conectado";
                    break;
                case Intent.ACTION_POWER_DISCONNECTED:
                    toastMessage = "Dispositivo Desconectado";
                    break;
                case ACTION_CUSTOM_BROADCAST:
                    toastMessage = "Broadcast personalizado recebido "+ACTION_CUSTOM_BROADCAST;
                    break;
            }
        }
        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
    }
}