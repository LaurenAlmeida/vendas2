package br.com.ifsul.vendas.setup;

import android.graphics.Bitmap;

import java.nio.DoubleBuffer;
import java.util.AbstractSequentialList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.ifsul.vendas.model.Cliente;
import br.com.ifsul.vendas.model.ItemPedido;
import br.com.ifsul.vendas.model.Pedido;
import br.com.ifsul.vendas.model.Produto;

public class AppSetup {
    public static List<Produto> produtos = new ArrayList<>();
    public static Cliente cliente = null;
    public static List<ItemPedido> carrinho = new ArrayList<>();
    public static Pedido pedido = null;


    public static Map <String, Bitmap> cacheProdutos;
}
