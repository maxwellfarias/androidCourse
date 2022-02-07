package com.example.netflixremake.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.example.netflixremake.model.Movie;
import com.example.netflixremake.model.MovieDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MovieDetailTask extends AsyncTask <String, Void, MovieDetail> {
    private WeakReference<Context> context;
    private ProgressDialog dialog;
    private MovieDetailLoader movieDetailLoader;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Context context = this.context.get();
        if (context != null)
            dialog = ProgressDialog.show(context, "Carregando", "", true);
    }

    public MovieDetailTask (Context context) {
        this.context = new WeakReference<>(context);
    }
    @Override
    protected MovieDetail doInBackground(String... params) {

        try {
            URL url = new URL(params[0]);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();

            BufferedInputStream in = new BufferedInputStream(inputStream);
            String jsonAsString = toString (in);

            MovieDetail movieDetail = getMovieDetail(new JSONObject(jsonAsString));
            in.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private MovieDetail getMovieDetail(JSONObject json) throws JSONException {
        int id = json.getInt("id");
        String coverUrl = json.getString("cover_url");
        String title = json.getString("title");
        String desc = json.getString("desc");
        String cast = json.getString("cast");

        List<Movie> movies = new ArrayList<>();
        JSONArray jsonArray = json.getJSONArray("movie");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObjectMovie = jsonArray.getJSONObject(i);
            String similarCover = jsonObjectMovie.getString("cover_url");
            int SimilarId = jsonObjectMovie.getInt("id");
            Movie SimilarMovie = new Movie();
            SimilarMovie.setId(SimilarId);
            SimilarMovie.setCoverUrl(similarCover);
            movies.add(SimilarMovie);
        }
        Movie movie = new Movie();
        movie.setCoverUrl(coverUrl);
        movie.setId(id);
        movie.setCast(cast);
        movie.setDesc(desc);
        movie.setTitle(title);

        return new MovieDetail(movie, movies);
    }

    private String toString(BufferedInputStream in) throws IOException {
        byte[] bytes = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int lidos;
        while((lidos = in.read()) > 0)
            baos.write(bytes, 0, lidos);
        return new String(baos.toByteArray());
    }

    @Override
    protected void onPostExecute(MovieDetail movieDetail) {
        super.onPostExecute(movieDetail);
        dialog.dismiss();

        if (movieDetailLoader != null)
            movieDetailLoader.onResult(movieDetail);
    }

    public void setMovieDetailLoader(MovieDetailLoader movieDetailLoader) {
        this.movieDetailLoader = movieDetailLoader;
    }

    public interface MovieDetailLoader {
        void onResult (MovieDetail movieDetail);
    }
}
