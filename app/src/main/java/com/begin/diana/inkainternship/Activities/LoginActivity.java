package com.begin.diana.inkainternship.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.begin.diana.inkainternship.R;
import com.begin.diana.inkainternship.activityCobaCoba;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    Button btnDaftar, btnMasuk;
    TextView txtEmail, txtPass;
    String sEmail, sPass;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnDaftar = findViewById(R.id.btnGoRegister);
        btnMasuk = findViewById(R.id.btnSignin);
        txtEmail = findViewById(R.id.txtEmailLogin);
        txtPass = findViewById(R.id.txtPassLogin);
        progressBar = findViewById(R.id.progressBar2);
        mAuth = FirebaseAuth.getInstance();


        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent daftar = new Intent(LoginActivity.this, DaftarActivity.class);
                finish();
                startActivity(daftar);
            }
        });

        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                btnMasuk.setVisibility(View.INVISIBLE);
                sEmail = txtEmail.getText().toString().trim();
                sPass = txtPass.getText().toString().trim();

                if (sEmail.isEmpty() || sPass.isEmpty()){
                    showMessage("Pastikan semua field terisi");
                }else {
                    signIn(sEmail,sPass);
                }

            }
        });
    }

    private void signIn(String sEmail, String sPass) {
        mAuth.signInWithEmailAndPassword(sEmail, sPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressBar.setVisibility(View.INVISIBLE);
                    btnMasuk.setVisibility(View.VISIBLE);
                    showMessage("Berhasil Masuk");
                    updateUI();
                }else {
                    showMessage("Gagal Masuk " + task.getException().getMessage());
                }
            }
        });
    }

    public void updateUI() {
        startActivity(new Intent(getApplicationContext(), Main2Activity.class));
        finish();
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(this, activityCobaCoba.class));
    }


}
