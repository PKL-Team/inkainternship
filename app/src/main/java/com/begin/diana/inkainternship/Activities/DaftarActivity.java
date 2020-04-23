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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class DaftarActivity extends AppCompatActivity {

    Button btnRegister;
    Spinner list;
    EditText txtNama, txtEmail, txtPass1, txtPass2;
    String sNama, sItem, sEmail, sPass1, sPass2;
    private User user;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
//    private FirebaseDatabase database;
//    private DatabaseReference mDatabase;
//    private static final String USERS = "users";

    ImageView imageUser;
    static int PReqCode = 1;
    static int REQUESTCODE = 1;
    Uri picImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        imageUser = findViewById(R.id.regFoto);
        txtNama = findViewById(R.id.regNama);
        txtEmail = findViewById(R.id.regEmail);
        txtPass1 = findViewById(R.id.regPass1);
        txtPass2 = findViewById(R.id.regPass2);
        list = findViewById(R.id.listItemDaftar);
        btnRegister = findViewById(R.id.btnRegister);
        progressBar = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();
//        database = FirebaseDatabase.getInstance();
//        mDatabase = database.getReference(USERS);

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

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRegister.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                sNama = txtNama.getText().toString().trim();
                sEmail = txtEmail.getText().toString().trim();
                sPass1 = txtPass1.getText().toString().trim();
                sPass2 = txtPass2.getText().toString().trim();
                sItem = list.getSelectedItem().toString();

                if (sNama.isEmpty() || sEmail.isEmpty() || sPass1.isEmpty() || sPass2.isEmpty() || sItem.isEmpty() || !sPass1.equals(sPass2)){
                    showMessage("Pastikan semua field terisi");
                    btnRegister.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                }else {
                    CreateUserAccount(sEmail,sNama,sPass1);
                }
            }
        });
    }

    private void CreateUserAccount(final String sEmail, final String sNama, String sPass1) {
        mAuth.createUserWithEmailAndPassword(sEmail, sPass1).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    showMessage("Pendaftaran Akun Berhasil");
                    user = new User(sNama,sEmail,sItem);
                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                showMessage("Input User Info Berhasil");
                            }else {
                                showMessage("Input User Info Gagal"+task.getException().getMessage());
                            }
                        }
                    });
                    updateUserInfo(sNama,picImageUrl, mAuth.getCurrentUser());
                }else {
                    showMessage("Pendaftaran Akun Gagal"+task.getException().getMessage());
                    btnRegister.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void updateUserInfo(final String sNama,  Uri picImageUrl, final FirebaseUser currentUser) {
        //upload foto ke firebase storage
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("user_photos");
        final StorageReference imageFilePath = mStorage.child(picImageUrl.getLastPathSegment());
        imageFilePath.putFile(picImageUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //upload image sukses
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //alamat image (uri)
                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(sNama)
                                .setPhotoUri(uri)
                                .build();
                        currentUser.updateProfile(profileUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            showMessage("Registrasi Complete");
                                            updateUI();
                                        }

                                    }
                                });
                    }
                });
            }
        });
    }

    private void updateUI() {
        startActivity(new Intent(getApplicationContext(), Main2Activity.class));
        finish();
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
                showMessage("Please Accept for required permission");
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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null){
            //handle login
        }
    }

    private void showMessage(String message){
        Toast.makeText(DaftarActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}

