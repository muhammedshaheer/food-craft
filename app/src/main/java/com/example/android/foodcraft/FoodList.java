package com.example.android.foodcraft;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.foodcraft.Database.Database;
import com.example.android.foodcraft.Interface.ItemClickListener;
import com.example.android.foodcraft.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

import steelkiwi.com.library.DotsLoaderView;

public class FoodList extends AppCompatActivity {

    TextView cart_badge;

    RecyclerView recyclerView;

    FirebaseDatabase database;
    DatabaseReference databaseReference;
    //ImageButton wishList;

    String categoryId="";
    DotsLoaderView dotsLoaderView;
    Database db;

    FirebaseRecyclerAdapter<Food,FoodViewHolder> adapter;
    FirebaseAuth firebaseAuth;
    SwipeRefreshLayout swipeRefreshLayout;

    Badge badge;

    //FoodViewHolder foodHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        badge = new Badge();

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("Products");
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        database= FirebaseDatabase.getInstance();
        databaseReference=database.getReference("products");
        firebaseAuth= FirebaseAuth.getInstance();

        db=new Database(this);

        //wishList=findViewById(R.id.wish_list);
//
//        wishList.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (wishList.getImageAlpha() == R.drawable.ic_favorite_border_grey_24dp) {
//                    wishList.setImageAlpha(R.drawable.ic_favorite_grey_24dp);
//                }else if (wishList.getImageAlpha() == R.drawable.ic_favorite_grey_24dp) {
//                    wishList.setImageAlpha(R.drawable.ic_favorite_border_grey_24dp);
//                }
//            }
//        });

        recyclerView=findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        dotsLoaderView=findViewById(R.id.dots_loader_View) ;
        dotsLoaderView.show();

        if(getIntent()!= null)
        {
            categoryId=getIntent().getStringExtra("CategoryId");
        }
        if(!categoryId.isEmpty() && categoryId!=null)
        {

            swipeRefreshLayout=findViewById(R.id.swipe);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    loadListFood(categoryId);
                }
            });
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    loadListFood(categoryId);
                }
            });

        }

    }

    private void loadListFood(String categoryId) {

            adapter=new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class, R.layout.food_item,FoodViewHolder.class,
                    databaseReference.orderByChild("MenuId").equalTo(categoryId)) {//select * from where
                @Override
                protected void populateViewHolder(final FoodViewHolder viewHolder, final Food model, final int position) {
                    dotsLoaderView.hide();
                    viewHolder.food_name.setText(model.getName());

                    Locale locale = new Locale("en", "IN");
                    NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

                    viewHolder.food_price.setText(fmt.format(model.getPrice()) + "/" + model.getType());
                    Log.e("Firebase", "price = " + model.getPrice());
                    Picasso.get().load(model.getImage()).into(viewHolder.food_image);

                    if(db.isFavorite(adapter.getRef(position).getKey()))
                        viewHolder.fav.setImageResource(R.drawable.ic_favorite_yello_24dp);

                    //adding to favorites
                    viewHolder.fav.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!db.isFavorite(adapter.getRef(position).getKey())) {
                                db.addToFavorites(adapter.getRef(position).getKey());
                                viewHolder.fav.setImageResource(R.drawable.ic_favorite_yello_24dp);
                                Toast.makeText(FoodList.this, model.getName() + "added to Favorites", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                db.removeFavorites(adapter.getRef(position).getKey());
                                viewHolder.fav.setImageResource(R.drawable.ic_favorite_fill_24dp);
                                Toast.makeText(FoodList.this, model.getName() + "removed from Favorites", Toast.LENGTH_SHORT).show();
                            }

                        }

                    });



                    //final Food local=model;
                    viewHolder.setItemClickListener(new ItemClickListener() {
                        @Override
                        public void onClick(View view, int position, boolean isLongClick) {
                            Intent intent=new Intent(FoodList.this,FoodDetails.class);
                            intent.putExtra("FoodId",adapter.getRef(position).getKey());
                            startActivity(intent);
                        }
                    });
                }
            };
            Log.d("TAG",""+adapter.getItemCount());

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);

    }

    @Override
    protected void onResume() {
        super.onResume();
        badge.setupBadge(this, cart_badge);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.home, menu);

        MenuItem search = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) search.getActionView();

        // TODO Search View Code

        final MenuItem menuItem = menu.findItem(R.id.action_cart);
        View actionView = MenuItemCompat.getActionView(menuItem);
        cart_badge = actionView.findViewById(R.id.cart_badge);

        badge.setupBadge(this, cart_badge);

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_cart) {
            Intent intentCart=new Intent(FoodList.this,Carts.class);
            startActivity(intentCart);
        }else if(id == android.R.id.home){
            onBackPressed();
        }else if(id == R.id.action_search){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
