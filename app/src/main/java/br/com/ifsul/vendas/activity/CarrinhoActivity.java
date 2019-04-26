package br.com.ifsul.vendas.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.Date;

import br.com.ifsul.vendas.R;
import br.com.ifsul.vendas.adapter.CarrinhoAdapter;
import br.com.ifsul.vendas.model.ItemPedido;
import br.com.ifsul.vendas.model.Pedido;
import br.com.ifsul.vendas.setup.AppSetup;

public class CarrinhoActivity extends AppCompatActivity {

    private static final String TAG = "carrinhoActivity";
    private ListView lvCarrinho;
    private TextView tvTotalPedidoCarrinho;
    private Double totalPedido;
    private FirebaseDatabase database;
    private TextView tvClienteCarrinho;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);
        lvCarrinho = findViewById(R.id.lv_carrinho);
        tvTotalPedidoCarrinho =findViewById(R.id.tvTotalPedidoCarrinho);

        tvClienteCarrinho = findViewById(R.id.tvClienteCarrinho);
        atualizaView();
        tvClienteCarrinho.setText(AppSetup.cliente.getNome()+" "+AppSetup.cliente.getSobrenome());
        database = FirebaseDatabase.getInstance();

        lvCarrinho.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             //
            }
        });
    }

private void atualizaView(){
        lvCarrinho.setAdapter(new CarrinhoAdapter(CarrinhoActivity.this,AppSetup.carrinho));
    Log.d("Texto",AppSetup.carrinho.toString());
    totalPedido = 0.0;
    for (ItemPedido itemPedido : AppSetup.carrinho){
        totalPedido +=itemPedido.getTotalItem();
    }
    tvTotalPedidoCarrinho.setText(NumberFormat.getCurrencyInstance().format(totalPedido));

}




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_carrinho,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuitem_salvar:
                Pedido pedido = new Pedido();
                pedido.setCliente(AppSetup.cliente);
                pedido.setItens(AppSetup.carrinho);
                pedido.setDataCriacao(new Date());
                pedido.setDataModificacao(new Date());
                pedido.setEstado("aberto");
                pedido.setFormaDePagamento("debito");
                pedido.setSituacao(true);
                pedido.setTotalPedido(totalPedido);
                DatabaseReference myRef = database.getReference("vendas/pedidos");
                String key = myRef.push().getKey();
                pedido.setKey(key);
                myRef.child(key).setValue(pedido);
                Toast.makeText(this,"Pedido salvo",Toast.LENGTH_LONG).show();
                break;
        }

        case R.id.menuitem_cancelar:
            cancelaPedido();
          break;
    }



}
    private void cancelaPedido() {
    }


