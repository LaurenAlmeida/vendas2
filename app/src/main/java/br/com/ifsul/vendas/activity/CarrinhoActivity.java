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

import static br.com.ifsul.vendas.setup.AppSetup.produtos;

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

        lvCarrinho.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override

        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                excluiItem(position);
                return true;

        } });
<<<<<<< HEAD

        lvCarrinho.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editarItem(position);
            }
        });

    }
=======

        lvCarrinho.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editarItem(position);
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

}//fim atualiza


>>>>>>> be2242f486b86534bec291a5e5c4f02d4fb4d90a

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
                salvarPedido();
                break;
                }

                case R.id.menuitem_cancelar:{
                    cancelaPedido();
                  break;
                  }
        }
        return true;
    }


<<<<<<< HEAD
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
=======

>>>>>>> be2242f486b86534bec291a5e5c4f02d4fb4d90a

    private void salvarPedido() {
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
                    Date dataHoraAtual = new Date();

                    Toast.makeText(CarrinhoActivity.this,"Pedido salvo",Toast.LENGTH_SHORT).show();

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


<<<<<<< HEAD


=======
>>>>>>> be2242f486b86534bec291a5e5c4f02d4fb4d90a
    private void cancelaPedido() {
      AlertDialog.Builder builder = new AlertDialog.Builder(this);
      builder.setTitle("Atenção");
      final Cliente cliente;
      builder.setMessage("Deseja cancelar o pedido?");
      builder.setPositiveButton("Sim",new DialogInterface.OnClickListener(){
        @Override
        public void onClick(DialogInterface dialog,int which){
          for (final ItemPedido itemPedido : AppSetup.carrinho){
            final DatabaseReference myRef = database.getReference().child("vendas/produtos").child(itemPedido.getProduto().getKey()).child("quantidade");
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
        final DatabaseReference myRef = database.getReference("vendas/produtos");
        ItemPedido item = AppSetup.carrinho.get(position);
        myRef.child(item.getProduto().getKey()).child("quantidade").setValue(item.getQuantidade());
        AppSetup.carrinho.remove(position);
        atualizaView();
        }


    private void editarItem(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //adiciona um título e uma mensagem
        builder.setTitle(R.string.title_confirma);
        builder.setMessage(R.string.mensagens_edita);
        //adiciona os botões
        builder.setPositiveButton(R.string.alertdialog_sim, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                atualizaEstoque(position);
                Intent intent = new Intent(CarrinhoActivity.this, ProdutoDetalheActivity.class);
                intent.putExtra("position", AppSetup.produtos.get(position).getIndex());
                startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.alertdialog_nao, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
<<<<<<< HEAD
=======

            }
        });

        builder.show();
    }


    private void excluiItem(final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Atenção");
        final ItemPedido itemPedido = AppSetup.carrinho.get(position);
        final DatabaseReference myRef = database.getReference().child("vendas/produtos").child(itemPedido.getProduto().getKey()).child("quantidade");
        builder.setMessage("Deseja excluir o pedido?");
        builder.setPositiveButton("Sim",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog,int which){
                for (final ItemPedido itemPedido : AppSetup.carrinho){
                    final DatabaseReference myRef = database.getReference().child("vendas/produtos").child(itemPedido.getProduto().getKey()).child("quantidade");
                    myRef.addListenerForSingleValueEvent(new ValueEventListener(){
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Integer estoque = dataSnapshot.getValue(Integer.class);
                            myRef.setValue(estoque + itemPedido.getQuantidade());
                            atualizaEstoque(position);
                        }
                        @Override
                        public void onCancelled(DatabaseError error){
                        }
                    });
                }

                AppSetup.carrinho.remove(position);
                atualizaView();
                Toast.makeText(CarrinhoActivity.this,"Produto excluído",Toast.LENGTH_SHORT).show();

            }
        });

        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(CarrinhoActivity.this, "Operação cancelada.", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }
>>>>>>> be2242f486b86534bec291a5e5c4f02d4fb4d90a

            }
        });

        builder.show();
    }


    private void excluiItem(final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Atenção");
        final ItemPedido itemPedido = AppSetup.carrinho.get(position);
        final DatabaseReference myRef = database.getReference().child("vendas/produtos").child(itemPedido.getProduto().getKey()).child("quantidade");
        builder.setMessage("Deseja excluir o pedido?");
        builder.setPositiveButton("Sim",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog,int which){
                for (final ItemPedido itemPedido : AppSetup.carrinho){
                    final DatabaseReference myRef = database.getReference().child("vendas/produtos").child(itemPedido.getProduto().getKey()).child("quantidade");
                    myRef.addListenerForSingleValueEvent(new ValueEventListener(){
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Integer estoque = dataSnapshot.getValue(Integer.class);
                            myRef.setValue(estoque + itemPedido.getQuantidade());
                            atualizaEstoque(position);
                        }
                        @Override
                        public void onCancelled(DatabaseError error){
                        }
                    });
                }

                AppSetup.carrinho.remove(position);
                atualizaView();
                Toast.makeText(CarrinhoActivity.this,"Produto excluído",Toast.LENGTH_SHORT).show();

            }
        });

        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(CarrinhoActivity.this, "Operação cancelada.", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }



}
