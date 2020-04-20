package com.begin.diana.inkainternship;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import java.util.Map;

public class Main2Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout2;
    ActionBarDrawerToggle actionBarDrawerToggle2;
    Toolbar toolbar2;
    NavigationView navigationView2;
    TextView title;
    ImageView imageUserHeader, imageUserToolbar;
    TextView set1,set2,set3,set4;
    FirebaseAuth mAuth;

    //var database
    private Map<String, String> userMap;
    private String email;
    private String userid;
    private static final String USERS = "users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        title = findViewById(R.id.toolbarTitle2);

//        imageUserHeader = findViewById(R.id.profilePic2);
//        imageUserToolbar = findViewById(R.id.iconProfileToolbar2);
//        imageUserHeader.setImageResource(R.drawable.foto_profile);
//        imageUserToolbar.setImageResource(R.drawable.foto_profile);

        toolbar2 = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar2);

        drawerLayout2 = findViewById(R.id.drawer2);
        navigationView2 = findViewById(R.id.navigationView2);
        navigationView2.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle2 = new ActionBarDrawerToggle(this, drawerLayout2, toolbar2, R.string.openDrawer, R.string.closeDrawer);
        drawerLayout2.addDrawerListener(actionBarDrawerToggle2);
        actionBarDrawerToggle2.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle2.syncState();

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,
                    new FragmentBeranda()).commit();
            navigationView2.setCheckedItem(R.id.nav_beranda2);
            title.setText("Beranda");
        }


        //receive data from login screen
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        set1 = findViewById(R.id.set1);
        set1.setText(email);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout2.isDrawerOpen(GravityCompat.START)){
            drawerLayout2.closeDrawer(GravityCompat.START);
        }else{
            Intent back = new Intent(Main2Activity.this, activityCobaCoba.class);
            finish();
            startActivity(back);
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_beranda2:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,
                        new FragmentBeranda()).commit();
                title.setText("Beranda");
                break;
            case R.id.nav_daftarAwal:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,
                        new FragmentDaftarAwal()).commit();
                title.setText("Daftar Awal");
                break;
            case R.id.nav_daftarUlang:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,
                        new FragmentDaftarUlang()).commit();
                title.setText("Daftar Ulang");
                break;
            case R.id.nav_dokumenBerkas:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,
                        new FragmentDokumenBerkas()).commit();
                title.setText("Dokumen dan Berkas");
                break;
            case R.id.nav_infoPeserta:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,
                        new FragmentInformasiPeserta()).commit();
                title.setText("Informasi Peserta");
                break;
            case R.id.nav_alur2:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,
                        new FragmentAlur()).commit();
                title.setText("Alur Informasi");
                break;
            case R.id.nav_syarat2:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,
                        new FragmentPersyaratan()).commit();
                title.setText("Persyaratan Umum");
                break;
            case R.id.nav_pengaturan2:
                Intent open = new Intent(Main2Activity.this, PengaturanActivity.class);
                finish();
                startActivity(open);
                title.setText("Pengaturan");
                break;
        }
        drawerLayout2.closeDrawer(GravityCompat.START);
        return true;
    }

    public  void logout(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (mAuth.getCurrentUser() == null) {
//            finish();
//            startActivity(new Intent(this, LoginActivity.class));
//        }
//    }
}
