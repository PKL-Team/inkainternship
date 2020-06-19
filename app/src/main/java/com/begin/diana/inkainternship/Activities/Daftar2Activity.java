package com.begin.diana.inkainternship.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import com.begin.diana.inkainternship.R;
import com.begin.diana.inkainternship.apihelper.BaseApiService;
import com.begin.diana.inkainternship.apihelper.UtilsApi;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Daftar2Activity extends AppCompatActivity {

    Button btnRegister;
    Spinner list;
    EditText txtEmail, txtPass1, txtPass2;

    ProgressDialog loading;

    Context mContext;
    BaseApiService mApiService;

    private CircleImageView mPicture;
    FloatingActionButton mFab;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar2);

        inItComponents();
        spinner();
        buttonClick();

    }

    private void inItComponents() {
        mContext = this;
        mApiService = UtilsApi.getAPIService();

        txtEmail = findViewById(R.id.regEmail2);
        txtPass1 = findViewById(R.id.regPass12);
        txtPass2 = findViewById(R.id.regPass22);
        list = findViewById(R.id.listItemDaftar2);
        btnRegister = findViewById(R.id.btnRegister2);

    }

    private void spinner() {
        PopupMenu dropDownMenu = new PopupMenu(getApplicationContext(), list);
        dropDownMenu.getMenuInflater().inflate(R.menu.dropdown_menu, dropDownMenu.getMenu());
        dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Toast.makeText(getApplicationContext(), "You have clicked " + menuItem.getTitle(), Toast.LENGTH_LONG).show();
                return true;
            }
        });
    }

    private void buttonClick() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                String email = txtEmail.getText().toString().trim();
                String password = txtPass1.getText().toString().trim();
                String jenis_kegiatan = list.getSelectedItem().toString().trim();
                if ( email.isEmpty() || password.isEmpty() || jenis_kegiatan.isEmpty()){
                    showMessage("Field belum terisi. Mohon lengkapi semua field isian diatas");
                }else {
                    requestRegister(email,password,jenis_kegiatan);
                }

            }
        });
    }

    private void requestRegister(final String email, final String password, final String jenis_kegiatan) {
        mApiService.registerRequest(email,password,jenis_kegiatan)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            Log.i("debug", "onResponse: BERHASIL");
                            loading.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("error").equals("false")){
                                    showMessage("BERHASIL REGISTRASI");
                                    startActivity(new Intent(mContext, LoginActivity.class));
                                    finish();
                                } else {
                                    String error_message = jsonRESULTS.getString("error_msg");
                                    showMessage(error_message);
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
                        showMessage("Koneksi Internet Bermasalah");
                    }
                });
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void showMessage(String message){
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }



}

