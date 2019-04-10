package br.com.ifsul.vendas.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import java.util.List;

import br.com.ifsul.vendas.R;
import br.com.ifsul.vendas.activity.CarrinhoActivity;
import br.com.ifsul.vendas.model.ItemPedido;

public class CarrinhoAdapter extends ArrayAdapter <ItemPedido> {

    private final Context context;

    public CarrinhoAdapter(CarrinhoActivity carrinhoActivity, List<ItemPedido> carrinho) {
        super(context,0,carrinho);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @androidx.annotation.NonNull ViewGroup parent) {
        final ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_carrinho_adapter,parent,false);


    }
}
