package com.begin.diana.inkainternship;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {
    Button btnDaftar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnDaftar = findViewById(R.id.btnDaftar);

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent menu = new Intent(LoginActivity.this, DaftarActivity.class);
                finish();
                startActivity(menu);
            }
        });
    }
}
