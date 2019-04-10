package br.com.ifsul.vendas.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import br.com.ifsul.vendas.R;
import br.com.ifsul.vendas.adapter.CarrinhoAdapter;
import br.com.ifsul.vendas.setup.AppSetup;

public class CarrinhoActivity extends AppCompatActivity {

    private ListView lv_carrinho;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);
        lv_carrinho = findViewById(R.id.lv_carrinho);
        lv_carrinho.setAdapter(new CarrinhoAdapter(CarrinhoActivity.this, AppSetup.carrinho));


    }
}
