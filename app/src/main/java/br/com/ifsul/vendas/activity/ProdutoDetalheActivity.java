package br.com.ifsul.vendas.activity;

import android.content.Intent;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;

import br.com.ifsul.vendas.R;
import br.com.ifsul.vendas.model.ItemPedido;
import br.com.ifsul.vendas.model.Produto;
import br.com.ifsul.vendas.setup.AppSetup;

public class ProdutoDetalheActivity extends AppCompatActivity {

    private static final String TAG = "produtoDetalheActivity" ;
    private TextView tvNome,tvDescricao,tvValor,tvEstoque;
    private EditText etQuantidade;
    private ImageView imvFoto;
    private Button btVender;
    private Produto produto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto_detalhe);

        tvNome = findViewById(R.id.tvNomeProduto);
        tvDescricao = findViewById(R.id.tvDerscricaoProduto);
        tvValor = findViewById(R.id.tvValorProduto);
        tvEstoque= findViewById(R.id.tvQuantidadeProduto);
        etQuantidade= findViewById(R.id.etQuantidade);
        imvFoto = findViewById(R.id.imvFoto);
        btVender = findViewById(R.id.btComprarProduto);

       Integer position = getIntent().getExtras().getInt("position");
       produto = AppSetup.produtos.get(position);
       Log.d(TAG, "" + produto.equals(AppSetup.produtos.get(position)));

        //bindview
       tvNome.setText(AppSetup.produtos.get(position).getNome());
       tvDescricao.setText("Descrição: " + AppSetup.produtos.get(position).getDescricao());
       tvValor.setText(NumberFormat.getCurrencyInstance().format(AppSetup.produtos.get(position).getValor()));
       if(produto.getUrl_foto() != null){
           //carrega a img
       }

       FirebaseDatabase database = FirebaseDatabase.getInstance();
       DatabaseReference myRef = database.getReference("vendas/produtos/" + produto.getKey() + "/quantidade");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer quantidade = dataSnapshot.getValue(Integer.class);
                tvEstoque.setText(String.format("%s %s", getString(R.string.label_estoque), quantidade.toString()));
                produto.setQuantidade(quantidade);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btVender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AppSetup.cliente == null){
                    startActivity(new Intent(ProdutoDetalheActivity.this, ClientesActivity.class));
                }else{
                    if(etQuantidade.getText().toString().isEmpty()){
                        Toast.makeText(ProdutoDetalheActivity.this, "Digite a quantidade:",Toast.LENGTH_SHORT).show();
                    }else{
                        Integer quantidade = Integer.valueOf(etQuantidade.getText().toString());
                        if(quantidade <= produto.getQuantidade()){
                            //vende
                            ItemPedido item = new ItemPedido();
                            item.setProduto(produto);
                            item.setQuantidade(quantidade);
                            item.setTotalItem(quantidade * produto.getValor());
                            item.setSituacao(true);
                            AppSetup.carrinho.add(item);
                            Toast.makeText(ProdutoDetalheActivity.this, getString(R.string.toast_adicionado_ao_carrinho), Toast.LENGTH_SHORT).show();
                           startActivity(new Intent(ProdutoDetalheActivity.this, CarrinhoActivity.class));
                           finish();

                        }else{
                            Toast.makeText(ProdutoDetalheActivity.this, "Quantidade acima do estoque.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
}
