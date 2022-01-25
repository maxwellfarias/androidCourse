package com.example.netflixremake.model;

import java.util.List;
//Faz referencia a cada categoria do filme dividida por temas
public class Category {

    public Category(String name) {
        this.name = name;
    }

    private String name;
    private List<Movie> movies;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }
}
