package br.com.ifsul.vendas.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.google.android.gms.common.api.Api;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.ifsul.vendas.R;
import br.com.ifsul.vendas.adapter.ClientesAdapter;
import br.com.ifsul.vendas.model.Cliente;


public class ClientesActivity extends AppCompatActivity {

    private static final String TAG = "clientesactivity";
    private ListView lvClientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);
        lvClientes = findViewById(R.id.lv_clientes);

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("vendas/clientes");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d(TAG, "Value is: " + dataSnapshot.getValue());

                List<Cliente> clientes = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Cliente cliente = ds.getValue(Cliente.class);
                    clientes.add(cliente);
                }

                lvClientes.setAdapter(new ClientesAdapter(ClientesActivity.this, clientes));

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}
