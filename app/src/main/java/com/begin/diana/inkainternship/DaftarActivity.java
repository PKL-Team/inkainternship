package com.begin.diana.inkainternship;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class DaftarActivity extends AppCompatActivity {

    Button button;
    Spinner list;
    EditText txtEmail, txtPass1, txtPass2;
    String sItem, sEmail, sPass1, sPass2;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);
<<<<<<< HEAD
        list = findViewById(R.id.listItemDaftar);

=======

        final Spinner List = findViewById(R.id.listItemDaftar );
        list = findViewById(R.id.listItemDaftar);


<<<<<<< HEAD
=======
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PopupMenu dropDownMenu = new PopupMenu(getApplicationContext(), button);
//                dropDownMenu.getMenuInflater().inflate(R.menu.dropdown_menu, dropDownMenu.getMenu());
//                dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//
//                    @Override
//                    public boolean onMenuItemClick(MenuItem menuItem) {
//                        Toast.makeText(getApplicationContext(), "You have clicked " + menuItem.getTitle(), Toast.LENGTH_LONG).show();
//                        return true;
//                    }
//                });
//                dropDownMenu.show();
//            }
//        });
<<<<<<< HEAD

=======
<<<<<<< HEAD
=======
>>>>>>> cdd861f8bb429824719e2b23c4412f8bdf18ca76
>>>>>>> a613dd5798d4a180bc68b18aaf5e279e4f604753
>>>>>>> 20c77239b57d9fd5a86b339593b7f08d01e305b9
        button = findViewById(R.id.btnRegister);
        txtEmail = findViewById(R.id.txtEmailDaftar);
        txtPass1 = findViewById(R.id.txtPass1);
        txtPass2 = findViewById(R.id.txtPass2);
        progressBar = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        PopupMenu dropDownMenu = new PopupMenu(getApplicationContext(), list);
        dropDownMenu.getMenuInflater().inflate(R.menu.dropdown_menu, dropDownMenu.getMenu());
        dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Toast.makeText(getApplicationContext(), "You have clicked " + menuItem.getTitle(), Toast.LENGTH_LONG).show();
                return true;
            }
        });

        if (mAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), Main2Activity.class));
            finish();
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sEmail = txtEmail.getText().toString().trim();
                sPass1 = txtPass1.getText().toString().trim();
                sPass2 = txtPass2.getText().toString().trim();

                if (TextUtils.isEmpty(sEmail)){
                    txtEmail.setError("Email belum diisi");
                    return;
                }
                if (TextUtils.isEmpty(sPass1)){
                    txtPass1.setError("Password belum diisi");
                    return;
                }
                if (TextUtils.isEmpty(sPass2)){
                    txtPass2.setError("Re-Password belum diisi");
                    return;
                }
                if (sPass1.length() < 6 ){
                    txtPass1.setError("Password harus lebih dari 6 karakter");
                    return;
                }if (sPass2.length() < 6){
                    txtPass2.setError("Password harus lebih dari 6 karakter");
                    return;
                }
                if (sPass1 == sPass2){
//                    txtPass2.setError("Re-Password tidak sama dengan Password");
                    txtPass1.setError(sPass1);
                    txtPass2.setError(sPass2);
                    return;
                }
                sItem = list.getSelectedItem().toString();

                progressBar.setVisibility(View.VISIBLE);

                mAuth.createUserWithEmailAndPassword(sEmail, sPass1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(DaftarActivity.this, "Pendaftaran Akun Berhasil", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(), Main2Activity.class));
                        }else {
                            Toast.makeText(DaftarActivity.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });
<<<<<<< HEAD
=======

>>>>>>> 20c77239b57d9fd5a86b339593b7f08d01e305b9

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(this, Main2Activity.class));
        }
    }

}
