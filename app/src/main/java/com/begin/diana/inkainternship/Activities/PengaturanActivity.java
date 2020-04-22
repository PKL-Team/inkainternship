package com.begin.diana.inkainternship.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.TextView;

import com.begin.diana.inkainternship.Fragments.FragmentPengaturan;
import com.begin.diana.inkainternship.R;

public class PengaturanActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaturan);
        title = findViewById(R.id.toolbarTitle);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,
                    new FragmentPengaturan()).commit();
            title.setText("Pengaturan");
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
