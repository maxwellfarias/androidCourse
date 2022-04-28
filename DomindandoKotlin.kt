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
    fromArray.contains(3) //Verifica se contem o elemento 3
    2 in fromArray //Verifica se tem o elemento 2 nessa colecao
    fromArray.add(5)
    fromArray.remove(1)
    fromArray

//FORMAS DE USAR FUNCAO LAMBDA
    var lambda: () -> Unit = {
        println("Hi Lambda")
    }
    lambda()

    var fLambda: (Int) -> Unit = { a: Int -> println("Teste¹ $a") }
    fLambda(10)

    fLambda = {println("Teste² $it")}
    fLambda(11)

    fLambda = { a:Int -> Unit
        println("Teste³ $a")}
    fLambda(12)

    //Usando lambdas em funcoes de esperam uma outra funcao como argumento
    fun lambdaCalc (x:Int, y:Int,  function: (Int, Int) -> Int) {
        println(function(x,y))
    }
    lambdaCalc(10, 5) {x:Int, y:Int -> //Pode colocar o tipo da variavel ou omiti-la como no caso abaixo
        x/y //Nao eh necessario dar um retorno, porque eh implicito que uma funcao de lambda espera um retorno
    }
    lambdaCalc(10, 5) {x, y ->
        x*y
    }
    lambdaCalc(10, 5, Int::plus)

    //Funcao dentro de uma variavel passada como argumento de outra funcao
    var sumLambda: (Int, Int) -> Int =  { x, y -> x+y}
    lambdaCalc(10,5, sumLambda)
    fun sumLambda2 (x:Int, y:Int)  = x+y
    lambdaCalc(10,5, ::sumLambda2) //Ao passar uma funcao como parametro eh necessario adicionar '::' antes do nome da funcao

    //Exemplos de lambdas prontas do Kotlin
    val listaDeTextos = listOf("Ola", "Mundo", "Hello", "World")
    println(listaDeTextos.sorted()) //Ordena os elementos por ordem alfabetica
    println(listaDeTextos.sortedWith(compareBy{
        it.length //O labda eh expresso em uma expressao de comparacao que nesse caso eh comparado o tamanho dos elementos de acordo com a quantidade
        //de letras de cada elemento da lista
    }))
    val arrayLambda = intArrayOf(1,2,3,4,5,6,7,8,9)
    arrayLambda.forEach {
       println(it*it)
    }
    arrayLambda.filter {
        it%2==0 //Retorna uma lista contendo os elementos que atendem a condicao da funcao labda
    }
    arrayLambda.map{
        it*2 // Retorna uma nova lista depois que cada elemento da lista anterior foi modificado pela expressao escolhida, nesse caso cada elemento da lista
        //sera multiplicado por 2   
    }


//Classes e propriedades de acesso
    //class NomeDaClasse (variaveis do construtor) {...Segue um modelo semelhante ao java}
    class User (var name:String, var lastName:String, var password:Int) {
       val fullName
            get() = "$name $lastName" //Esse trecho de codigo aparetemente atribui valor a ultima variavel declarada


    }
    var joao = User("Joao", "Farias", 21345)
    println(joao.fullName)

    var joao2 = joao
    joao.password = 123
    println(joao.password)
    println(joao2.password)
    println(joao === joao2) // === verifica se as duas variavel estao apontando para o mesmo endereco de memoria

//DATA CLASS
    //Ao atribuir uma classe como date class, essa classe passa a prossuir propriedades de setter, getter, hashcode, equals, toString
    //Ou seja, tudo o que era construido na "unha" no java, com o kotlin esse procedimento ja eh incluido sem trabalho
   data class Product (var name: String, var price: Double ) {


            * Todos os codigos abaixo passam a fazer parte da classe quando se atribui a classe como um dataClass

            override fun equals(other: Any?): Boolean {
            if (other === this) return true
            else if (other == null) return false
            else if (javaClass != other.javaClass) return false

            var obj = other as Product //converte o objeto other para o Product e o atribui para a variavel obj
            //Verifica se os atributor de other sao iguais aos atributos do construtor Product
            if (name != obj.name) return false
            else if (price != obj.price) return false
            return true
        }

        override fun toString(): String {
            return "Product is (Name: $name, Price: $price)"
        }
    }
    var iPhone = Product("iPhone", 2000.0)
    var android = Product("Android", 3000.0)
    //Quando da um println em um objeto, eh retornado o conteudo da funcao toString
    println(iPhone)
    iPhone == android
    iPhone.equals(android)

    //Tirando propriedades de dentro de um objeto
    //Havera uma unica instancia durante toda a vida do projeto usando o padrao singleton
    var (name, price) = iPhone
    println("Nome: $name, Price: $price")

    object Products {
        var allProducts = mutableListOf<Product>()
        fun addProduct(product: Product) {
            allProducts.add(product)
        }
    }
    Products.addProduct(iPhone)
    Products.addProduct(android)
    Products.allProducts.forEach(){
        println(it.toString())
    }

    //O object pode ser usado em um padrao de chave e valor como o Json
    object Keys {
        const val ID = "id"
        const val NAME = "ALTER_BRIDGE"
    }

//COMPANION
    //private constructor -> Faz com que o construtor seja privado para que niguem instancie essa classe
    //Contudo, o companion object permite que instancias seja criadas dentro da classe
    class Button private constructor (val id: Int, color:Int ) {
        //Tudo o que esta dentro do companion object eh compartilhado por todos os objetos desse contexto, ou seja
        //Eh criado um "bloco estatico".
        companion object { //Para que o companion object fique disponivel no java, eh necessario dar um nome: companion object NomeDoObject {...}
            var currentId = 0
            fun newButton (color: Int): Button {
                currentId ++
                return (Button(currentId, color))
            }
        }
    }

    val blue = Button.newButton(255)
    println(blue.id)
    val yellow = Button.newButton(0)
    println(yellow.id)

//INTERFACES
    interface OnClickListener {
        fun onClick ()
        fun onLongClick()
    }
    //Criando um objeto de inteface anonima
    /*No java, esta seria a forma de se criar um objeto de interface anonima:
        OnClickListener listener = new OnClickListener () {
            //Implementada os metodos...
        }
        Essa foi a forma que me fez ficar um pouco confuso quando vi isso em java achando que estava sendo criado uma instancia de uma interface
        */
    //Modo Kotlin de se criar um objeto de interface anonima
    var listener = object: OnClickListener {
        override fun onClick() {/*...*/}

        override fun onLongClick(){/*...*/}
    }
    //Bloco de construcao init
    class Screen {
        var top = 0
        var left = 0
        var bottom: Int
        var right:Int

        //Bloco init eh inicializado quando a classe for instanciada
        init {
            bottom = 10
            right = 10
        }

    }

//SETTERS AND GETTERS DIRETO NA VARIAVEL
    class Converter (var real:Double) {
        var dolar: Double
            get () { //Sempre que a variavel dolar for acessada, sera executada essas instrucoes
                return real / 3.5
            }
            set (valor) { //Sempre que for atribuido uma valor para dolar, sera executada essas instrucoes
                dolar = valor * 3.5
            }
    }
    val converter = Converter(3.5)
    println(converter.dolar)
    converter.dolar = 10.0
    println(converter.real)

    class Level (val id:Int, var boss:String) {
        companion object {
            @JvmStatic //Com @JvmStatic , o bloco fica disponivel como estatico para o java
            var higherLevel = 10;
        }
    }
    val chefao = Level(1, "Sefiroty")
    Level.higherLevel

//DELEGATES E OBSERVERS
    class Achivements (val id:Int) {
        companion object {
            @JvmStatic
            var level = 1
        }
        /*Informando que a variavel vai realizar eventos sempre que mudar de estado
        * Esse procedimento seria feito nesse exemplo hipotetico como uma compra de uma conquista no meu jogo e automaticamente
        * subir de nivel.
        * Quando a variavel buy tiver o seu valor modificado para true, o bloco de codigo que atribui do id para a variavel level
        * sera executado. Esse eh o principio de uma programacao reativa e alguma pessoas chamam esse procedimento de propriedades
        * delegadas ou inicializacoes e mudancas de estados atrasada    .
        * */
        var buy:Boolean by Delegates.observable(false) {
            //O valor inicial dessa variavel sera falso. Nao sera usado o valor inicial no proximo codigo ->
            _,old, new -> //old - valor velho; new valor novo. Nesse caso sera de false para true
            //Verifica se eh um valor novo e se o id eh maior que o level
            if (new && id > level) {
                level = id
            }
            println("$old - $new")

        }
    }

    val act1 = Achivements (1)
    val act2 = Achivements (2)
    println(Achivements.level)
    act2.buy = true
    println(Achivements.level)

    class DB {
        companion object {
            const val maxUser = 10
        }
        var current: Int by Delegates.vetoable(0) {
            _,_,new ->
            new <= maxUser /*a variavel current tera somente o seu valor alterado quando o novo valor atribuido (new)
            for menor ou igual ao maxUser (ou seja 10), caso o valor atribuido seja maior, entao sera atribuido o valor
            inicial 0. */
        }
    }
    val db = DB ()
    db.current = 20
    println(db.current)

 */

//INICIALIZACAO ATRASADA COM LAZY
    //O lazy eh usado fazer com que a variavel somente inicializado quando for ser usada, assim economiza-se memoria
    class Window (val scale:Int) {
        val height: Int by lazy {
            400 * scale
        }
        val width:Int
            get() = height / 16/ 9
    }
    val w = Window(2)
    println(w.height)

    //Outra forma de se iniciar uma variavel de forma atrasada eh usar o lateinit
    class Color {
        lateinit var value:String
    }

//EXTENSIONS
    //faz com que seja adicionado um novo atributo coloca-lo direto no corpo da classe. No exemplo, sera colocado
    //dentro da classe Window o atributo
    val Window.size: Int
        get() = height * width

    val window1 = Window(2)
    println(window1.size)

showWidth()



//TRABALHANDO COM CONSTRUTORES
fun main() {
    Home(6).apply {
        println("-------Home(6)--------")
        showColor()
        showNumber()
    }

    Home(10, "Blue").apply {
        println("-------Home(10, \"Blue\")--------")
        showColor()
        showNumber()
    }

    Room(20, "Red", 50, 100).apply{
        showColor()
        showNumber()
        showHeight()
        showWidth()
    }
}
open class Home (var number: Int) {
    var color: String = ""
//this passa o valor para o construtor primario
    constructor(number: Int, color: String):this(number) {
        this.color = color
    }


    fun showNumber() = println(number)
    fun showColor() = println(color)
}

class Room: Home {
    var height: Int = 0
    var width:Int = 0
    //super para os valores para o contrutor da Home (number) ou (numer, color)
    constructor(number: Int, color: String, height: Int): super(number, color){
        this.height = height
    }
    //this passa os valores para o constutor secundario
    constructor(number: Int, color: String, height: Int, width: Int):this(number, color, height) {
        this.width = width
    }

    /*Nao eh possivel colocar um contrutor primario em Room, pois nessa eu teria que usar o this e o
    * super no mesmo construtor, o que nao eh possivel. Entao basta fazer outros construtores diferentes
    * do primario*/

    fun showHeight() = println(height)
    fun showWidth() = println(width)
}


//CLASSES ABSTRATAS

fun main() {
    val bope = Bope()
    Test().apply {
        test(bope)
    }
}
class Test {
    //Espera um objeto do tipo Police, mas sera passado do tipo Bope (que descende do Police) 
    fun test(police: Police) {
        police.lie()
        police.shot()
    }
}
//Eh necessario a open keyword para dizer quais funcoes e variaveis irao descender
abstract class Police {
    abstract fun shot()
    open fun lie() = println("Police lay")
}

class Bope : Police() {
    override fun shot() = println("Bope atirou")
    override fun lie() = println("Police lay")
    fun bope() = println("Funcao Bope")
}
