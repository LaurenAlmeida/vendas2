package br.com.ifsul.vendas.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;

import br.com.ifsul.vendas.R;
import br.com.ifsul.vendas.model.Cliente;



public class ClientesAdapter extends ArrayAdapter<Cliente> {

    private Context context;
    private List<Cliente> clientes;

    public ClientesAdapter(@NonNull Context context, @NonNull List<Cliente> clientes) {
        super(context, 0, clientes);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //Devolve o objeto do modelo
        Cliente cliente = getItem(position);

        //infla a view
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_cliente_adapter, parent, false);
        }

        //mapeia os componentes da UI para vincular os dados do objeto de modelo
        TextView tvNome = convertView.findViewById(R.id.tvNomeClienteAdapter);
        TextView tvSobrenome = convertView.findViewById(R.id.tvSobrenomeClienteAdapter);
        TextView tvCpf = convertView.findViewById(R.id.tvCpfClienteAdapter);
        TextView tvCodigoDeBarras = convertView.findViewById(R.id.tvCodigoClienteAdapter);
        ImageView imvFoto = convertView.findViewById(R.id.imvFotoClienteAdapter);

        //vincula os dados do objeto de modelo à view
        tvNome.setText(cliente.getNome());
       tvSobrenome.setText(cliente.getSobrenome());
        tvCodigoDeBarras.setText(cliente.getCodigoDeBarras().toString());
        tvCpf.setText(cliente.getCpf());
        if(cliente.getUrl_foto().equals("")){
            imvFoto.setImageResource(R.drawable.img_cliente_icon_524x524);
        }else{
            ///img do storage
        }


        return convertView;
    }
}
