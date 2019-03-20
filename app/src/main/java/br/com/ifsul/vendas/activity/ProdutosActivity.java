package br.com.ifsul.vendas.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.ifsul.vendas.R;
import br.com.ifsul.vendas.adapter.ProdutosAdapter;
import br.com.ifsul.vendas.model.Produto;

public class ProdutosActivity extends AppCompatActivity {

    private static final String TAG = "produtosactivity";
    private ListView lvProdutos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produtos);
        lvProdutos = findViewById(R.id.lv_produtos);

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("vendas/produtos");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d(TAG, "Value is: " + dataSnapshot.getValue());

                List<Produto> produtos = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Produto produto = ds.getValue(Produto.class);
                    produtos.add(produto);
                }

                lvProdutos.setAdapter(new ProdutosAdapter(ProdutosActivity.this, produtos));

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}
