package br.com.ifsul.vendas.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.ifsul.vendas.R;
import br.com.ifsul.vendas.adapter.ClientesAdapter;
import br.com.ifsul.vendas.barcode.BarcodeCaptureActivity;
import br.com.ifsul.vendas.model.Cliente;
import br.com.ifsul.vendas.model.Produto;
import br.com.ifsul.vendas.setup.AppSetup;


public class ClientesActivity extends AppCompatActivity {

    private static final String TAG = "clientesactivity";
    private static final int RC_BARCODE_CAPTURE = 1;
    private ListView lvClientes;
    private List<Cliente> clientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    //Toast.makeText(this, barcode.displayValue, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Barcode read: " + barcode.displayValue);
                    //localiza o produto na lista (ou não)
                    boolean flag = true;
                    int position = 0;
                    for (Cliente cliente : clientes) {
                        if (String.valueOf(cliente.getCodigoDeBarras()).equals(barcode.displayValue)) {
                            flag = false;
                            Intent intent = new Intent(ClientesActivity.this, ClientesActivity.class);
                            intent.putExtra("position", position);
                            startActivity(intent);
                            break;
                        }
                        position++;
                    }
                    if (flag) {
                        Snackbar.make(findViewById(R.id.container_activity_clientes), "codigo de barras não cadastrado", Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "Falha na leitura do código", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "No barcode captured, intent data is null");
                }
            } else {
                Toast.makeText(this, String.format(getString(R.string.barcode_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)), Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
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
         //Toast.makeText(this,"Ler código de barras",Toast.LENGTH_SHORT).show();
             Intent intent = new Intent(ClientesActivity.this, BarcodeCaptureActivity.class);
             intent.putExtra(BarcodeCaptureActivity.AutoFocus, true); //liga a funcionalidade autofoco
             intent.putExtra(BarcodeCaptureActivity.UseFlash, false); //liga a lanterna do dispotivo
             startActivityForResult(intent, RC_BARCODE_CAPTURE);
             break;
         case android.R.id.home:
               finish();
               break;
       }

       return true;
     }




}
