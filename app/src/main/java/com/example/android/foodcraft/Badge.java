package com.example.android.foodcraft;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android.foodcraft.Database.Database;
import com.example.android.foodcraft.ViewHolder.CartAdapter;

import java.util.ArrayList;
import java.util.List;

class Badge {

    private List<Order> cart = new ArrayList<>();
    private Carts carts;
    private CartAdapter cartAdapter;

    Badge(){

    }

    void setupBadge(Context context, TextView cart_badge) {

        cart = new Database(context).getCarts();

        cartAdapter = new CartAdapter(cart, carts);

        if (cart_badge != null) {
            Log.e("Firebase", "Count = " + cartAdapter.getItemCount());
            if (cartAdapter.getItemCount() == 0) {
                if (cart_badge.getVisibility() != View.GONE) {
                    cart_badge.setVisibility(View.GONE);
                }
            } else {
                cart_badge.setText(String.valueOf(cartAdapter.getItemCount()));
                if (cart_badge.getVisibility() != View.VISIBLE) {
                    cart_badge.setVisibility(View.VISIBLE);
                }
            }
        }
    }

}
