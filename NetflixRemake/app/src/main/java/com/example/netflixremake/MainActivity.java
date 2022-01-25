package com.example.netflixremake;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.netflixremake.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Movie> movies = new ArrayList<>();

        for (int i = 0; i<30; i++){
            Movie movie = new Movie();
            movie.setCoverUrl("Item " + i);
            movies.add(movie);
        }

        RecyclerView rvMain = findViewById(R.id.rv_view_main);
        rvMain.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        MainAdapter adapter = new MainAdapter(movies);
        rvMain.setAdapter(adapter);

    }

    private class MovieHolder extends RecyclerView.ViewHolder {
        public MovieHolder(@NonNull View itemView) {
            super(itemView);
        }

        void bind (Movie movie){

            TextView textView = itemView.findViewById(R.id.text_view_url);

            textView.setText(movie.getCoverUrl());



        }
    }

    private class MainAdapter extends RecyclerView.Adapter<MovieHolder> {

        private final List<Movie> movies;

        private MainAdapter (List<Movie> movies) {
            this.movies = movies;
        }
        @NonNull
        @Override
        public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MovieHolder(getLayoutInflater().inflate(R.layout.movie_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull MainActivity.MovieHolder holder, int position) {
            holder.bind(movies.get(position));

        }

        @Override
        public int getItemCount() {
            return movies.size();
        }
    }
}