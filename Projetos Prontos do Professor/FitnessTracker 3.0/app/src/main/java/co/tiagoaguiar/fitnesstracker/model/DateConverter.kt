package co.tiagoaguiar.fitnesstracker.model

import androidx.room.TypeConverter
import java.util.*

//Eh object por ele ira precisar somente de uma funcao para executar
object DateConverter {
    //Os nomes das funcoes tambem tem que ser de acordo com o padrao do Room, ou seja, fromDate |
    //toDate
    @TypeConverter //Notacao usada pelo Room em cima de uma funcao para informa-lo qual tipo sera convertido
    fun toDate(dateLong: Long?):Date? { //usada para pegar o objeto do DB
        return if(dateLong != null) Date(dateLong) else null
    }
    @TypeConverter
    fun fromDate(date:Date?): Long?{ //usada para gravar o obejeto no DB
        return date?.time //retorna um long em milisegundos desde janeiro de 1970
    }

}