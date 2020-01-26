package com.example.android.foodcraft;

import android.content.Intent;
import android.graphics.Color;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.foodcraft.Database.Database;
import com.example.android.foodcraft.Helper.RecyclerItemTouchHelper;
import com.example.android.foodcraft.Interface.RecyclerItemTouchHelperListener;
import com.example.android.foodcraft.ViewHolder.CartAdapter;
import com.example.android.foodcraft.ViewHolder.CartViewHolder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Carts extends AppCompatActivity implements RecyclerItemTouchHelperListener {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference databaseReference;

    public TextView totals;
    Button place;

    List<Order> cart = new ArrayList<>();
    CartAdapter cartAdapter;
    RelativeLayout relativeLayout;

    Button btn_continue;

    CartViewHolder viewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carts);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Cart");
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Orders");

        recyclerView =  findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        relativeLayout=findViewById(R.id.rootLayout);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback=new RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        totals =  findViewById(R.id.total);
        place =  findViewById(R.id.place_order);
        place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent checkout=new Intent(Carts.this,Checkout.class);
                checkout.putExtra("Total",totals.getText());
                startActivity(checkout);

            }
        });

        loadCart();

    }


    private void loadCart() {
        cart = new Database(this).getCarts();

        cartAdapter = new CartAdapter(cart, this);
        recyclerView.setAdapter(cartAdapter);

        if(cartAdapter.getItemCount() == 0){
            setEmptyView();
        }

        int total = 0;

        for (Order order : cart)
            total = total + (Integer.parseInt(order.getPrice())) * (Integer.parseInt(order.getQuantity()));

        Locale locale = new Locale("en", "IN");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        totals.setText(fmt.format(total));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_cart) {
            Intent intentCart=new Intent(this,Carts.class);
            startActivity(intentCart);
        }else if(id == android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if(viewHolder instanceof CartViewHolder)
        {
            String name=((CartAdapter)recyclerView.getAdapter()).getItem(viewHolder.getAdapterPosition()).getProductName();
            final Order deleteItem=((CartAdapter)recyclerView.getAdapter()).getItem(viewHolder.getAdapterPosition());
            final int deleteIndex=viewHolder.getAdapterPosition();

            cartAdapter.removeFromCart(deleteIndex);
            new Database(getBaseContext()).removeFromCart(deleteItem);

            int total = 0;
            List<Order> orders=new Database(getBaseContext()).getCarts();
            for (Order item : orders)
                total = total + ( (Integer.parseInt(item.getPrice())) * (Integer.parseInt(item.getQuantity())) );

            Locale locale = new Locale("en", "IN");
            NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

            totals.setText(fmt.format(total));

            Snackbar snackbar=Snackbar.make(relativeLayout,name+" removed from cart",Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cartAdapter.restoreFromCart(deleteItem,deleteIndex);
                    new Database(getBaseContext()).addToCart(deleteItem);

                    int total = 0;
                    List<Order> orders=new Database(getBaseContext()).getCarts();
                    for (Order item : orders)
                        total = total + ( (Integer.parseInt(item.getPrice())) * (Integer.parseInt(item.getQuantity())) );

                    Locale locale = new Locale("en", "IN");
                    NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

                    totals.setText(fmt.format(total));

                    loadCart();

                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();

            snackbar.addCallback(new Snackbar.Callback(){

                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    if(cartAdapter.getItemCount() == 0){
                        setEmptyView();
                    }
                }
            });

        }

    }

    private void setEmptyView(){

        setContentView(R.layout.activity_empty_carts);

        btn_continue = findViewById(R.id.btn_continue);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Empty Cart");
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCart=new Intent(Carts.this,Home.class);
                startActivity(intentCart);
            }
        });

    }

}