package co.tiagoaguiar.fitnesstracker.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import java.net.CacheRequest

//Banco de dados do aplicativo

// [Calc::class] essa forma de passar um array de algum tipo aparentemente so pode ser usada em
// annotations. Aqui definimos quais tablemas terao esse banco de dados
@Database(entities = [Calc::class], version = 1)
@TypeConverters(DateConverter::class) //Recebe como parametro as classes que fazem as conversoes dos
//valores que serao usados no DB, nesse caso, usamos somente a classe que converte as datas.

abstract class AppDatabase : RoomDatabase() {

    //Devolve o DAO que se quer trabalhar, ou seja, um objeto com metodos para fazer manipulacoes.
    //O Room aparentemente procura alguma abstract fun que retorna algum DAO?
    abstract fun calcDao(): CalcDao

    //Criando o padrao singleton
    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context:Context):AppDatabase {
            return if (INSTANCE==null) {
                //synchronized aparentemente administra o controle multi-acesso dessa variavel
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "fitness_tracker"
                    ).build()
                }
                INSTANCE as AppDatabase
            } else {
                INSTANCE as AppDatabase

            }
        }
    }

}