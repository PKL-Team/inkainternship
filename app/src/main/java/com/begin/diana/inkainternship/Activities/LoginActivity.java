package com.begin.diana.inkainternship.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.begin.diana.inkainternship.SharedPrefManager;
import com.begin.diana.inkainternship.apihelper.BaseApiService;
import com.begin.diana.inkainternship.apihelper.UtilsApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {
    Button btnDaftar, btnMasuk;
    TextView txtEmail, txtPass;
    String sEmail, sPass;

    ProgressDialog loading;

    Context mContext;
    BaseApiService mApiService;

    SharedPrefManager sharedPrefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inItComponents();

        buttonClick();

    }

    private void inItComponents() {
        mContext = this;
        mApiService = UtilsApi.getAPIService();

        btnDaftar = findViewById(R.id.btnGoRegister);
        btnMasuk = findViewById(R.id.btnSignin);
        txtEmail = findViewById(R.id.txtEmailLogin);
        txtPass = findViewById(R.id.txtPassLogin);

        sharedPrefManager = new SharedPrefManager(this);
    }

    private void buttonClick() {
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
                String email = txtEmail.getText().toString();
                String password =  txtPass.getText().toString();
                loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                requestLogin(email,password);
            }
        });
    }

    private void requestLogin(final String email, final String password) {
        mApiService.loginRequest(email,password)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            loading.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("error").equals("false")){
                                    // Jika login berhasil maka data nama yang ada di response API
                                    // akan diparsing ke activity selanjutnya.
                                    showMessage("BERHASIL LOGIN");
                                    String id = jsonRESULTS.getJSONObject("user").getString("id");
                                    String nama = jsonRESULTS.getJSONObject("user").getString("nama");
                                    String level_user = jsonRESULTS.getJSONObject("user").getString("level_user");
                                    String foto = jsonRESULTS.getJSONObject("user").getString("foto_profile");

                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_ID, id);
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_FOTO, foto);
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_NAMA, nama);
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_LEVEL, level_user);
                                    sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, true);

                                    if (level_user.equals("1")){
                                        startActivity(new Intent(mContext, Main2Activity.class)
                                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                                        finish();
                                    }else if (level_user.equals("2")){
                                        startActivity(new Intent(mContext, Main3Activity.class)
                                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                                        finish();
                                    }else {
                                        startActivity(new Intent(mContext, MainActivity.class));
                                        finish();
                                    }
                                } else {
                                    // Jika login gagal
                                    String error_message = jsonRESULTS.getString("error_msg");
                                    showMessage(error_message);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            loading.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                        loading.dismiss();
                    }
                });
    }

    private void showMessage(String message) {
        Toast.makeText(mContext,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }


}
