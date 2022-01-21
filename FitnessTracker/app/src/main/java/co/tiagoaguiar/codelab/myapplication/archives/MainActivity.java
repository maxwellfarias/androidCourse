package co.tiagoaguiar.codelab.myapplication.archives;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import co.tiagoaguiar.codelab.myapplication.R;

public class MainActivity extends AppCompatActivity {

	private RecyclerView rvMain;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

	rvMain = findViewById(R.id.rv_main);

	//Layout Grid -> Especifica o context e o numero de colunas
	rvMain.setLayoutManager(new GridLayoutManager(this,2));


//ADIONANDO UMA LISTA COM AS INFORMAÇÕES QUE SERÃO MODIFICADAS NO ITEM
		//Foi criado a classe MainItem com todos os atributos que seriam modificados na view e feito uma lista de objetos
	List<MainItem> mainItems = new ArrayList<>();
	mainItems.add(new MainItem(1,R.string.imc, R.drawable.ic_baseline_wb_sunny_24, Color.YELLOW));
	mainItems.add(new MainItem(2,R.string.tmb, R.drawable.ic_baseline_visibility_24, Color.GREEN));




		/*
		 * MainAdapter: Cria um objeto da classe que controla a RecycleWiew
		 * Eh o codigo logico que vai fazer toda a conexao do codigo  da View que voce precisa, da quantidade de intens que
		 * se precisa para criar a lista e qual eh o layout xml que ele tem que "inflar"
		 * Foi colocado como parametro do construtor uma lista de objetos MainItem
		 */
	MainItemAdapter adapter = new MainItemAdapter (mainItems);




	/* Explicacao de uma interface recebendo uma objeto:
	O que aconteceu no código acima foi criação um objeto anônimo e NÃO UMA INSTÂNCIA da interface OnItemClickListener,
	o que consequentemente geraria um erro de compilação.	Dessa forma, interfaces podem funcionar como variáveis que podem receber objetos
	que implementam as suas funções, nesse caso recebeu um objeto anônimo
*/
//A implementacao acima faz com que atraves do ID, seja feita uma decisao sobre qual eh a nova janela que sera chamada.

		adapter.setListener(id -> {
			switch (id){
				case 1:
					startActivity(new Intent(MainActivity.this, ImcActivity.class));
					break;
				case 2:
					startActivity(new Intent(MainActivity.this, TmbActivity.class));
					break;
			}
		});

		//Metopo para configurar a recycleView
		rvMain.setAdapter(adapter);

	}

//MainItemAdapter.MainViewHolder -> O ponto faz com que seja possivel acessar a classe MainViewHolder que esta dentro da MainItemAdapter
private class MainItemAdapter extends RecyclerView.Adapter<MainItemAdapter.MainViewHolder> {
	private List<MainItem> mainItems;
	private OnItemClickListener listener;
	public MainItemAdapter (List<MainItem> mainItems) {
		this.mainItems = mainItems;
	}

	public void setListener(OnItemClickListener listener) {
		this.listener = listener;
	}

	@NonNull
	@Override
	public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new MainViewHolder(getLayoutInflater().inflate(R.layout.main_item,parent, false));
	}

	@Override
	public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
		holder.bind (mainItems.get(position));
	}

	@Override
	public int getItemCount() {
		return mainItems.size();
	}

	private class MainViewHolder extends RecyclerView.ViewHolder {

		public MainViewHolder(@NonNull View itemView) {
			super(itemView);
		}

		//A funcao faz as modificacoes nos itens da da RecycleView e adiciona um evento de toach no botao
		public void bind (MainItem item) {
			TextView textItem = itemView.findViewById(R.id.item_txt_name);
			ImageView imgItem = itemView.findViewById(R.id.item_img_icon);
			LinearLayout btnImc = itemView.findViewById(R.id.btn_imc);

			// adiciona um evento de toach no botao, setOnClickListener espera um objeto que implemente a funcao onClick, nesse caso ja
			// foi passado o objeto listener com a ja funcao implementada, passando dessa forma apenas o argumento da funcao ID
			btnImc.setOnClickListener(v -> {
				listener.onCLick(item.getId());
			});


			textItem.setText(item.getTextStringId());
			imgItem.setImageResource(item.getDrawable());
			btnImc.setBackgroundColor(item.getColor());


		}
	}
}


}