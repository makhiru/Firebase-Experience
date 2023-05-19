package com.example.firebaseexperience.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.firebaseexperience.Add_Fragment;
import com.example.firebaseexperience.MainActivity;
import com.example.firebaseexperience.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class Main_Start extends AppCompatActivity {

    NavigationView navigation;
    DrawerLayout drawer;
    Toolbar toolbar;
    FirebaseAuth mAuth;

    String method;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_start);

        drawer = findViewById(R.id.drawer);
        navigation = findViewById(R.id.navigation);
        toolbar = findViewById(R.id.toolbar);

        method = MainActivity.preferences.getString("method", "h");

        mAuth = FirebaseAuth.getInstance();

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(Main_Start.this, drawer, toolbar, R.string.open, R.string.close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigation.setCheckedItem(R.id.add);
        setFragment(new Add_Fragment());
        toolbar.setTitle("Add Product");

        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.logout) {
                    mAuth.signOut();
                    startActivity(new Intent(Main_Start.this, MainActivity.class));
                    finish();
                }

                return true;
            }
        });

        View headerlayout = navigation.getHeaderView(0);
        LinearLayout layout = headerlayout.findViewById(R.id.headerlayout);
        ImageView imageview = headerlayout.findViewById(R.id.userimg);

        if (method.equals("google")) {
            layout.setBackgroundColor(getResources().getColor(R.color.yellow));
            toolbar.setBackgroundColor(getResources().getColor(R.color.yellow));
            navigation.setBackgroundColor(getResources().getColor(R.color.light_yellow));
            imageview.setImageResource(R.drawable.user);

        } else if (method.equals("phone")) {
            layout.setBackgroundColor(getResources().getColor(R.color.green));
            toolbar.setBackgroundColor(getResources().getColor(R.color.green));
            navigation.setBackgroundColor(getResources().getColor(R.color.light_green));
            imageview.setImageResource(R.drawable.userthree);

        } else if (method.equals("email")) {
            layout.setBackgroundColor(getResources().getColor(R.color.pink));
            toolbar.setBackgroundColor(getResources().getColor(R.color.pink));
            navigation.setBackgroundColor(getResources().getColor(R.color.light_pink));
            imageview.setImageResource(R.drawable.usertwo);

        }else if (method.equals("anonymous")) {
            layout.setBackgroundColor(getResources().getColor(R.color.blue));
            toolbar.setBackgroundColor(getResources().getColor(R.color.blue));
            navigation.setBackgroundColor(getResources().getColor(R.color.light_blue));
            imageview.setImageResource(R.drawable.userfour);

        }
    }

    public void setFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.framelayout, fragment)
                .commit();
    }
}