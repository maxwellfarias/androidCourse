package com.example.netflixremake.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.example.netflixremake.model.Category;
import com.example.netflixremake.model.Movie;

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

/*AsyncTask cria um processo paralelo para nao travar a Thread princiapal a fim de fazer requisicoes
Os 3 parametros sao:
    1 - String(url) -> Esse valor eh passado recebido no metodo 'protected List<Category> doInBackground(String... params)' e
passado na MainActivity --> new CategoryTask(this).execute("https://tiagoaguiar.co/api/netflix/home"); Momento que eh passado a string com a url para se f
azer a requisicao .
    2 - um objeto Progress a fim de mostrar um carregamento na tela equanto eh feita a solicitacao dos dados para o servidor, nesse caso foi colocado o valor Void
apenas para nao fazer nada a fim de suprir o requisito de se ter esse parametro, pois a progress foi usado de outra forma no metodo onPreExecute -> dialog = ProgressDialog.show(context, "carregando", "", true);
    3 - Um retorno do doInBackground, no nosso caso estamos retornando uma List<Category> que eh o que o arquivo json de fato tem. Esse valor eh usado
    como parametro no metodo onPostExecute*/

public class CategoryTask extends AsyncTask<String, Void, List<Category>> {
    /*Criando uma progress bar, ela esta depreciada tambem nesse momento porque mostra a tela cheia, esse era um recurso
    utilizado antigamete. Eh possivel criar uma progress bar na interface grafica (FrameLayout)*/

    private final WeakReference<Context> context;
    private ProgressDialog dialog;
    private CategoryLoader categoryLoader;

    //Criado um construtor para pegar o context
    /*WeakReference<Context> diz que o context eh uma referencia fraca, dessa forma o CategoryTask eh destruido
     assim que ele perder a referencia do contexto que foi passado como parametro (nessa caso o contexto da MainActivity), seja pelo
      ciclo de vida que chegou ao fim ou por qualquer outro fato que impacte na performace e faca isso acontecer.
       Esse procedimento ajuda na perforamance do app*/
    public CategoryTask(Context context) {
        this.context = new WeakReference<>(context);
    }

    //Quem instancia-lo, pode passar o set e definir o listener para ele
    public void setCategoryLoader (CategoryLoader categoryLoader) {
        this.categoryLoader = categoryLoader;
    }

    //Sera execuatdo na thread principal uma progress bar e quando terminar a execucao, ela sera escondida.
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        //Pega o contexto atual
        Context context = this.context.get();
        //Criando a progressBar. O parametro indeterminate ao ser true fara com que a progressBar rode por tempo inderteminado ate que seja mandado esconder
        if (context != null) {
            dialog = ProgressDialog.show(context, "carregando", "", true);
        }
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

            //Quanto tempo deve ser esperado para mostrar uma mensagem de erro (nos casos em que a internet caiu)
            urlConnection.setConnectTimeout(2000);

            //Toda requisicao dara um status code, geralmente quando o status code eh maior que 400 eh porque algo deu errado.
            int responseCode = urlConnection.getResponseCode();
            if (responseCode >  400) {
                throw new IOException("Erro na comunicacao do servidor");
            }

           /* Stream eh uma sequencia de bytes que diz respeito a alguma informacao (imagem, texto, video...). Devido a isso, eh necessário converter
            toda a informação, nesse caso será convertido em string. Sera pego todos os bytes, le-los um por um e coloca-los em um array para devolver
            no formato de string*/
            InputStream inputStream = urlConnection.getInputStream();

            //Permite pegar e alocar espacos na memoria, para ser possivel manipular o stream em formato de buffer
            BufferedInputStream in = new BufferedInputStream(inputStream);

            String jsonAsString = toString(in);

            //Transformando toda a string em um objeto json
            List<Category> categories = getCategories(new JSONObject(jsonAsString));

            //Fecha a conexao
            in.close();

            // O classe CategoryTask tem como parametro generico uma List<Category>, quando eh feito um return categories o parametro da
            //extends AsyncTask recebe esse retorno e fica "sendo usado como uma variavel de campo", assim o metodo protected void onPostExecute(List<Category> categories)
            //pode usa-lo
            return categories;

        } catch (MalformedURLException e) {
            //Retorna um erro se a url estiver mal formatada
            e.printStackTrace();

            //Retorna um erro que nao conseguir abrir a conexao
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Quando o metodo doInBackground foi sobreescrito, aparece automaticamente esse retorno, significando que nao teve nenhum retorno anterior
        return null;
    }
    // metodo que converte string em um objeto json
    private List<Category> getCategories(JSONObject json) throws JSONException {
        List<Category> categories = new ArrayList<>();

        //Cria um objeto array no qual possui objtos json
        JSONArray categoryArray = json.getJSONArray("category");
        for (int i = 0; i < categoryArray.length(); i++) {
            JSONObject category = categoryArray.getJSONObject(i);
            String title = category.getString("title");

            JSONArray movieArray = category.getJSONArray("movie");
            List<Movie> movies = new ArrayList<>();
            for (int j = 0; j < movieArray.length(); j++) {
                JSONObject movieJson = movieArray.getJSONObject(j);

                String cover_url = movieJson.getString("cover_url");
                int id = movieJson.getInt("id");
                Movie movieObj = new Movie();
                movieObj.setCoverUrl(cover_url);
                movieObj.setId(id);
                movies.add(movieObj);
            }

            Category categoryObj = new Category(title, movies);

            categories.add(categoryObj);
        }
        return categories;
    }

    //Transformando todos os bytes em caracteres no formato de string
    private String toString(InputStream is) throws IOException {
        byte[] bytes = new byte[1024];
        //baos funciona como uma matriz de bytes que sera responsavel por armazenar varios arrays de bytes que virao do fluxos de bytes dado no parametro ( BufferedInputStream in = new BufferedInputStream(urlConnection.getInputStream());)
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        //o inteiro 'lidos' sera usado como teste para o loop acontecer, is.read(bytes) retorna um inteiro com o numero de bytes que foi lido em cada linha do fluxo, essa leitura eh
        //feita da primeira linha ate a ultima. Ao chegar no final, eh retornado o valor -1 indicando que nao ha mais dados para serem lidos.
        int lidos;
        while ((lidos = is.read(bytes)) > 0) {
            /*is.read(bytes)  -> o array bytes que eh passado como parametro e povoado com os bytes lidos na linha do fluxo e eh adicionado ao obeto baos atraves do codigo (baos.write(bytes, 0, lidos))
            baos funciona como uma matriz de bytes */
            baos.write(bytes, 0, lidos);
        }

        //Converte os bytes do objeto baos para string
        return new String(baos.toByteArray());
    }


    //Realiza uma acao na main Thread depois que thread que esta rodando em background terminar a sua acao.
    @Override
    protected void onPostExecute(List<Category> categories) {
        super.onPostExecute(categories);

        //Sumir com a barra de progressao
        dialog.dismiss();

        if (categoryLoader != null) {
            categoryLoader.onResult(categories);
        }

    }

    public interface CategoryLoader {
        void onResult (List<Category> categories);
    }
}
