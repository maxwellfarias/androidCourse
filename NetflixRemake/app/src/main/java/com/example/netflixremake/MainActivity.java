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
import com.example.netflixremake.util.CategoryTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CategoryTask.CategoryLoader {

    private RecyclerView rvMain;
    private MainAdapter mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        List<Category> categories = new ArrayList<>();

        rvMain = findViewById(R.id.rv_view_main);
        rvMain.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        mainAdapter = new MainAdapter(categories);
        rvMain.setAdapter(mainAdapter);


        //o parametro this significa que a intancia MainActivity esta sendo passada como parametro e vai implementar o categoryLoader.onResult, uma vez que a
        // categoryLoader se comporta como uma variavel que armazena objetos que implementam os seus metodos.
        CategoryTask categoryTask = new CategoryTask(this);
        categoryTask.setCategoryLoader(this);
        //Executa a progressBar, o metodo execute tem como parametro o link que se encontra o arquivos json
        categoryTask.execute("https://tiagoaguiar.co/api/netflix/home");


        }

        //Passa as categorias novas para o MainAdapter
        @Override
        public void onResult(List<Category> categories) {
            mainAdapter.setCategories(categories);
            //Notifica que os todos os dados que estavam sendo esperados já podem ser populados no mainAdapter
            mainAdapter.notifyDataSetChanged();
        }

        private class CategoryHolder extends RecyclerView.ViewHolder {
            private TextView textViewTitle;
            private RecyclerView recyclerViewMovie;

            public TextView getTextViewTitle() {
                return textViewTitle;
            }

            public RecyclerView getRecyclerViewMovie() {
                return recyclerViewMovie;
            }

            public CategoryHolder(@NotNull View itemView) {
                super(itemView);

                textViewTitle = itemView.findViewById(R.id.text_view_title);
                recyclerViewMovie = itemView.findViewById(R.id.rv_movie);
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
                return new CategoryHolder(getLayoutInflater().inflate(R.layout.category_item, parent, false));
            }

            @Override
            public void onBindViewHolder(@NonNull @NotNull CategoryHolder holder, int position) {
                Category category = categories.get(position);
                holder.getTextViewTitle().setText(category.getName());
                holder.getRecyclerViewMovie().setLayoutManager(new LinearLayoutManager(getBaseContext(), RecyclerView.HORIZONTAL, false));
                holder.getRecyclerViewMovie().setAdapter(new MovieAdapter(category.getMovies()));

            }

            @Override
            public int getItemCount() {
                return categories.size();
            }

            public void setCategories(List<Category> categories) {

                //Limpa a categoria atual, o mainAdapter.notifyDataSetChanged() só funcionara se usar o clear() para limpar a referencia da lista passada
                //caso contrário dara um erro.
                this.categories.clear();
                //Adiciona todas as novas categorias com addAll
                this.categories.addAll(categories);
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
                return new MovieHolder(getLayoutInflater().inflate(R.layout.movie_item, parent, false));
            }

            @Override
            public void onBindViewHolder(@NonNull @NotNull MovieHolder holder, int position) {
                Movie movie = movies.get(position);
               // holder.getImageViewCover().setImageResource(movie.getCoverUrl());
            }

            @Override
            public int getItemCount() {
                return movies.size();
            }
        }


}
