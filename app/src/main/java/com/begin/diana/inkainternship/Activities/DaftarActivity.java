package com.begin.diana.inkainternship.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.graphics.Bitmap;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.begin.diana.inkainternship.R;
import com.begin.diana.inkainternship.apihelper.BaseApiService;
import com.begin.diana.inkainternship.apihelper.UtilsApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DaftarActivity extends AppCompatActivity {

    Button btnRegister;
    Spinner list;
    EditText txtNama, txtEmail, txtPass1, txtPass2, txtTelp;

    ProgressDialog loading;

    Context mContext;
    BaseApiService mApiService;

    ImageView imageUser;
    static int PReqCode = 1;
    static int REQUESTCODE = 1;
    Uri picImageUrl;

    private CircleImageView mPicture;
    FloatingActionButton mFab;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        mContext = this;
        mApiService = UtilsApi.getAPIService();

//        imageUser = findViewById(R.id.regFoto);
        txtNama = findViewById(R.id.regNama);
        txtEmail = findViewById(R.id.regEmail);
        txtPass1 = findViewById(R.id.regPass1);
        txtPass2 = findViewById(R.id.regPass2);
        txtTelp = findViewById(R.id.regTelp);
        list = findViewById(R.id.listItemDaftar);
        btnRegister = findViewById(R.id.btnRegister);

        mPicture = findViewById(R.id.regFoto);
        mFab = findViewById(R.id.fabChoosePic);

        PopupMenu dropDownMenu = new PopupMenu(getApplicationContext(), list);
        dropDownMenu.getMenuInflater().inflate(R.menu.dropdown_menu, dropDownMenu.getMenu());
        dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Toast.makeText(getApplicationContext(), "You have clicked " + menuItem.getTitle(), Toast.LENGTH_LONG).show();
                return true;
            }
        });

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }

        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                String nama = txtNama.getText().toString().trim();
                String email = txtEmail.getText().toString().trim();
                String password = txtPass1.getText().toString().trim();
                String no_telp = txtTelp.getText().toString().trim();
                String jenis_kegiatan = list.getSelectedItem().toString().trim();
                String foto_profile = getStringImage(bitmap);
                requestRegister(nama,email,password,no_telp,jenis_kegiatan,foto_profile);

            }
        });
    }

    private void requestRegister(final String nama, final String email, final String password, final String no_telp, final String jenis_kegiatan, final String foto_profile) {
        mApiService.registerRequest(nama,email,password,no_telp,jenis_kegiatan,foto_profile)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            Log.i("debug", "onResponse: BERHASIL");
                            loading.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("error").equals("false")){
                                    Toast.makeText(mContext, "BERHASIL REGISTRASI", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(mContext, LoginActivity.class));
                                    finish();
                                } else {
                                    String error_message = jsonRESULTS.getString("error_msg");
                                    Toast.makeText(mContext, error_message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.i("debug", "onResponse: GA BERHASIL");
                            loading.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.getMessage());
                        Toast.makeText(mContext, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                mPicture.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESTCODE);
    }

    private void chooseFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
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
            chooseFile();
        }
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK && requestCode == REQUESTCODE && data != null){
//            //user berhasil memilih gambar
//            //kita perlu menyimpan alamat image ke dalam variabel
//            picImageUrl = data.getData();
//            imageUser.setImageURI(picImageUrl);
//        }
//    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(this, LoginActivity.class));
    }


    private void showMessage(String message){
        Toast.makeText(DaftarActivity.this, message, Toast.LENGTH_SHORT).show();
    }



}

