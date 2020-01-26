package com.example.android.foodcraft;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.example.android.foodcraft.Database.Database;
import com.example.android.foodcraft.ViewHolder.CartAdapter;
import com.example.android.foodcraft.ViewHolder.WishViewHolder;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.glide.slider.library.Animations.DescriptionAnimation;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.SliderTypes.BaseSliderView;
import com.glide.slider.library.SliderTypes.TextSliderView;
import com.glide.slider.library.Tricks.ViewPagerEx;
import com.example.android.foodcraft.Interface.ItemClickListener;
import com.example.android.foodcraft.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import steelkiwi.com.library.DotsLoaderView;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ViewPagerEx.OnPageChangeListener {

    TextView fullNames, shopCategory, cart_badge;
    HashMap<String,String> imageList;
    String name;

    FirebaseDatabase database;
    DatabaseReference category, databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseRecyclerAdapter<Category, MenuViewHolder> adapter;

    RequestOptions requestOptions;

    DotsLoaderView dotsLoaderView;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    NavigationView navigationView;
    View HeaderView;

    SliderLayout sliderLayout;
    SwipeRefreshLayout swipeRefreshLayout;

    Badge badge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        badge = new Badge();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);

        firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser user=firebaseAuth.getCurrentUser();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        shopCategory = findViewById(R.id.shop_category);

        HeaderView=navigationView.getHeaderView(0);
        fullNames=HeaderView.findViewById(R.id.full_name);
        database= FirebaseDatabase.getInstance();
        category=database.getReference("categories");
        dotsLoaderView=findViewById(R.id.dots_loader_View) ;
        dotsLoaderView.show();

        swipeRefreshLayout=findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMenu();
                loadSlider();
            }
        });
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                loadMenu();
                loadSlider();
            }
        });

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        String displayName = null;
        if(account != null) {
            displayName = account.getDisplayName() + "\n" + account.getEmail();
        }
        fullNames.setText(displayName);

        databaseReference=database.getReference("Users").child(user.getUid());

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        requestOptions = new RequestOptions().centerCrop();
        //requestOptions.centerCrop();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cart_intent=new Intent(Home.this,Carts.class);
                startActivity(cart_intent);
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        recyclerView=findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    protected void onResume() {
        super.onResume();
        badge.setupBadge(this, cart_badge);
    }

    @Override
    protected void onStop() {
        super.onStop();
        sliderLayout.stopAutoCycle();
    }

    private void loadSlider() {

        sliderLayout=findViewById(R.id.banner);
        imageList=new HashMap<>();
        final DatabaseReference banners=database.getReference("Banner");

        banners.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dotsLoaderView.hide();
                for(DataSnapshot postsnapshot:dataSnapshot.getChildren())
                {
                    Banner banner=postsnapshot.getValue(Banner.class);
                    if(banner != null) {
                        imageList.put(banner.getName() + "@@" + banner.getId(), banner.getImage());
                    }

                }
                for(String key:imageList.keySet())
                {
                    String keySplit[]=key.split("@@");
                    String foodName=keySplit[0];
                    String foodId=keySplit[1];

                    //Text
                    final TextSliderView textSliderView=new TextSliderView(getBaseContext());
                    textSliderView.description(foodName).image(imageList.get(key)).setRequestOption(requestOptions)
                            .setProgressBarVisible(true)
                            .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                @Override
                                public void onSliderClick(BaseSliderView slider) {
                                    Intent slide=new Intent(Home.this,FoodDetails.class);
                                    slide.putExtras(textSliderView.getBundle());
                                    startActivity(slide);
                                }
                            });

                    textSliderView.bundle(new Bundle());
                    textSliderView.getBundle().putString("FoodId",foodId);
                    sliderLayout.addSlider(textSliderView);

                }

                shopCategory.setText(R.string.shop_by_category);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(4000);
        sliderLayout.addOnPageChangeListener(this);
    }

    private void loadMenu() {
        adapter=new FirebaseRecyclerAdapter<Category, MenuViewHolder>(Category.class, R.layout.menu_item, MenuViewHolder.class,category) {
            @Override
            protected void populateViewHolder(MenuViewHolder viewHolder, Category model, int position) {
                dotsLoaderView.hide();
                viewHolder.textView.setText(model.getName());
                Log.e("Firebase", "image url = " + model.getUrl());
                Picasso.get().load(model.getUrl()).into(viewHolder.imageView);

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intent=new Intent(Home.this,FoodList.class);
                        intent.putExtra("CategoryId",adapter.getRef(position).getKey());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }
                });
            }
        };

        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.home, menu);
        MenuItem search = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) search.getActionView();

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

        // TODO Search View Code
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cart) {
            Intent intentCart=new Intent(Home.this,Carts.class);
            startActivity(intentCart);
        }else if(id == R.id.action_search){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu) {



        } else if (id == R.id.nav_orders) {

            Intent intentCart=new Intent(this,OrderTrack.class);
            startActivity(intentCart);

        } else if (id == R.id.nav_cart) {

            Intent intentCart=new Intent(this,Carts.class);
            startActivity(intentCart);

        } else if (id == R.id.nav_wishList) {

            Intent intentCart=new Intent(this, WishList.class);
            startActivity(intentCart);

        } else if (id == R.id.nav_logout) {

            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, MainActivity.class));

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


}
