package co.tiagoaguiar.tutorial.jokerappdev

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Como foi informado que essa toolbar eh uma toolbar de acao, entao eh necessario os componentes do
        // drawer do navigationView a fim de controlar e sincronzar essa acao. O codigo abaixo por si so faz
        //Aparecer o nome do aplicativo onde ficara o menu hamburguer
        var toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        //Pegando a referencia da tela principal que eh um drawer_layout
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        //Pegando a referencia da barra de navegacao lateral
        val navView = findViewById<NavigationView>(R.id.nav_view)
        //findNavController eh controlador da naveacao e so esta disponivel porque foi adicionado a
        //dependencia do kotlinExtension. Ele indica quais sao os fragments que serao acessados
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        //Implementa as configuracoes do menu hamburguer
        appBarConfiguration = AppBarConfiguration(
            setOf(
                //Colecao de IDs que sera sincronizada com as lables do menu, eh por meio desse ID que eh captado o
                //label do fragment de modo que esse nome fica ao lado do icone do 'hamburger'
                R.id.nav_home, R.id.nav_joke_day, R.id.nav_about
            ), drawerLayout
        )
        //Sincroniza as configuracoes do menu hambuerger, fazendo com que o menu hamburguer apareca e ao seu lado
        //o nome de respectiva label do fragment (inicio, piada do dia ou sobre)
        setupActionBarWithNavController(navController, appBarConfiguration)
        /*Faz o sincronismo dos itens do menu lateral com os seus respectivos fragments.
        O app:navGraph="@navigation/mobile_navigation" do content_main.xml faz referencia aos fragments
        que serao acessados, os ids desses fragments devem ser os mesmos dos itens do menu da navegacao lateral
        (activity_main_drawer.xml)*/
        navView.setupWithNavController(navController)
    }

    //Implementa a acao de ao clicar no hamburguer, o menu lateral aparece
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        //super.onSupportNavigateUp() chama o comportamento padrao dele
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}