package com.begin.diana.inkainternship;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class activityCobaCoba extends AppCompatActivity {

    Button btnTanpa, btnDengan, btnCoba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coba_coba);

        btnTanpa = findViewById(R.id.btnTanpaLogin);
        btnDengan = findViewById(R.id.btnDenganLogin);
        btnCoba = findViewById(R.id.btnLogin);

        btnTanpa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent open = new Intent(activityCobaCoba.this, MainActivity.class);
                finish();
                startActivity(open);
            }
        });

        btnDengan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent open = new Intent(activityCobaCoba.this, Main2Activity.class);
                finish();
                startActivity(open);
            }
        });

        btnCoba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent coba = new Intent(activityCobaCoba.this, LoginActivity.class);
                finish();
                startActivity(coba);
            }
        });

    }
}
