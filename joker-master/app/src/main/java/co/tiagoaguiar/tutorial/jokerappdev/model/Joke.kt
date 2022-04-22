package co.tiagoaguiar.tutorial.jokerappdev.model

import com.google.gson.annotations.SerializedName

data class Joke(
    /*@SerializedName faz o mapeamento das chaves e valores e serve para atribuir as informacoes que vierem no jsonObject para vincular o seu
     valor a variavel que vem depois do serializedName, caso o nome da variavel seja o mesmo que o a
     variavel que vier do JO, entao nao sera necessario usar o SerializedName*/
    @SerializedName ("value") val text: String,
    @SerializedName("icon_url") val iconUrl: String
)
