package com.begin.diana.inkainternship.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.begin.diana.inkainternship.Fragments.FragmentAlur;
import com.begin.diana.inkainternship.Fragments.FragmentBeranda;
import com.begin.diana.inkainternship.Fragments.FragmentDaftarAwalPkl;
import com.begin.diana.inkainternship.Fragments.FragmentDaftarUlangPkl;
import com.begin.diana.inkainternship.Fragments.FragmentDokumenBerkas;
import com.begin.diana.inkainternship.Fragments.FragmentInformasiPeserta;
import com.begin.diana.inkainternship.Fragments.FragmentPersyaratan;
import com.begin.diana.inkainternship.R;
import com.begin.diana.inkainternship.SharedPrefManager;
import com.begin.diana.inkainternship.apihelper.UtilsApi;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationView;

public class Main3Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    TextView title;
    String resultNama;

    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        initComponents();

        updateHeader();

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,
                    new FragmentBeranda()).commit();
            navigationView.setCheckedItem(R.id.nav_beranda3);
            title.setText("Beranda");
        }
    }

    private void initComponents() {
        title = findViewById(R.id.toolbarTitle2);
        toolbar = findViewById(R.id.toolbar2);
        navigationView = findViewById(R.id.navigationView3);
        drawerLayout = findViewById(R.id.drawer3);

        setSupportActionBar(toolbar);
        navigationView.setNavigationItemSelectedListener(this);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
    }

    private void updateHeader() {
        navigationView = findViewById(R.id.navigationView3);
        View headerView = navigationView.getHeaderView(0);
        final TextView namaUser = headerView.findViewById(R.id.namaUser);
        final CircleImageView fotoUser = headerView.findViewById(R.id.imageUser);

        sharedPrefManager = new SharedPrefManager(this);
        String email = sharedPrefManager.getSPEmail();
        namaUser.setText(email);

//        String foto = sharedPrefManager.getSPFoto();
//        RequestOptions requestOptions = new RequestOptions();
//        requestOptions.skipMemoryCache(true);
//        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
//        requestOptions.placeholder(R.drawable.ic_account_circle_white_24dp);
//        requestOptions.error(R.drawable.ic_account_circle_white_24dp);
//        Glide.with(Main3Activity.this)
//                .load(UtilsApi.BASE_URL_API+foto)
//                .apply(requestOptions)
//                .into(fotoUser);

    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_beranda3:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,
                        new FragmentBeranda()).commit();
                title.setText("Beranda");
                break;
            case R.id.nav_daftarAwalPkl:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,
                        new FragmentDaftarAwalPkl()).commit();
                title.setText("Daftar Awal");
                break;
            case R.id.nav_daftarUlangPkl:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,
                        new FragmentDaftarUlangPkl()).commit();
                title.setText("Daftar Ulang");
                break;
            case R.id.nav_dokumenBerkasPkl:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,
                        new FragmentDokumenBerkas()).commit();
                title.setText("Dokumen dan Berkas");
                break;
            case R.id.nav_infoPesertaPkl:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,
                        new FragmentInformasiPeserta()).commit();
                title.setText("Informasi Peserta");
                break;
            case R.id.nav_alur3:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,
                        new FragmentAlur()).commit();
                title.setText("Alur Informasi");
                break;
            case R.id.nav_syarat3:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,
                        new FragmentPersyaratan()).commit();
                title.setText("Persyaratan Umum");
                break;
            case R.id.nav_pengaturan3:
                Intent open = new Intent(Main3Activity.this, PengaturanActivity.class);
                finish();
                startActivity(open);
                title.setText("Pengaturan");
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}
