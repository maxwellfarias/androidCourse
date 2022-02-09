package com.example.netflixremake.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.example.netflixremake.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
/*
* Essa classe eh responsavel por fazer requisicoes para o servidor, converter o stream de bytes em um bitmap e adicionar o bitmap na imageview que
* eh passada como parametro
* */
public class ImageDownloadTask extends AsyncTask<String, Void, Bitmap> {

    private WeakReference<ImageView> imageViewWeakReference;
    //Flag para saber se tem um shadow ativado
    private boolean shadowEnabled;

    public ImageDownloadTask (ImageView imageView) {
        this.imageViewWeakReference = new WeakReference<>(imageView);
    }
    // Essa flag permite desviar o fluxo no onPostExecute a fim de adicionar uma sombra na imagemView atraves do drawble shadows.xml
    public void setShadowEnabled(boolean shadowEnabled) {
        this.shadowEnabled = shadowEnabled;
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
                //Transforma todo o stream em transforma em bytes de imagem (Bitmap)
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

           //Desvia o fluxo caso o metodo setShadowEnabled tiver sido setado com true. Eh feita a conversao do bitmap em drawable e esse
           //drawable eh usado para modificar a imagem de fundo da shadows.xml, fazendo com que haja um efeito de sombra.
           if (shadowEnabled) {
               //Cria u drawable que faz referencia ao shadows
               LayerDrawable drawable = (LayerDrawable) ContextCompat.getDrawable(imageView.getContext(),
                       R.drawable.shadows);
               if (drawable != null) {
                   //Cria um drawble a partir de uma bitmap
                   BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
                   //Muda a imagem no drawble shadows para a imagem que foi carregada atraves do link
                   drawable.setDrawableByLayerId(R.id.cover_drawable, bitmapDrawable);
                   //Muda a imagem no imageView
                   imageView.setImageDrawable(drawable);
               }
           }
           else {
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
                   bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
               }
               imageView.setImageBitmap(bitmap);

           }
       }
    }
}
