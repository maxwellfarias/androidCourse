package co.tiagoaguiar.fitnesstracker

import android.app.Application
import co.tiagoaguiar.fitnesstracker.model.AppDatabase

/*
A classe Application eh executada sempre que o app comecar, como o App eh sua filha, essa classe
tera o mesmo comportamento. Assim eh possivel acessar a variavel db que eh uma instancia da classe
que manipula do DB desde o inicio do app.
Obs.: Eh necessario adicionar a classe App no android manifest:

        android:name=".App"

*/
class App : Application() {

    lateinit var db: AppDatabase

    override fun onCreate() {
        super.onCreate()
        db = AppDatabase.getDatabase(this)
    }

}