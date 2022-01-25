package com.example.netflixremake;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.netflixremake.model.Category;
import com.example.netflixremake.model.Movie;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        List<Category> categories = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Category category = new Category("Cat " + i);

            List<Movie> movies = new ArrayList<>();
            for (int j = 0; j < 30; j++) {
                Movie movie = new Movie();
                movie.setCoverUrl(R.drawable.movie_4);
                movies.add(movie);
            }

            category.setMovies(movies);
            categories.add(category);

        }

        RecyclerView rvMain = findViewById(R.id.rv_view_main);
        rvMain.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        rvMain.setAdapter(new MainAdapter (categories));

    }

    private class CategoryHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private RecyclerView recyclerViewMain;

        public TextView getTextViewTitle() {
            return textViewTitle;
        }

        public RecyclerView getRecyclerViewMain() {
            return recyclerViewMain;
        }

        public CategoryHolder(@NotNull View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.text_view_title);
            recyclerViewMain = itemView.findViewById(R.id.rv_view_main);
        }
    }


    private class MainAdapter extends RecyclerView.Adapter<CategoryHolder> {
        private List<Category> categories;

        public MainAdapter(List<Category> categories) {
            this.categories = categories;
        }



        @NonNull
        @NotNull
        @Override
        public CategoryHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            return new CategoryHolder (getLayoutInflater().inflate(R.layout.category_item, parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull CategoryHolder holder, int position) {
            Category category = categories.get(position);
            holder.getTextViewTitle().setText(category.getName());
            holder.getRecyclerViewMain().setLayoutManager(new LinearLayoutManager(getBaseContext(), RecyclerView.HORIZONTAL, false));
            holder.getRecyclerViewMain().setAdapter(new MovieAdapter (category.getMovies()));

        }

        @Override
        public int getItemCount() {
            return categories.size();
        }
    }

    private class MovieHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewCover;

        public ImageView getImageViewCover() {
            return imageViewCover;
        }


        public MovieHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imageViewCover = itemView.findViewById(R.id.image_view_cover);
        }
    }

    private class MovieAdapter extends RecyclerView.Adapter<MovieHolder> {

        private final List<Movie> movies;

        private MovieAdapter(List<Movie> movies) {
            this.movies = movies;
        }

        @NonNull
        @NotNull
        @Override
        public MovieHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            return new MovieHolder (getLayoutInflater().inflate(R.layout.movie_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull MovieHolder holder, int position) {
            Movie movie = movies.get(position);
            holder.getImageViewCover().setImageResource(movie.getCoverUrl());
        }

        @Override
        public int getItemCount() {
            return movies.size();
        }
    }



}