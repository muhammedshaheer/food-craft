package com.example.android.foodcraft.ViewHolder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.foodcraft.Interface.ItemClickListener;
import com.example.android.foodcraft.R;

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView food_name;
    public TextView food_price;
    public ImageView food_image;
    public ImageButton fav;
    private ItemClickListener itemClickListener;

    public FoodViewHolder(View itemView) {
        super(itemView);

        food_name=itemView.findViewById(R.id.food_name);
        food_price=itemView.findViewById(R.id.food_price);
        food_image=itemView.findViewById(R.id.food_image);
        fav=itemView.findViewById(R.id.wish_list);

        itemView.setOnClickListener(this);
    }
    public void setItemClickListener(ItemClickListener itemClickListener)
    {
        this.itemClickListener=itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }



}
