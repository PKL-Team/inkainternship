package com.begin.diana.inkainternship;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    androidx.appcompat.widget.Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigationView);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.openDrawer,R.string.closeDrawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.navigation1:
                Toast.makeText(MainActivity.this, "Beranda Selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.navigation2:
                Toast.makeText(MainActivity.this, "Alur Pendaftaran Selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.navigation3:
                Toast.makeText(MainActivity.this, "Persyaratan Umum Selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.navigation4:
                Toast.makeText(MainActivity.this, "Pengaturan Selected", Toast.LENGTH_SHORT).show();
                break;

        }
        return false;
    }
}
