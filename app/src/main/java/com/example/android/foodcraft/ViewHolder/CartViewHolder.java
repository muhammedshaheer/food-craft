package com.example.android.foodcraft.ViewHolder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.android.foodcraft.Interface.ItemClickListener;
import com.example.android.foodcraft.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private TextView names,price;
    private ElegantNumberButton count;
    private ImageView images;
    private RelativeLayout back;
    private LinearLayout front;

    public RelativeLayout getBack() {
        return back;
    }

    public void setBack(RelativeLayout back) {
        this.back = back;
    }

    public LinearLayout getFront() {
        return front;
    }

    public void setFront(LinearLayout front) {
        this.front = front;
    }

    public ItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public ImageView getImages() {
        return images;
    }

    public void setImages(ImageView images) {
        this.images = images;
    }

    public TextView getNames() {
        return names;
    }

    public TextView getPrice() {
        return price;
    }

    public void setPrice(TextView price) {
        this.price = price;
    }

    public ElegantNumberButton getCount() {
        return count;
    }

    public void setCount(ElegantNumberButton count) {
        this.count = count;
    }

    public void setNames(TextView names) {
        this.names = names;
    }

    private ItemClickListener itemClickListener;

    public CartViewHolder(View itemView) {

        super(itemView);
        names=itemView.findViewById(R.id.item_name);
        price=itemView.findViewById(R.id.item_price);
        count=itemView.findViewById(R.id.item_quantity);
        images=itemView.findViewById(R.id.food_image);
        back=itemView.findViewById(R.id.background);
        front=itemView.findViewById(R.id.front);

    }

    @Override
    public void onClick(View view) {

    }
}