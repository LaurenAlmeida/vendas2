package br.com.ifsul.vendas.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

import br.com.ifsul.vendas.R;
import br.com.ifsul.vendas.adapter.CarrinhoAdapter;
import br.com.ifsul.vendas.setup.AppSetup;

public class CarrinhoActivity extends AppCompatActivity {

    private static final String TAG = "carrinhoActivity";
    private ListView lvCarrinhos;
    private TextView tvTotalPedidoCarrinho;
    private Double totalPedido;
    private FirebaseDatabase database;
    private TextView tvClienteCarrinho;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);
        ListView lv_carrinho = findViewById(R.id.lv_carrinho);

        tvTotalPedidoCarrinho =findViewById(R.id.tvTotalPedidoCarrinho);
        tvClienteCarrinho = findViewById(R.id.tvClienteCarrinho);

        tvClienteCarrinho.setText(AppSetup.cliente.getNome() + " " + AppSetup.cliente.getSobrenome());
        database = FirebaseDatabase.getInstance();




    }
}
