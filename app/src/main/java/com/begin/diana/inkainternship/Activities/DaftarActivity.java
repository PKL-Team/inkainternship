package com.begin.diana.inkainternship.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.begin.diana.inkainternship.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class DaftarActivity extends AppCompatActivity {

    Button button;
    Spinner list;
    EditText txtNama, txtEmail, txtPass1, txtPass2;
    String sItem, sEmail, sPass1, sPass2;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

    ImageView imageUser;
    static int PReqCode = 1;
    static int REQUESTCODE = 1;
    Uri picImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        imageUser = findViewById(R.id.imgUserPhoto);
        txtNama = findViewById(R.id.txtNama);
        txtEmail = findViewById(R.id.txtEmailDaftar);
        txtPass1 = findViewById(R.id.txtPass1);
        txtPass2 = findViewById(R.id.txtPass2);
        list = findViewById(R.id.listItemDaftar);
        button = findViewById(R.id.btnRegister);
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

        imageUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 22){
                    checkAndRequestPermission();
                }else{
                    openGallery();
                }
            }
        });

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
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESTCODE);


    }

    private void checkAndRequestPermission() {
        if (ContextCompat.checkSelfPermission(DaftarActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    DaftarActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(DaftarActivity.this, "Please Accept for required permission",
                        Toast.LENGTH_SHORT).show();
            }else {
                ActivityCompat.requestPermissions(DaftarActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }
        }else {
            openGallery();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUESTCODE && data != null){
            //user berhasil memilih gambar
            //kita perlu menyimpan alamat image ke dalam variabel
            picImageUrl = data.getData();
            imageUser.setImageURI(picImageUrl);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(this, Main2Activity.class));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(this, LoginActivity.class));
    }
}
