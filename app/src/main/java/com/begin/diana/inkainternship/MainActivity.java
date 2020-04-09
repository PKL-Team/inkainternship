package com.begin.diana.inkainternship;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    Button btnLogin, btnSignup;
    TextView jdlMenu;
    // ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigationView);
        setSupportActionBar(toolbar);

        btnLogin = findViewById(R.id.btnMasuk);
        btnSignup = findViewById(R.id.btnRegister);
        jdlMenu = findViewById(R.id.jdlMenu);
        jdlMenu.setText("Beranda");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent menu = new Intent(MainActivity.this, LoginActivity.class);
                finish();
                startActivity(menu);
            }
        });

//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent menu = new Intent(MainActivity.this, LoginActivity.class);
//                finish();
//                startActivity(menu);
//            }
//        });

//        btnSignup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent menu = new Intent(MainActivity.this, SplashActivity.class);
//                finish();
//                startActivity(menu);
//            }
//        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent menu = new Intent(MainActivity.this, DaftarActivity.class);
                finish();
                startActivity(menu);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //Menutup  drawer item klik
                drawerLayout.closeDrawers();
                //Memeriksa untuk melihat item yang akan dilklik dan melalukan aksi
                switch (menuItem.getItemId()){
                    // pilihan menu item navigasi akan menampilkan pesan toast klik kalian bisa menggantinya
                    //dengan intent activity
                    case R.id.navigation1:
                        return true;
                    case R.id.navigation2:
                        Intent alur = new Intent(MainActivity.this, AlurActivity.class);
                        finish();
                        startActivity(alur);
                        return true;
                    case R.id.navigation3:
                        Intent persyaratan = new Intent(MainActivity.this, PersyaratanActivity.class);
                        finish();
                        startActivity(persyaratan);
                        return true;
                    case R.id.navigation4:
                        Intent pengaturan = new Intent(MainActivity.this, PengaturanActivity.class);
                        finish();
                        startActivity(pengaturan);
                        return true;
                    default:
                        Toast.makeText(getApplicationContext(),"kesalahan Terjadi ",Toast.LENGTH_SHORT).show();
                        return true;
                }
            }
        });

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.openDrawer, R.string.closeDrawer){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }
}
