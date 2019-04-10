package br.com.ifsul.vendas.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;

import br.com.ifsul.vendas.R;
import br.com.ifsul.vendas.model.Produto;
import br.com.ifsul.vendas.setup.AppSetup;

public class ProdutoDetalheActivity extends AppCompatActivity {

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
       tvValor.setText(NumberFormat.getCurrencyInstance().format(AppSetup.produto.get(position).getValor()));
       if(produto.getUrl_foto() != null){
           //carrega a img
       }
       FirebaseDatabase database = FirebaseDatabase.getInstance();
       DatabaseReference myRef = database.getReference("vendas/produtos/" + produto.getKey() + "/quantidade");

       myRef.addValueEventListener(new valueEventListener(){
         @Override
         public void onDataChange (@NonNull DataSnapshot dataSnapshot){
           Integer quantidade = dataSnapshot.getValue(Integer.class);
           tvEstoque.setText(String.format("%s %s",))
         }
       });
       tvEstoque.setText(String.format("%s %s",getString(R.string.label_estoque), produto.getQuantidade().toString()));


    }
}
