package br.com.ifsul.vendas.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

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
import br.com.ifsul.vendas.setup.AppSetup;


public class ClientesActivity extends AppCompatActivity {

    private static final String TAG = "clientesactivity";
    private ListView lvClientes;
    private List<Cliente> clientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);

        lvClientes = findViewById(R.id.lv_clientes);
        lvClientes.setOnItemClickListener(new AdapterView.OnItemClickListener(){
          @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            selecionarCliente(position);
          }
        });
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("vendas/clientes");

        // Read from the database
        myRef.orderByChild("nome").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d(TAG, "Value is: " + dataSnapshot.getValue());

                clientes = new ArrayList<>();
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

          private void selecionarCliente(int position){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //Título e mensagem
            builder.setTitle(R.string.title_selecionar_cliente);
            final Cliente cliente = clientes.get(position);
            builder.setMessage(getString(R.string.message_nome_cliente) + " "+cliente.getNome() + "  " + cliente.getSobrenome() + "?");
            //Botões
            builder.setPositiveButton(R.string.alertdialog_sim,new DialogInterface.OnClickListener(){
              @Override
              public void onClick(DialogInterface dialog,int which){
                AppSetup.cliente = cliente;
                Toast.makeText(ClientesActivity.this,getString(R.string.toast_cliente_selecionado),Toast.LENGTH_SHORT).show();
                finish();
              }
            });

            builder.setNegativeButton(R.string.alertdialog_nao,new DialogInterface.OnClickListener(){
               @Override
               public void onClick(DialogInterface dialog,int which){
                 Snackbar.make(findViewById(R.id.container_activity_clientes),R.string.snack_operacao_cancelada,Snackbar.LENGTH_LONG).show();
               }
            });
              builder.show();
          }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_clientes, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.menuitem_pesquisar).getActionView();
        searchView.setQueryHint(getString(R.string.hint_nome_searchview));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Cliente> clientesTemp = new ArrayList<>();
                for (Cliente cliente : clientes) {
                    if (cliente.getNome().contains(newText)) {
                        clientesTemp.add(cliente);
                        return true;
                    }
                }

                lvClientes.setAdapter(new ClientesAdapter(ClientesActivity.this, clientesTemp));

                return true;
            }
        });

        return true;
    }

    @Override
     public boolean onOptionsItemSelected(MenuItem item){
       switch(item.getItemId()){
         case R.id.menuitem_barcode:
         Toast.makeText(this,"Ler código de barras",Toast.LENGTH_SHORT).show();
         break;
       }
       return true;
     }


}
