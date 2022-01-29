package com.example.netflixremake.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.example.netflixremake.model.Category;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/*AsyncTask cria um processo paralelo para nao travar a Thread princiapal a fim de fazer requisicoes
Os parametro sao String(url), um objeto Void (colocado apenas para nao fazer nada a fim de suprir o requisito de se ter esse parametro)
e uma lista de categoria que eh o que o arquivo json de fato tem.*/

public class JsonDownloadTask extends AsyncTask <String, Void, List<Category>>{
    /*Criando uma progress bar, ela esta depreciada tambem nesse momento porque mostra a tela cheia, esse era um recurso
    utilizado antigamete. Eh possivel criar uma progress bar na interface grafica (FrameLayout)*/

    private final Context context;
    ProgressDialog dialog;

    //Criado um construtor para pegar o context
    public JsonDownloadTask (Context context) {
        this.context = context;
    }

    //Sera execuatdo na thread principal uma progress bar e quando terminar a execucao, ela sera escondida.
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        //Criando a progressBar. O parametro indeterminate ao ser true fara com que a progressBar rode por tempo inderteminado ate que seja mandado esconder
        dialog = ProgressDialog.show(context, "carregando", "", true);
    }

    //Faz com que a operacao rode em uma outra thread diferente da principal.
    // (String... params) means that zero or more String objects (or a single array of them)may be passed as the argument(s) for that method.
    @Override
    protected List<Category> doInBackground(String... params) {

        //Abrindo as conexoes
        String url = params[0];
        try {
            URL requestUrl = new URL(url);

            //Abrindo a conexao
            HttpsURLConnection urlConnection = (HttpsURLConnection) requestUrl.openConnection();

            //Tempo de espera de leitura (valor passado em milisegundos)
            urlConnection.setReadTimeout(2000);

            //Quanto tempo deve ser esperado para mostrar uma mensagem de erro
            urlConnection.setConnectTimeout(2000);

            //Toda requisicao dara um status code, geralmente quando o status code eh maior que 400 eh porque algo deu errado.
            int responseCode = urlConnection.getResponseCode();
            if(responseCode > 400) {
                throw new IOException("Erro na comunicacao do servidor");
            }

           /* Stream eh uma sequencia de bytes que diz respeito a alguma informacao (imagem, texto, video...). Devido a isso, eh necessário converter
            toda a informação, nesse caso será convertido em string. Sera pego todos os bytes, le-los um por um e coloca-los em um array para devolver
            no formato de string*/
            InputStream inputStream = urlConnection.getInputStream();
            //FAZENDO A CONVERSAO DE STRING PARA STREAM:
            //Permite pegar e alocar espacos na memoria, para ser possivel manipular o stream em formato de buffer
            BufferedInputStream in = new BufferedInputStream(urlConnection.getInputStream());

            String jsonAsString = toString(in);

        } catch (MalformedURLException e) {
            //Retorna um erro se a url estiver mal formatada
            e.printStackTrace();

            //Retorna um erro que nao conseguir abrir a conexao
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Transformando todos os bytes em caracteres no formato de string
    private String toString (InputStream is) throws IOException {
        byte[] bytes = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int lidos;
        while ((lidos = is.read(bytes)) > 0) {
            //Toda vez que eles conseguirem ler esses bytes, no caso maior que zero, eu vou escrever na saida esses bytes, iniciando no indice zero
            //indo ate o final
            baos.write(bytes, 0, lidos);
        }

        //Converte todos os caracteres em uma string
        return new String(baos.toByteArray());
    }


    //Realiza uma acao na main Thread depois que thread que esta rodando em background terminar a sua acao.
    @Override
    protected void onPostExecute(List<Category> categories) {
        super.onPostExecute(categories);

        //Sumir com a barra de progressao
        //dialog.dismiss();
    }
}
