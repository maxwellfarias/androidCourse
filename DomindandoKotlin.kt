import kotlin.math.abs



/*
PONTOS PARA REVISAR:


    //Passando funcoes genericas como parametro
*/



"%.4f".format(2.54324)
val constant = 10 // declarando uma constante
//const val contant2 = 100 //Define uma constante fora do bloco da funcao de modo que possa ser usado em outras partes do sistema
val pi: Float = 3.14159f //Realiza o cast para float, por padrao o numero aparentemente seria lido como double
1_000_000 //Eh possivel separar numeros grandes com '_' para facilitar a vizualizacao
var text = "Eh uma string"
val char = 99
char.toChar() //Convertendo numeros em caracteres
"Text com a letra ${char.toChar()}" //Adicionando variaveis e expressoes dentro do texto
val texts = """
    Adionando multiplas linhas de
    texto em uma constante
""".trimIndent() //trimIndent faz com que o texto tenha os espacos em branco a mais

//PARES DE VALORES
    val coordinates = Pair(1.4, 6) //Pair retorna uma variavel que armazena uma chave de valores
    val coord = 1.4 to 6 //Tem o mesmo sentido da linha acima, mas funciona de maneira mais enxuta
    coord.first //metodo fisrt retorna o primeiro valor da chave
    coord.second //retorna o segundo valor da chave
    val (x, y) = coord //Atribui as os valores da chave para a variavel x e y
    val (x1, _) = coord //O '_' faz com o valor da segunda chave seja desconsiderado
    val referencias3D = Triple(1, 2, 3) //Triple retorna uma variavel que armazena uma chave de valores

//VARIAVEIS E SEUS TAMANHOS
    val byte: Byte = 1 // 1 byte
    val short: Short = 1 //1B
    val integer: Int = 1 //4B
    val long: Long = 1 //8B
    val float: Float = 1.0f //4B
    val double: Double = 1.0 //8B

//ANY, UNIT e NOTHING
val qualquerValor: Any = "String" //Pode receber qualquer tipo de valor
    //Unit
    fun sum (x: Int, y: Int):Unit { //Tem a mesma funcao que o Void em java, pode ser omitida que o Unit sera subentendido
    //Qualquer codigo
}
    //Nothing tem a mesma aplicacao que o Unit, mas eh colocada em funcoes que 'nunca terminam', como em loops infinitos

//CONDICOES BOOLEANAS
    //Seguem os mesmos padroes do Java
    "barco" < "casa" //Quando se compara textos, eh feita uma comparacao que considera ordem alfabetica

//IF e ELSE Inline
    val a = 10
    val b = 20
    if (a < b) b else a //if e else em uma unica linha

    //IF pode retorna uma determinado valor
    val bankAccount = 1200
    val product = if (bankAccount > 1900) "iPhone"
    else if (bankAccount > 1500) "Motorola"
    else if (bankAccount > 1000) "Sansung"
    else "Sem dinheiro"
    product
    //WHEN
    val product2 = when {
        bankAccount > 1900 -> "iPhone"
        bankAccount > 1500 -> "Motorola"
        bankAccount > 1000 -> "Sansung"
        else -> "Sem dinheiro"
    }
    product2

//INTERVALO RANGE
    //Cria um intervalo de valores
    val intervalo = 1..5
    print(intervalo)
    val decrease = 5 downTo 0 //Decresce
    print(decrease)
    val rangeAte = 1 until 5 //Exclui o ultimo valor do intervalo
    print (rangeAte)

//LOOPS
    for (x in intervalo) {
        println(x)
    }
    intervalo.forEach {
        println(it) //it eh a variavel interna que retorna o numero de cada interacao do intervalo
    }
    repeat(10) { //repeat fara um loop com o numero de vezes determinado. Tambem eh possivel acessar a variavel interna 'it'
        println(it)
    }
    for (x in 0..10 step 2) { // step eh colocado depois do range e pode ser especificado o 'pulo'
        if (x>3 && x<7) continue //continue faz com que seja passado para o proximo loop
        if (x in 4..6) continue //Essa linha quer dizer a mesma coisa da linha acima
        println(x)
    }
    //Labels
   col@ for (x in 0..2) {
        row@ for (y in 0..2) {
            if (x==y) continue@col
            println("X: ${x}, Y: ${y}")
        }
    }
    dig@ for (d in 0..2) { //nome@ eh uma label que identifica o trecho do codigo, quando eh chamado 'continue@dig' o loop eh direcionado para dig@ do codigo
        col@ for (x in 0..2) {
            row@ for (y in 0..2) {
                if (x ==1 && y == 1) continue@dig
                println("D: ${d}, X: ${x}, Y: ${y}")
            }
        }
    }

//WHEN
    val user = "motorola"
    when (user) {
        "samsung", "motorola" -> println("Android")
        else -> println("iPhone")
    }

    val number = 3;
    val textNumber =  when (number) {
        1 -> "one"
        2 -> "two"
        4 -> "four"
        else -> "Valor desconhecido"
    }
    println(textNumber)

    val hour = 9
    val time = when(hour) {
        in 6..12 -> "Manha"
        in 13..17 -> "Tarde"
        in 18..24 -> "Noite"
        else -> "Nao definido"
    }
    println(time)

//FUNCOES
    fun discount (value:Float, discount:Float = 10.00f):Float { //Pode ser colocado um value padrao como parametro da funcao
        return (value - (value*discount/100))
    }
    println(discount(1000.toFloat(), 20f))
    println(discount(5000f))
    //Retornando um par de valores de uma funcao
    fun multiplyDivide (x:Int, y:Int): Pair<Int, Int> {
        return Pair(x*y, x/y)
    }
    println(multiplyDivide(10, 5))

    //Funcoes que tem o retorno em uma unica linha de codigo podem ser simplificadas
    fun multiplyDivide2 (x:Int, y:Int) = Pair(x*y, x/y)
    println(multiplyDivide2(10, 5))

    //Variavel recendo uma funcao
    var newDiscount = ::discount
    println(newDiscount(1000f, 50f))

    //Passando funcoes genericas como parametro
    fun add (x:Int, y:Int) = x + y
    fun sub (x:Int, y:Int) = x-y
    fun calculoGenerico (funtion: (Int, Int) -> Int, x:Int, y:Int) {
        println (funtion (x,y))
    }
    calculoGenerico(::add, 10, 5)
    calculoGenerico(::sub, 10, 2)
//EVITANDO NULL POINTER
    //Declarando variaveis que podem receber null -> Basta colocar o sinal de interrogacao apos a declarao do tipo de variavel
    var errorCode:Int? = null

    var textTest:String? = "Hello"
        //!! Eh usando para desenvelopar a variavel
    var msg:String = ""
        //Como a variavel text pode ser null, eh necessario antes de usa-la, verificar se o valor eh null:
    if(textTest != null)  msg = "${textTest} World" //Verificacao feita de maneira semelhante o que era feita em Java
    println("$msg !!!")

        //Jeito Kotlin de verificar se uma variavel eh null:
    var text2:String? = null
    println(text2?.length?.plus(0)) """O sinal de interrogacao nesse caso faz com que as funcoes somente sejam executadas se os valores forem diferentes
        de null e caso sejam null nao dara um erro no programa"""

    //Ao usar o let, o bloco de codigo somente sera executado se a variavel for diferente de nulla, semelhante a verificacao por meio do if
    text2?.let {
        println(it.length.plus(0)) //it seria um variavel interna que faz referencia ao contexto text2
    }
    //Eh possivel fazer com que um valor padrao seja retornado a nossa variavel seja nulla. Basta usar o operador elvis-> ' ?: '
    println(text2?.length?: 10) //Caso text2 seja nullo, o valor retornado sera 10

//ARRAYS DE LIST
    //Arrays
val numbers = arrayOf(1,2,3)
numbers.forEach{println(it)}
numbers[2] = 10
numbers.forEach{println(it)}

    //Usando os tipos primitivos de arrays consomem menos memoria do que os tipos gerericos de arrays
    val numbersPrimitives = intArrayOf(1,2,3,4)

    //Listas
    var texts = listOf<String>("Ola", "mundo", "list") //<String> poderia ser omitida e o prgrama iria inferir que seria uma lista de strings
    var textImplicit = listOf("Lista", "de", "Strings", "implicita")
    println(textImplicit[0])
    println(textImplicit)

    //Listas mutaveis
    var listMutable = mutableListOf("Primeira", "Lista", "Mutavel")
    listMutable.add("Primeira adicao")
    listMutable.add("Segunda adicao")
    listMutable += "Terceira adicao"
    listMutable.isEmpty()
    listMutable.size
    listMutable.first()
    listMutable.last()
    listMutable.joinToString(":") //Gera uma String com todos os elementos juntos por uma determinada string
    listMutable.slice(1..3) //Gera uma substring de acordo com os seu indice
    listMutable.contains("Mutavel")
    listMutable.remove("Primeira")
    listMutable.sort() //Ordena por ordem alfabetica

    for (item in listMutable) println(item)
    for ((index, item) in listMutable.withIndex()) println("$index, $item") //withIndex retorna um par de index e valor

    //Listas com valores nullo
    var ListaDeNullos:List<Int?> = listOf(1,2,3, null)
    var ListaDeNulos2:List<Int>? = null

    //Map eh um conjunto de chaves e valores, ele pode ser imutavel ou mutavel
        //Imutavel
    val products = mapOf(
        "Apple" to "IOS",
        "Motorola" to "Android"
    )

    products["Apple"]
    products.isEmpty()
    products.size

        //Mutavel
    var users = mutableMapOf(
        "user1" to "Maxwell",
        "user2" to "Danielle"
    )
    users.put("user3", "Eva") //Adicionando elemento
    users.put("user2",  "Mahiara") // subsecreve o valor que esta contido na chave referenciada
    //Casa chave e elemento desse mapa possui um hashcode, devido a isso foi possivel sobreescrever os elementos devido ao seu identificador unico
        for(hashCodeDaChave in users.keys) println(hashCodeDaChave.hashCode())

    val user4 = mapOf("user4" to "Liko")
    users += user4 //Outra maneira de adicionar elemento
    users
    users.remove("user1") //remove de acordo com a chave
    for (key in users.keys) println(key) // Retorna uma 'lista' de chaves

    //SET eh uma colecao semelhante a uma list, mas que nao aceita elementos repetidos
    val otherNames = setOf("Maxwell", "Danielle", "Eva", "Maxwell")
    println(otherNames)
    val array = arrayOf(1,2,3,3,4)
    var fromArray = mutableSetOf(*array) //Eh necessário colocar * para pegar o ponteiro do array?
    fromArray.contains(3)
    2 in fromArray
    fromArray.add(5)
    fromArray.remove(1)
    fromArray





