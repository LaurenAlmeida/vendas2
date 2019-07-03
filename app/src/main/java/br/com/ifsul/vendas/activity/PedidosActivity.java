package br.com.ifsul.vendas.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.ifsul.vendas.R;
import br.com.ifsul.vendas.adapter.PedidosAdapter;
import br.com.ifsul.vendas.barcode.BarcodeCaptureActivity;
import br.com.ifsul.vendas.model.Cliente;
import br.com.ifsul.vendas.model.Pedido;
import br.com.ifsul.vendas.setup.AppSetup;

public class PedidosActivity extends AppCompatActivity {
    private ListView lv_pedidos;
    private static final String TAG = "pedidosactivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Cliente cliente = (Cliente) getIntent().getExtras().getSerializable("cliente");
        Log.d("data", "" + cliente.getPedidos());

        lv_pedidos = findViewById(R.id.lv_pedidos);
        lv_pedidos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("clientes" + AppSetup.cliente.getKey() + "pedidos");

        final List<Pedido> pedidos = new ArrayList<>();
        for (String keypedido : cliente.getPedidos()) {

                DatabaseReference myRef = database.getReference("vendas/pedidos").child(keypedido);
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("data", String.valueOf(dataSnapshot.getValue(Pedido.class)));
                        Pedido pedido = dataSnapshot.getValue(Pedido.class);
                        pedidos.add(pedido);
                    }


                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });

        }
        lv_pedidos.setAdapter(new PedidosAdapter(PedidosActivity.this, pedidos));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}

