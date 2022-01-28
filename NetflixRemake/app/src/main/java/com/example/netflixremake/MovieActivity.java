package com.example.netflixremake;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

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
            toolbar.setOnClickListener(v -> {
                    startActivity(new Intent(getBaseContext(), MainActivity.class));
            });
        }

        //Metodo para manipular um drawble, nao foi usado apenas o getDrawble pois o ContextCompat tem metodos com mais compatibilidade a dispositivos antigos
        //Foi feito um cast para (LayerDrawable), pois o retorno do ContextCompat.getDrawable seria apenas um drawable
        LayerDrawable drawable = (LayerDrawable) ContextCompat.getDrawable(this, R.drawable.shadows);

        //Verifica se eh diferente de null para nao dar nullpointer no codigo
        if (drawable != null) {
            Drawable movieCover = ContextCompat.getDrawable(this, R.drawable.movie);

            //Acessando o item das sombras e trocando a sua propriedade
            drawable.setDrawableByLayerId(R.id.cover_drawable, movieCover);

            //Acessa o ImageView do movieItem.xlm e troca a imagem de fundo.
            ((ImageView)findViewById(R.id.image_view_cover_play)).setImageDrawable(drawable);
        }

    }
}