package com.begin.diana.inkainternship;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.begin.diana.inkainternship.Adapter.CustomExpandableListAdapter;
import com.begin.diana.inkainternship.Helper.FragmentNavigationManager;
import com.begin.diana.inkainternship.Interface.NavigationManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Main2Activity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    private String[] items;

    private ExpandableListView expandableListView;
    private ExpandableListAdapter adapter;
    private List<String> lstTitle;
    private HashMap<String, List<String>> lstChild;
    private NavigationManager navigationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer2);
        mActivityTitle = getTitle().toString();
        expandableListView = (ExpandableListView)findViewById(R.id.navList);
        navigationManager = FragmentNavigationManager.getmInstance(this);

        initItems();

        View listHeaderView = getLayoutInflater().inflate(R.layout.nav_header, null, false);
        expandableListView.addHeaderView(listHeaderView);

        genData();

        addDrawersItem();
        setupDrawer();

        if(savedInstanceState == null) {
            selectFirstItemAsDefault();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("INKA");
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void selectFirstItemAsDefault() {
        if(navigationManager != null){
            String firstItem = lstTitle.get(0);
            navigationManager.showFragment(firstItem);
            getSupportActionBar().setTitle(firstItem);
        }
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
//                getSupportActionBar().setTitle("INKA");
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
//                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu();
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void addDrawersItem() {
        adapter = new CustomExpandableListAdapter(this,lstTitle,lstChild);

        expandableListView.setAdapter(adapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                getSupportActionBar().setTitle(lstTitle.get(groupPosition).toString());

            }
        });
        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                getSupportActionBar().setTitle("INKA INTERSHIP");
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long l) {
                String selectedItem = ((List)(lstChild.get(lstTitle.get(groupPosition))))
                        .get(childPosition).toString();
                getSupportActionBar().setTitle(selectedItem);

                if(items[0].equals(lstTitle.get(groupPosition)))
                    navigationManager.showFragment(selectedItem);
                else
                    throw new IllegalArgumentException("Not Supported Fragment");

                mDrawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }

    private void genData() {
//        List<String> title = Arrays.asList("Beranda","Pendaftaran PKL","Alur Pendafataran");
//        List<String> menu = Arrays.asList("Beranda","Pendaftaran PKL","Alur Pendaftaran","Persyaratan Umum");
        lstTitle = new ArrayList<String>();
        String[] array = new String[]{"Beranda","Pendaftaran PKL","Alur Pendaftaran","Persyaratan Umum"};
        lstTitle = Arrays.asList(array);
        List<String> childitem = Arrays.asList("Daftar Awal","Daftar Ulang","Dokumen & Berkas","Informasi Peserta");
        lstChild = new HashMap<>();
//        lstChild.put(menu.get(0),childitem);
//        lstChild.put(menu.get(1),childitem);
//        lstChild.put(menu.get(2),childitem);
//        lstChild.put(menu.get(3),childitem);
//        lstTitle = new ArrayList<>(lstChild.keySet());

//        lstTitle = new ArrayList<String>();
//        String[] array = new String[]{"Beranda","Pendaftaran PKL","Alur Pendaftaran","Persyaratan Umum"};
//        lstTitle = Arrays.asList(array);
//        lstChild = new HashMap<String, List<String>>();
        lstChild.put(lstTitle.get(0),childitem);
        lstChild.put(lstTitle.get(1),childitem);
        lstChild.put(lstTitle.get(2),childitem);
        lstChild.put(lstTitle.get(3),childitem);
//        lstTitle = new ArrayList<>(lstChild.keySet());
    }

    private void initItems() {
        items = new String[]{"Beranda","Pendaftaran PKL","Alur Pendaftaran","Persyaratan Umum"};
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(mDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }
}
