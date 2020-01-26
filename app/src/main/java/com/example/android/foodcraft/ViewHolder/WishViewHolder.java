package com.example.android.foodcraft.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.android.foodcraft.R;

public class WishViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView food_name, food_price;
    private ImageView food_image;

    public TextView getFood_name() {
        return food_name;
    }

    public void setFood_name(TextView food_name) {
        this.food_name = food_name;
    }

    public TextView getFood_price() {
        return food_price;
    }

    public void setFood_price(TextView food_price) {
        this.food_price = food_price;
    }

    public ImageView getFood_image() {
        return food_image;
    }

    public void setFood_image(ImageView food_image) {
        this.food_image = food_image;
    }

    WishViewHolder(View itemView){

        super(itemView);

        food_name = itemView.findViewById(R.id.item_name);
        food_price = itemView.findViewById(R.id.item_price);
        food_image = itemView.findViewById(R.id.item_image);

    }

    @Override
    public void onClick(View v) {

    }
}
