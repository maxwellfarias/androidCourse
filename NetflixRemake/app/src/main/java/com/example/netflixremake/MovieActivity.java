package com.example.netflixremake;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

public class MovieActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        Toolbar toolbar = findViewById(R.id.toolbar);
        /*Foi necessario colocar manualmente o import: import android.widget.Toolbar;
        O setSupportActionBar tem uma compatibilidade maior com dispositivos antigos */
        setSupportActionBar(toolbar);

        //Verifica se o ActionBar foi adicionada
        if (getSupportActionBar() != null) {
            //Exibe o icone de home
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //Adiciona a seta para voltar
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        }
    }
}