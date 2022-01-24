package com.example.netflixremake;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.netflixremake.model.Movie;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rvMain = findViewById(R.id.rv_view_main);
    }

    private class MovieHolder extends RecyclerView.ViewHolder {

        public MovieHolder(@NonNull View itemView) {
            super(itemView);
        }

        void bind (){

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
            Movie movie = movies.get(position);

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }
}