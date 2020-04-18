package com.begin.diana.inkainternship;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

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


        if (mAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), Main2Activity.class));
            finish();
        }

        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sEmail = txtEmail.getText().toString().trim();
                sPass = txtPass.getText().toString().trim();

                if (TextUtils.isEmpty(sEmail)){
                    txtEmail.setError("Email belum diisi");
                    return;
                }
                if (TextUtils.isEmpty(sPass)){
                    txtPass.setError("Password belum diisi");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                mAuth.signInWithEmailAndPassword(sEmail, sPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Berhasil Masuk", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Main2Activity.class));
                        }else {
                            Toast.makeText(LoginActivity.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
