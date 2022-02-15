package com.example.beberagua;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

//Cria um servico de background no android atraves do broadcast
//Tudo o que comeca com com 'on' sao eventos (onCreate, OnReceive, onClick)
//BroadcastReceiver eh o recurso que ira escutar qualquer notificacao que vira do SO android
public class NotificationPublisher extends BroadcastReceiver {
    //declaracao das chaves que buscarao as informacoes vindas da MainActivity
    public static final String KEY_NOTIFICATION = "key_notification";
    public static final String KEY_NOTIFICATION_ID = "key_notification_id";

    //O metodo onReceive sera disparado quando acontecer o evento de broadcast
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        //Cria a intencao que sera usada para abrir o apicativo quando tocar na notificacao
        Intent ii = new Intent(context.getApplicationContext(), MainActivity.class);
        //Criando uma intecao pendente
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, ii, 0);
        //Cria um objeto que faz a manipulacao das notificacoes em background da MainActivity
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //Pega a mensagem e o id que vem da MainActivity
        String message = intent.getStringExtra(KEY_NOTIFICATION);
        int id = intent.getIntExtra(KEY_NOTIFICATION_ID,0);
        //getNotification retorna uma notificacao construida
        Notification notification = getNotification(message, context, notificationManager, pIntent);

        //Ativa a notificacao
        notificationManager.notify(id, notification);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Notification getNotification (String content, Context context, NotificationManager manager, PendingIntent pIntent) {
        //Sera criada uma notificacao atraves de um construtor builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext())
                //Adiciona a mensagem da notificacao
                .setContentText(content)
                //Cria uma intencao para que a tela abra o aplicativo ao ser clicada
                .setContentIntent(pIntent)
                //Adiciona o titulo
                .setTicker("Alerta")
                //Evita cancelar de maneira automatica se nao apertar a notificacao
                .setAutoCancel(false)
                //Adiciona a notificacao padrao
                .setDefaults(Notification.DEFAULT_SOUND)
                //Adiciona o item que sera exibido na notificacao
                .setSmallIcon(R.mipmap.ic_launcher);

        //Verifica se a versao do SDK do smartphone eh igual ou maior que a versao do android oreo
        //As regras de notificacao a partir do android oreo mudaram, entao eh necessario seguir o codigo abaixo
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "YOUR_CHANNEL_ID";
            NotificationChannel channel =
                    new NotificationChannel(channelId, "Channel", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }
        //Retorna uma notificacao construida
        return builder.build();
    }
}
