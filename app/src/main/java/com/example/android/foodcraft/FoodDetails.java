package com.example.android.foodcraft;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.android.foodcraft.Database.Database;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

public class FoodDetails extends AppCompatActivity {

    TextView food_name,food_price,food_description;
    ImageView food_image;
    Button btCart,goToCart;
    ElegantNumberButton numberButton;
    String foodId="";

    FirebaseDatabase database;
    DatabaseReference databaseReference;
    Food clickedFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);

        database=FirebaseDatabase.getInstance();
        databaseReference=database.getReference("products");

        numberButton=findViewById(R.id.number_button);
        btCart=findViewById(R.id.btnCart);
        goToCart=findViewById(R.id.goToCart);

        btCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Database(getBaseContext()).addToCart(new Order(
                        foodId,
                        clickedFood.getName(),
                        numberButton.getNumber(),
                        clickedFood.getPrice().toString(),
                        clickedFood.getImage()
                ));

                Toast.makeText(FoodDetails.this, "Added to Cart", Toast.LENGTH_SHORT).show();
            }
        });

        goToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentCart=new Intent(FoodDetails.this,Carts.class);
                startActivity(intentCart);

            }
        });


        food_description=findViewById(R.id.food_description);
        food_name=findViewById(R.id.food_name);
        food_price=findViewById(R.id.food_price);
        food_image=findViewById(R.id.img_food);

        food_description.setText("");

        if(getIntent()!= null)
        {
            foodId=getIntent().getStringExtra("FoodId");
        }
        if(!foodId.isEmpty() && foodId!=null)
        {
            loadDescription(foodId);
        }

    }

    private void loadDescription(String foodId) {

        databaseReference.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clickedFood=dataSnapshot.getValue(Food.class);
                Picasso.get().load(clickedFood.getImage()).into(food_image);
                food_description.setText(clickedFood.getDescription());
                food_name.setText(clickedFood.getName());

                Locale locale = new Locale("en", "IN");
                NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

                food_price.setText(fmt.format(clickedFood.getPrice()) + "/" + clickedFood.getType());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
