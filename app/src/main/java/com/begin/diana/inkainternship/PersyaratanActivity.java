package com.begin.diana.inkainternship;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class PersyaratanActivity extends AppCompatActivity {
    DrawerLayout drawerLayoutSyarat;
    Toolbar toolbarSyarat;
    NavigationView navigationViewSyarat;
    TextView jdlMenuPersyaratan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persyaratan);

        drawerLayoutSyarat = findViewById(R.id.drawerPersyaratan);
        toolbarSyarat = findViewById(R.id.toolbarPersyaratan);
        navigationViewSyarat = findViewById(R.id.navigationViewPersyaratan);
        setSupportActionBar(toolbarSyarat);
        jdlMenuPersyaratan = findViewById(R.id.jdlMenu);
        jdlMenuPersyaratan.setText("Persyaratan Umum");

        navigationViewSyarat.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //Menutup  drawer item klik
                drawerLayoutSyarat.closeDrawers();
                //Memeriksa untuk melihat item yang akan dilklik dan melalukan aksi
                switch (menuItem.getItemId()){
                    // pilihan menu item navigasi akan menampilkan pesan toast klik kalian bisa menggantinya
                    //dengan intent activity
                    case R.id.navigation1:
                        Intent beranda = new Intent(PersyaratanActivity.this, MainActivity.class);
                        finish();
                        startActivity(beranda);
                        return true;
                    case R.id.navigation2:
                        Intent alur = new Intent(PersyaratanActivity.this, AlurActivity.class);
                        finish();
                        startActivity(alur);
                        return true;
                    case R.id.navigation3:
                        return true;
                    case R.id.navigation4:
                        Intent pengaturan = new Intent(PersyaratanActivity.this, PengaturanActivity.class);
                        finish();
                        startActivity(pengaturan);
                        return true;
                    default:
                        Toast.makeText(getApplicationContext(),"kesalahan Terjadi ",Toast.LENGTH_SHORT).show();
                        return true;
                }
            }
        });

        drawerLayoutSyarat = (DrawerLayout) findViewById(R.id.drawerPersyaratan);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayoutSyarat,toolbarSyarat,R.string.openDrawer, R.string.closeDrawer){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayoutSyarat.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }
}
