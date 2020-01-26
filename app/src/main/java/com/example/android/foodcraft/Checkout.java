package com.example.android.foodcraft;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.foodcraft.Database.Database;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Checkout extends AppCompatActivity {

    TextView total,tax,subtotal;
    EditText buyerName,emailId,phoneNo,addresS;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    LinearLayout place;
    LinearLayout placed;
    List<Order> cart = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("Checkout");
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        total=findViewById(R.id.total_order);
        tax=findViewById(R.id.price_tax);
        subtotal=findViewById(R.id.total_fees);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Requests");

        placed= findViewById(R.id.placeorder);
        Locale locale = new Locale("en", "IN");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        String tot = getIntent().getStringExtra("Total");
        String tot_new = tot.substring(2);

        total.setText(fmt.format(Double.parseDouble(tot_new)));
        Double tax_val = Double.parseDouble(tot_new) * 0.05;
        tax.setText(fmt.format(tax_val));

        subtotal.setText(fmt.format((Double.parseDouble(tot_new) + tax_val)));

        buyerName = findViewById(R.id.buyer_name);
        emailId = findViewById(R.id.email);
        phoneNo = findViewById(R.id.phone);
        addresS = findViewById(R.id.address);

        cart = new Database(this).getCarts();

        placed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String buyer = buyerName.getText().toString();
                final String email = emailId.getText().toString();
                final String phone = phoneNo.getText().toString();
                final String address = addresS.getText().toString();

                Log.d("TAG","Order placed : Buyer: "+buyer + "Email: " + email + "Phone: " + phone + "Address " + address);

                Request request=new Request(phone,buyer,email,address,subtotal.getText().toString(),cart);
                databaseReference.child(String.valueOf(System.currentTimeMillis())).setValue(request);

                new Database(getBaseContext()).deleteCart();
                Toast.makeText(Checkout.this,"Order place",Toast.LENGTH_SHORT).show();
            }
        });

    }


    public boolean onCreateOptionsMenu(Menu menu) {

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
