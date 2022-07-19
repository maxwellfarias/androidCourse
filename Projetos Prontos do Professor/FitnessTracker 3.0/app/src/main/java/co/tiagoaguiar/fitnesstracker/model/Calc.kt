package co.tiagoaguiar.fitnesstracker.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

//ENTIDADE (sao as tabelas no banco de dados)
//A classe sera usada para especificar quais serao os dados que serao gravados e criar a tabela
// no DB

/*Informa para o Room que essa eh uma classe para o banco, assim, quando o codigo for copi-
lado, o banco de dados sera criado caso nao exista */
@Entity
data class Calc(
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "res") val res: Double, //resposta
    @ColumnInfo(name = "created_date") val createdDate: Date = Date() //Data de criacao
/*
    * @PrimaryKey define a coluna que sera a chave primaria e o parametro autoGenerate incrementa o ID
        automaticamente
    * @ColumnInfo: Define o tipo da coluna de acordo com a variavel e tem como parametro a definicao do
        nome da coluna.
    *  id: Int=0 define o valor inicial do ID
    *  Date = Date() usa como valor inicial a data da criacao do objeto.
*/
)