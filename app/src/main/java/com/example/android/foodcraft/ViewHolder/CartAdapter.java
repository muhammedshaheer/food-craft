package com.example.android.foodcraft.ViewHolder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

import com.example.android.foodcraft.Carts;
import com.example.android.foodcraft.Database.Database;
import com.example.android.foodcraft.Order;
import com.example.android.foodcraft.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder>{

    private List<Order> listData;
    private Carts cart;

    public CartAdapter(List<Order> listData, Carts cart) {
        this.listData = listData;
        this.cart = cart;
    }

    @NonNull
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater=LayoutInflater.from(cart);
        View itemView=layoutInflater.inflate(R.layout.cart_list,parent,false);
        return  new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, final int position) {

       holder.getCount().setNumber(listData.get(position).getQuantity());
       holder.getCount().setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
           @Override
           public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
               Order order=listData.get(position);
               order.setQuantity(String.valueOf(newValue));
               new Database(cart).updateCart(order);

               int total = 0;
               List<Order> orders=new Database(cart).getCarts();
               for (Order item : orders)
                   total = total + ( (Integer.parseInt(item.getPrice())) * (Integer.parseInt(item.getQuantity())) );

               Locale locale = new Locale("en", "IN");
               NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

               cart.totals.setText(fmt.format(total));
           }
       });

        Picasso.get().load(listData.get(position).getImage()).resize(100,100).centerCrop().into(holder.getImages());

        Locale locale=new Locale("en","IN");
        NumberFormat fmt=NumberFormat.getCurrencyInstance(locale);
        int price=(Integer.parseInt(listData.get(position).getPrice()));
        holder.getPrice().setText(fmt.format(price));
        holder.getNames().setText(listData.get(position).getProductName());

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public  Order getItem(int position)
    {
        return listData.get(position);
    }

    public void removeFromCart(int position)
    {
        listData.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreFromCart(Order item,int position)
    {
        listData.add(position,item);
        notifyItemInserted(position);
    }

}
