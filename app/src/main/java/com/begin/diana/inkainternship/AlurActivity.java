package com.begin.diana.inkainternship;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AlurActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alur);
        title = findViewById(R.id.toolbarTitle);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,
                    new FragmentAlur()).commit();
            title.setText("Alur Pendaftaran");
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
