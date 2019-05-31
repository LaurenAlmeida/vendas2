package br.com.ifsul.vendas.setup;

import android.graphics.Bitmap;

import com.google.firebase.auth.FirebaseAuth;

import java.nio.DoubleBuffer;
import java.util.AbstractSequentialList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.ifsul.vendas.model.Cliente;
import br.com.ifsul.vendas.model.ItemPedido;
import br.com.ifsul.vendas.model.Pedido;
import br.com.ifsul.vendas.model.Produto;
import br.com.ifsul.vendas.model.User;

public class AppSetup {
    public static List<Produto> produtos = new ArrayList<>();
    public static Cliente cliente = null;
    public static List<ItemPedido> carrinho = new ArrayList<>();
    public static Pedido pedido = null;
    public static User user = null;
    public static Map <String, Bitmap> cacheProdutos = new HashMap<>();
    public static Map <String, Bitmap> cacheClientes = new HashMap<>();
    public static FirebaseAuth mAuth;
}
