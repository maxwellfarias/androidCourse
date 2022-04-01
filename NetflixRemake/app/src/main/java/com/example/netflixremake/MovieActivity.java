package com.example.netflixremake;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.netflixremake.model.Movie;
import com.example.netflixremake.model.MovieDetail;
import com.example.netflixremake.util.ImageDownloadTask;
import com.example.netflixremake.util.MovieDetailTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MovieActivity extends AppCompatActivity implements MovieDetailTask.MovieDetailLoader {
    RecyclerView rvSimilar;
    MovieSimilarAdapter movieSimilarAdapter;
    TextView textViewTitleCover;
    TextView textViewDesc;
    TextView textViewCast;
    private ImageView imageCover;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        rvSimilar = findViewById(R.id.rv_similar);
        textViewTitleCover = findViewById(R.id.text_view_title_cover);
        textViewCast = findViewById(R.id.text_view_cast);
        textViewDesc = findViewById(R.id.text_view_desc);
        imageCover = findViewById(R.id.image_view_cover_play);

        Toolbar toolbar = findViewById(R.id.toolbar);
        /*Foi necessario colocar manualmente o import: import android.widget.Toolbar;
        O setSupportActionBar tem uma compatibilidade maior com dispositivos antigos */
        setSupportActionBar(toolbar);

        //Verifica se o ActionBar foi adicionada
        if (getSupportActionBar() != null) {
            //Exibe o icone de volta padrao do android
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //Muda o icone padrao de volta do android por outro
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
            //Oculta o titulo
            getSupportActionBar().setTitle(null);
        }


        //Eh necessario passar uma lista de filmes, mesmo que vazia como construtor, porque o metodo getItemCount() da MovieSimilarAdapter sera chamado antes
        //de ser inserida a lista de filmes definitiva.
        List<Movie> movies = new ArrayList<>();
        movieSimilarAdapter = new MovieSimilarAdapter(movies);
        rvSimilar.setAdapter(movieSimilarAdapter);
        rvSimilar.setLayoutManager(new GridLayoutManager(this, 3));


        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            int id = extras.getInt("id");
            MovieDetailTask movieDetailTask = new MovieDetailTask(this);
            movieDetailTask.setMovieDetailLoader(this);
            movieDetailTask.execute("https://tiagoaguiar.co/api/netflix/" + id);
        }
    }

    //Esse eh o metodo do AppCompact e eh chamado automaticamente sempre que um item do menu eh selecionado. Abaixo eh feita uma verificacao
    //dentro do if, se 'item.getItemId()' (O item selecionado) for igual a 'android.R.id.home' (seta de voltar 'home') sera chamado o finish()
    //que destruira a atividade atual voltando para a ativiadade anterior.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResult(MovieDetail movieDetail) {
        textViewDesc.setText(movieDetail.getDesc());
        textViewCast.setText(movieDetail.getCast());
        textViewTitleCover.setText(movieDetail.getTitle());

        movieSimilarAdapter.setMovies(movieDetail.getMoviesSimilar());
        //Notifica que todos os dados que estavam sendo esperados já podem ser populados no movieSimilarAdapter
        movieSimilarAdapter.notifyDataSetChanged();

        //Adicionando a imagem da player do movie
        ImageDownloadTask imageDownloadTask = new ImageDownloadTask(imageCover);
        imageDownloadTask.setShadowEnabled(true);
        imageDownloadTask.execute(movieDetail.getCoverUrl());

    }

    private class MovieSimilarHolder extends  RecyclerView.ViewHolder {
       private ImageView imageViewSimilarItem;

        public ImageView getImageViewSimilarItem() {
            return imageViewSimilarItem;
        }

        public MovieSimilarHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            imageViewSimilarItem = itemView.findViewById(R.id.image_view_cover_similar);
        }
    }

    private class MovieSimilarAdapter extends RecyclerView.Adapter<MovieSimilarHolder> {

        private List<Movie> movies;

        public MovieSimilarAdapter(List<Movie> movies) {
            this.movies = movies;
        }


        @NonNull
        @NotNull
        @Override
        public MovieSimilarHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            return new MovieSimilarHolder(getLayoutInflater().inflate(R.layout.movie_item_similar, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull MovieSimilarHolder holder, int position) {
          Movie movie = movies.get(position);
          new ImageDownloadTask(holder.getImageViewSimilarItem()).execute(movie.getCoverUrl());


        }

        @Override
        public int getItemCount() {
            return movies.size();
        }

        public void setMovies(List<Movie> movies) {
            this.movies.clear();
            this.movies.addAll(movies);
        }
    }
}