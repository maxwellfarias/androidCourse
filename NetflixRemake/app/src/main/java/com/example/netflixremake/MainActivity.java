package com.example.netflixremake;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.example.netflixremake.model.Category;
import com.example.netflixremake.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        List<Category> categories = new ArrayList<>();

        for (int i=0; i<10; i++) {
            Category category = new Category("Cat " + i);

            List<Movie> movies = new ArrayList<>();
            for (int j = 0; j<30; j++){
                Movie movie = new Movie();
                movie.setCoverUrl(R.drawable.movie);
                movies.add(movie);
            }

            category.setMovies(movies);
            categories.add(category);

        }


        RecyclerView rvMain = findViewById(R.id.rv_view_main);
        rvMain.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        MainAdapter adapter = new MainAdapter(categories);
        rvMain.setAdapter(adapter);

    }

    

    private class MovieHolder extends RecyclerView.ViewHolder {
        public MovieHolder(@NonNull View itemView) {
            super(itemView);
        }

        void bind (Category category){


        }
    }

    private class MainAdapter extends RecyclerView.Adapter<MovieHolder> {

        private final List<Category> categories;

        private MainAdapter (List<Category> categories) {
            this.categories = categories;
        }
        @NonNull
        @Override
        public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MovieHolder(getLayoutInflater().inflate(R.layout.movie_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull MainActivity.MovieHolder holder, int position) {
            holder.bind(categories.get(position));

        }

        @Override
        public int getItemCount() {
            return categories.size();
        }
    }
}