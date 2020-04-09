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

public class AlurActivity extends AppCompatActivity {
    DrawerLayout drawerLayoutAlur;
    Toolbar toolbarAlur;
    NavigationView navigationViewAlur;
    TextView jdlMenuAlur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alur);

        drawerLayoutAlur = findViewById(R.id.drawerAlur);
        toolbarAlur = findViewById(R.id.toolbarAlur);
        navigationViewAlur = findViewById(R.id.navigationViewAlur);
        setSupportActionBar(toolbarAlur);
        jdlMenuAlur = findViewById(R.id.jdlMenu);
        jdlMenuAlur.setText("Alur Pendaftaran");

        navigationViewAlur.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //Menutup  drawer item klik
                drawerLayoutAlur.closeDrawers();
                //Memeriksa untuk melihat item yang akan dilklik dan melalukan aksi
                switch (menuItem.getItemId()){
                    // pilihan menu item navigasi akan menampilkan pesan toast klik kalian bisa menggantinya
                    //dengan intent activity
                    case R.id.navigation1:
                        Intent beranda = new Intent(AlurActivity.this, MainActivity.class);
                        finish();
                        startActivity(beranda);
                        return true;
                    case R.id.navigation2:
                        return true;
                    case R.id.navigation3:
                        Intent persyaratan = new Intent(AlurActivity.this, PersyaratanActivity.class);
                        finish();
                        startActivity(persyaratan);
                        return true;
                    case R.id.navigation4:
                        Intent pengaturan = new Intent(AlurActivity.this, PengaturanActivity.class);
                        finish();
                        startActivity(pengaturan);
                        return true;
                    default:
                        Toast.makeText(getApplicationContext(),"kesalahan Terjadi ",Toast.LENGTH_SHORT).show();
                        return true;
                }
            }
        });

        drawerLayoutAlur = (DrawerLayout) findViewById(R.id.drawerAlur);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayoutAlur,toolbarAlur,R.string.openDrawer, R.string.closeDrawer){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayoutAlur.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

}
