package co.tiagoaguiar.codelab.myapplication.archives;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

//Classe que ira gerencias as queries para o db a fim de realizar o  CRUD
public class SQLHelper extends SQLiteOpenHelper {

    //Como nao sera necessario instanciar um objeto para cada query, sera utilizado o padrao singleton
    // a fim de usar um unica instancia para fazer todas as queries
    private static SQLHelper INSTANCE;

    private static final String DB_NAME = "fitness_tracker.db";

    //Sempre que for mudado a versao do banco, sera executado o metodo onUpgrade
    private static final int DB_VERSION = 2;

    //Metodo estatico que ira fazer a instancia do objeto apenas uma vez
    static SQLHelper getInstance(Context context) {

        //Se INSTANCE for null, significa que ela ainda nao foi instanciada e dar-se inicio ao instanciamento
        if (INSTANCE == null)
            INSTANCE = new SQLHelper(context); //Quando so ha uma "linha" pode fazer o if de forma resumida
        return INSTANCE;


    }

    public SQLHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //Quando nao existir uma base de dados, sera executado o metodo onCreate
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Montando a estrutura do banco de dados
        db.execSQL(
                "CREATE TABLE calc (id INTEGER primary key, type_calc TEXT, res DECIMAL, create_date DATETIME)"
        );
    }

    //Quando existir uma base de dados, sera executado o metodo onUpdate para atualizar
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("test", "onUpgrade acionado");
    }

    List<Register> getRegisterBy(String type) {
        List<Register> registers = new ArrayList<>();

        //getReadableDatabase :  Retorna um objeto que auxilia nas queries de leitura
        SQLiteDatabase db = getReadableDatabase();

        //Cursor do banco de dados onde podera ser feito a varredura das linhas da tabela do banco de dados.
        Cursor cursor = db.rawQuery("select * from calc where type_calc = ?", new String[]{type});

        try {
            //Move o curso para a primeira linha e se o cursor estiver  vazio, sera retornado falso
            if (cursor.moveToFirst()) {
                do {
                    Register register = new Register();

                    //Sera setado no register as informacoes que estao no BD. A cada loop o curso esta em uma linha diferente, sendo usado
                    //o cursor.getString que retorna uma string da index da coluna que se deseja e o cursor.getColumnIndex ("coluna_desejada")
                    // para retornar a index da coluna.
                    register.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    register.setType(cursor.getString(cursor.getColumnIndex("type_calc")));
                    register.setCreatedDate(cursor.getString(cursor.getColumnIndex("create_date")));
                    register.setResponse(cursor.getDouble(cursor.getColumnIndex("res")));
                    registers.add(register);

                } while (cursor.moveToNext()); //Se houve um proximo elemento para mover, ele fara o loop novamente
            }

        } catch (Exception e) {
            Log.e("SQLite", e.getMessage(), e);
        } finally {
            //Verifica se o curso esta aberto e eh diferente de null, em caso positivo, sera fechado
            if (cursor != null && !cursor.isClosed()) cursor.close();

        }
        return registers;
    }


    //Funcao para adicionar um novo item de registro de calculo
    // O long retornado eh o ID que sera inserido no banco de dados
    long addItem(String type, double response) {


        //      getWritableDatabase() -> Para inserir, atualizar e excluir registros*/

        SQLiteDatabase db = getReadableDatabase();

        //Se o valor continuar zero eh porque nao sofreu nenhuma alteracao.
        long calcId = 0;

        try {
            //Abri a conexao
            db.beginTransaction();

            //Armazenado os registros que se quer salvar, ContentValues eh um objeto AndroidStudio que trabalha com chaves e valores
            ContentValues values = new ContentValues();
            values.put("type_calc", type);
            values.put("res", response);

            //SimpleDateFormat eh um objeto que ira formatar a data antes de ser inserida no DB. Locale eh um
            // objeto que especifica a lingua que o pattern deve ser formatado
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("pt", "BR"));

            // sdf.format(new Date) retorna uma string com a hora atual
            String now = sdf.format(new Date());
            values.put("create_date", now);

            //Insere os dados e em caso de erro lanca uma excecao. Parametros sao: Nome_tabela, Nome_Colunas, valores_para_mudar_de_estado.
            //Seria basicamente um INSERT INTO (COLUNA1, COLUNA2...) VALUES (VALUE1, VALUE2...)
            //O metodo db.insertOrThrow retorna sempre que eh feito um insert um identificador dessa query
            calcId = db.insertOrThrow("calc", null, values);

            //Efetivar a transacao no banco de dados
            db.setTransactionSuccessful();

        } catch (Exception e) {
            //Log que retornara uma mensagem  de erro
            Log.e("SQLite", e.getMessage(), e);

        } finally {
            //Se a conexao estiver aberta, ela sera fechada.
            if (db.isOpen())
                db.endTransaction();
        }
        return calcId;
    }

    // Metodo que realiza a atualizacao das informacoes no banco de dados
    long updateItem (String type, double response, int id) {

        //A funcao mais abaico db.update retornara um inteiro com o numero de linhas afetadas
        long calcId = 0;
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();

        try {
                ContentValues values = new ContentValues();
                values.put("type_calc", type);
                values.put("res", response);

                //Usou o padrao builder para retornar uma string
              String now = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss", new Locale("pt", "BR"))
                      //A linha acima retornaria um objeto, mas o codigo pode continuar colocando um '.metodo_do_objeto'
                      .format(Calendar.getInstance().getTime());
              values.put("create_date", now);

              calcId = db.update("calc", values, "id = ? and type_calc = ?", new String[]{String.valueOf(id), type});

              db.setTransactionSuccessful();

        } catch (Exception e) {
            Log.d("SQLite", e.getMessage(), e);
        }
        finally {
            db.endTransaction();
        }
        return calcId;
    }

       int deleteItem (int id) {
        int calcId = 0;
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();

        try {
            calcId = db.delete("calc", "id = ?", new String[]{String.valueOf(id)});
            db.setTransactionSuccessful();

        } catch (Exception e) {
            Log.e("SQLite", e.getMessage(), e);
        } finally {
            db.endTransaction();
        }

        return calcId;

       }


}
