package com.example.netflixremake.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ImageDownloadTask extends AsyncTask<String, Void, Bitmap> {

    private WeakReference<ImageView> imageViewWeakReference;

    public ImageDownloadTask (ImageView imageView) {
        this.imageViewWeakReference = new WeakReference<>(imageView);
    }
    @Override
    protected Bitmap doInBackground(String... params) {
        String urlImg = params[0];
        HttpsURLConnection urlConnection = null;
        try {
            URL urlRequest = new URL(urlImg);
            urlConnection = (HttpsURLConnection) urlRequest.openConnection();

            urlConnection.setConnectTimeout(2000);
            urlConnection.setReadTimeout(2000);

            int statusCode = urlConnection.getResponseCode();
            if(statusCode != 200) {
                return null;
            }

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream != null) {
                //Transforma todo o stream em transforma em bytes de imagem
                return BitmapFactory.decodeStream(inputStream);
            }

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        //Verifica se a requisicao nao esta sendo cancelada, retorna verdadeiro se a tarefa foi cancelada antes de ser completada
       if (isCancelled())
           bitmap = null;

       ImageView imageView = imageViewWeakReference.get();
       if (imageViewWeakReference != null && bitmap!= null ) {

           //Fazendo as alteracoes do tamanho do bitmap, principalmente quando vem imagens menores do servidor, eh necessário redimensionar e escolar a imagem
           //para nao ficar esquisita
           if (bitmap.getWidth() < imageView.getWidth() || bitmap.getHeight() < imageView.getHeight()) {
               Matrix matrix = new Matrix();
               //escalando com aspectRatio
               matrix.postScale((float) imageView.getWidth() / (float) bitmap.getWidth(), (float) imageView.getHeight() / bitmap.getHeight());
                /*Pega a proporcao que foi feita em matrix para saber quanto tera que ser escalado mais (30%, 20%...)
                * Exemplo: comparando as larguras, se a imageView tiver 30dp de largura e o bitmap 25dp, fazendo a divisao de 30/25 o resultado seria 1,2. Ou seja,
                * A largura do bitmap precisa ser multiplicada por 1,2 para ficar no tamanho correto. Aparentemente quando se passa a matriz como parametro para criar
                * um novo bitmap, essa matriz contera esses valores que serao usados para multiplicar as dimensões do bitmap antigo.
                * */
               bitmap = Bitmap.createBitmap(bitmap,0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
           }
            imageView.setImageBitmap(bitmap);
       }
    }
}
