package br.com.ifsul.vendas.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.Date;

import br.com.ifsul.vendas.R;
import br.com.ifsul.vendas.adapter.CarrinhoAdapter;
import br.com.ifsul.vendas.model.Cliente;
import br.com.ifsul.vendas.model.ItemPedido;
import br.com.ifsul.vendas.model.Pedido;
import br.com.ifsul.vendas.setup.AppSetup;


public class CarrinhoActivity extends AppCompatActivity {


    private static final String TAG = "carrinhoActivity";
    private ListView lvCarrinho;
    private TextView tvTotalPedidoCarrinho;
    private Double totalPedido;
    private FirebaseDatabase database  = FirebaseDatabase.getInstance();
    private TextView tvClienteCarrinho;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        lvCarrinho = findViewById(R.id.lv_carrinho);
        tvTotalPedidoCarrinho = findViewById(R.id.tvTotalPedidoCarrinho);

        tvClienteCarrinho = findViewById(R.id.tvClienteCarrinho);
        atualizaView();
        tvClienteCarrinho.setText(AppSetup.cliente.getNome() + " " + AppSetup.cliente.getSobrenome());
        database = FirebaseDatabase.getInstance();

        lvCarrinho.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                excluir(position);
                return true;
            }
        });

        lvCarrinho.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editar(position);
            }
        });


    }


//Criação do Menu lateral (3 pontos)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_carrinho,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuitem_salvar:{
                fazerPedido();
                break;
                }

                case R.id.menuitem_cancelar:{
                    cancelaPedido();
                  break;
                  }

            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

//Funções
    private void atualizaView(){
            lvCarrinho.setAdapter(new CarrinhoAdapter(CarrinhoActivity.this,AppSetup.carrinho));
        Log.d("Texto",AppSetup.carrinho.toString());
        totalPedido = 0.0;
        for (ItemPedido itemPedido : AppSetup.carrinho){
            totalPedido +=itemPedido.getTotalItem();
        }
        tvTotalPedidoCarrinho.setText(NumberFormat.getCurrencyInstance().format(totalPedido));

    }

    private void fazerPedido() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //adiciona um título e uma mensagem
        builder.setTitle(R.string.title_confirma);
        builder.setMessage(R.string.message_confirma_salvar);
        //adiciona os botões
        builder.setPositiveButton(R.string.alertdialog_sim, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (AppSetup.carrinho == null) {
                    Toast.makeText(CarrinhoActivity.this, getString(R.string.carrinho_vazio), Toast.LENGTH_SHORT).show();
                } else {
                    Date data = new Date();

                    Toast.makeText(CarrinhoActivity.this,"Pedido salvo",Toast.LENGTH_SHORT).show();

                    Pedido pedido = new Pedido();
                    pedido.setCliente(AppSetup.cliente);
                    pedido.setItens(AppSetup.carrinho);
                    pedido.setDataCriacao(data);
                    pedido.setDataModificacao(data);
                    pedido.setEstado("aberto");
                    pedido.setFormaDePagamento("debito");
                    pedido.setSituacao(true);
                    pedido.setTotalPedido(totalPedido);
                    DatabaseReference myRef = database.getReference("vendas/pedidos");
                    String key = myRef.push().getKey();
                    pedido.setKey(key);
                    AppSetup.cliente.getPedidos().add(key);
                    myRef.child(key).setValue(pedido);
                    myRef = database.getReference("vendas/clientes").child(AppSetup.cliente.getKey()).child("pedidos");
                    myRef.setValue(AppSetup.cliente.getPedidos());
                    AppSetup.carrinho.clear();
                    AppSetup.cliente = null;
                    startActivity(new Intent(CarrinhoActivity.this, ProdutosActivity.class));

                }
            }
        });
        builder.setNegativeButton(R.string.alertdialog_nao, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }

    private void cancelaPedido() {
      AlertDialog.Builder builder = new AlertDialog.Builder(this);
      builder.setTitle("Atenção");
      final Cliente cliente;
      builder.setMessage(R.string.cancel_pedido);
      builder.setPositiveButton("Sim",new DialogInterface.OnClickListener(){
        @Override
        public void onClick(DialogInterface dialog,int which){
          for (final ItemPedido itemPedido : AppSetup.carrinho){
             DatabaseReference myRef = database.getReference().child("vendas/produtos").child(itemPedido.getProduto().getKey()).child("quantidade");
              myRef.setValue(itemPedido.getQuantidade() + itemPedido.getProduto().getQuantidade());
            myRef.addListenerForSingleValueEvent(new ValueEventListener(){

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
              public void onCancelled(DatabaseError error){
              }
            });
          }

          Toast.makeText(CarrinhoActivity.this,"Pedido cancelado",Toast.LENGTH_SHORT).show();
          AppSetup.carrinho.clear();
          AppSetup.cliente = null;
          startActivity(new Intent(CarrinhoActivity.this, ProdutosActivity.class));
          finish();
        }
      });
      builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialog, int which) {
          Toast.makeText(CarrinhoActivity.this, "Operação cancelada.", Toast.LENGTH_SHORT).show();
         }
});
        builder.show();
}


    private void atualizaEstoque(int position){
        DatabaseReference myRef = database.getReference("vendas/produtos/" + AppSetup.carrinho.get(position).getProduto().getKey() + "/quantidade");
        myRef.setValue(AppSetup.carrinho.get(position).getQuantidade() + AppSetup.carrinho.get(position).getProduto().getQuantidade());

        AppSetup.carrinho.remove(position);
        atualizaView();

        Toast.makeText(CarrinhoActivity.this, "Estoque atualizado com sucesso!", Toast.LENGTH_SHORT).show();
    }




    private void editar(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //adiciona um título e uma mensagem
        builder.setTitle(R.string.title_confirma);
        builder.setMessage(R.string.mensagens_edita);

        //adiciona os botões
        builder.setPositiveButton(R.string.alertdialog_sim, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(CarrinhoActivity.this, ProdutoDetalheActivity.class);
                intent.putExtra("position", AppSetup.carrinho.get(position).getProduto().getIndex());
                startActivity(intent);
                atualizaEstoque(position);
            }
        });
        builder.setNegativeButton(R.string.alertdialog_nao, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }


    private void excluir(final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Atenção");
        builder.setMessage(R.string.confirma_excl);
        builder.setPositiveButton("Sim",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                atualizaEstoque(position);

            }
        });
        builder.setNegativeButton(R.string.alertdialog_nao, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();

    }

}
