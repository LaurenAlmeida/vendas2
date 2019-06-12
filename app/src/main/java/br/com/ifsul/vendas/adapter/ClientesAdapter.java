package br.com.ifsul.vendas.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.NumberFormat;
import java.util.List;

import br.com.ifsul.vendas.R;
import br.com.ifsul.vendas.model.Cliente;
import br.com.ifsul.vendas.setup.AppSetup;


public class ClientesAdapter extends ArrayAdapter<Cliente> {

    private final String TAG = "clientesadapter";
    private Context context;
    private List<Cliente> clientes;
    private Bitmap fotoEmBitmap;

    public ClientesAdapter(@NonNull Context context, @NonNull List<Cliente> clientes) {
        super(context, 0, clientes);
        this.context = context;
    }



    @SuppressLint("SetTextI18n")
    @SuppressWarnings("StatementWithEmptyBody")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //declara o objeto que irá segurar os objetos escaneados da view
        final ClientesAdapter.ViewHolder holder;
        //infla a view
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_cliente_adapter, parent, false);
            holder = new ClientesAdapter.ViewHolder(convertView);
            convertView.setTag(holder); //anexa à view o holder
        }else{
            holder = (ClientesAdapter.ViewHolder) convertView.getTag(); //pega da view o holder
        }

        //vincula os dados do objeto de modelo à view
        final Cliente cliente = getItem(position); //devolve o objeto do modelo
        holder.tvNome.setText(cliente.getNome());
        holder.tvSobrenome.setText(cliente.getSobrenome());
        holder.tvCpf.setText(cliente.getCpf());
        holder.pbFotoCliente.setVisibility(ProgressBar.VISIBLE);
        holder.imvFotoClienteAdapter.setImageResource(R.drawable.img_cliente_icon_524x524);
        if(cliente.getUrl_foto().equals("")){
            holder.pbFotoCliente.setVisibility(ProgressBar.INVISIBLE);
        }else{
            //faz o download do servidor
            if(AppSetup.cacheClientes.get(cliente.getKey()) == null){
                StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("images/clientes/" + cliente.getCodigoDeBarras() + ".jpeg");
                final long ONE_MEGABYTE = 1024 * 1024;
                mStorageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        fotoEmBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        holder.imvFotoClienteAdapter.setImageBitmap(fotoEmBitmap);
                        AppSetup.cacheClientes.put(cliente.getKey(), fotoEmBitmap);
                        holder.pbFotoCliente.setVisibility(ProgressBar.INVISIBLE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.d(TAG, "Download da foto do cliente falhou: " + "images/clientes/" + cliente.getCodigoDeBarras() + ".jpeg");
                    }
                });
            }else{
                holder.imvFotoClienteAdapter.setImageBitmap(AppSetup.cacheClientes.get(cliente.getKey()));
                holder.pbFotoCliente.setVisibility(ProgressBar.INVISIBLE);
            }
        }

        return convertView;
    }

    private class ViewHolder {

        final TextView tvNome;
        final TextView tvSobrenome;
        final TextView tvCpf;
        final ImageView imvFotoClienteAdapter;
        final ProgressBar pbFotoCliente;

        public ViewHolder(View view) {
            //mapeia os componentes da UI para vincular os dados do objeto de modelo
            tvNome = view.findViewById(R.id.tvNomeClienteAdapter);
            tvSobrenome = view.findViewById(R.id.tvSobrenomeClienteAdapter);
            tvCpf = view.findViewById(R.id.tvCpfClienteAdapter);
            imvFotoClienteAdapter = view.findViewById(R.id.imvFotoClienteAdapter);
            pbFotoCliente = view.findViewById(R.id.pb_foto_clientes_adapter);
        }
    }



}
