package com.begin.diana.inkainternship.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.begin.diana.inkainternship.Activities.Main2Activity;
import com.begin.diana.inkainternship.Activities.Main3Activity;
import com.begin.diana.inkainternship.Activities.MainActivity;
import com.begin.diana.inkainternship.R;
import com.begin.diana.inkainternship.SharedPrefManager;
import com.begin.diana.inkainternship.apihelper.BaseApiService;
import com.begin.diana.inkainternship.apihelper.UtilsApi;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;

public class FragmentDaftarUlangPkl extends Fragment {
    Button btnDaftar;
    LinearLayout layout;
    TextView tvNama, tvKampus, tvDivisi, scanSuratTugas, status1, status2, status3;
    ProgressDialog loading;

    private String url = "";
    private String path = "";
    private static final int BUFFER_SIZE = 1024 * 2;
    private static final String IMAGE_DIRECTORY = "/inka";

    private String cekExisted;

    Context mContext;
    BaseApiService mApiService;

    SharedPrefManager sharedPrefManager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daftar_ulang_pkl,container,false);
        inItComponents(view);

        sharedPrefManager = new SharedPrefManager(getActivity());
        String id = sharedPrefManager.getSPId();
        loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
        cekPkl(id);

        toDoDaftarUlang();
        return view;

    }

    private void inItComponents(View view) {
        mContext = getActivity();
        mApiService = UtilsApi.getAPIService();

        tvNama = view.findViewById(R.id.duNamaPkl);
        tvKampus = view.findViewById(R.id.duKampus);
        tvDivisi = view.findViewById(R.id.duDivisiPkl);
        btnDaftar = view.findViewById(R.id.btnDaftarUlangPkl);
        layout  = view.findViewById(R.id.layoutDaftarUlangPkl);
        status1 = view.findViewById(R.id.statusPkl1); // belum daftar ulang
        status2 = view.findViewById(R.id.statusPkl2); // belum verifikasi
        status3 = view.findViewById(R.id.statusPkl3); // sudah daftar ulang

    }

    private void cekPkl(String id) {
        mApiService.cekUlangPkl(id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    loading.dismiss();
                    try {
                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                        if (jsonRESULTS.getString("error").equals("false")){
                            cekExisted = jsonRESULTS.getString("result");
                            if (cekExisted.equals("1")){
                                layout.setVisibility(View.INVISIBLE);
                                status1.setVisibility(View.VISIBLE);
                                status2.setVisibility(View.INVISIBLE);
                                status3.setVisibility(View.INVISIBLE);
                            }else if(cekExisted.equals("2")) {
                                layout.setVisibility(View.INVISIBLE);
                                status1.setVisibility(View.INVISIBLE);
                                status2.setVisibility(View.VISIBLE);
                                status3.setVisibility(View.INVISIBLE);
                            }else if (cekExisted.equals("3")){
                                sharedPrefManager = new SharedPrefManager(getActivity());
                                String id = sharedPrefManager.getSPId();
                                loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                                tampilData(id);
                                layout.setVisibility(View.VISIBLE);
                                status1.setVisibility(View.INVISIBLE);
                                status2.setVisibility(View.INVISIBLE);
                                status3.setVisibility(View.INVISIBLE);
                            }else if (cekExisted.equals("4")){
                                layout.setVisibility(View.INVISIBLE);
                                status1.setVisibility(View.INVISIBLE);
                                status2.setVisibility(View.INVISIBLE);
                                status3.setVisibility(View.VISIBLE);
                            }
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

    private void tampilData(String id) {
        mApiService.tampilPendaftarPkl(id)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            loading.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("error").equals("false")){
                                    String nama = jsonRESULTS.getJSONObject("user").getString("nama");
                                    String kampus = jsonRESULTS.getJSONObject("user").getString("kampus");
                                    String jurusan = jsonRESULTS.getJSONObject("user").getString("jurusan");
                                    tvNama.setText(nama);
                                    tvKampus.setText(kampus);
                                    tvDivisi.setText(jurusan);
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

    private void toDoDaftarUlang() {

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPrefManager = new SharedPrefManager(getActivity());
                String id = sharedPrefManager.getSPId();
                String daftar = "sudah daftar ulang";
                loading = ProgressDialog.show(getActivity(), null, "Harap Tunggu...", true, false);
                daftarUlang(id,daftar);
            }
        });
    }

    private void daftarUlang(String id, String daftar) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UtilsApi.BASE_URL_API)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        BaseApiService getResponse = retrofit.create(BaseApiService.class);
        Call<ResponseBody> call = getResponse.daftarUlangPkl(
                id, daftar);
        Log.d("assss","asss");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    Log.i("debug", "onResponse: BERHASIL");
                    loading.dismiss();
                    try {
                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                        if (jsonRESULTS.getString("error").equals("false")){
                            showMessage("BERHASIL DAFTAR ULANG");
                            String id = jsonRESULTS.getJSONObject("user").getString("id");
                            if (id != null){
                                startActivity(new Intent(getActivity(), Main3Activity.class));
                                getActivity().finish();
                            }
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

    private void showMessage(String message){
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume(){
        super.onResume();
        sharedPrefManager = new SharedPrefManager(getActivity());
        String level_user = sharedPrefManager.getSPLevel();
        if (level_user.equals("1")){
            getView().setFocusableInTouchMode(true);
            getView().requestFocus();
            getView().setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                        startActivity(new Intent(getActivity(), Main2Activity.class));
                        getActivity().finish();
                        return true;
                    }
                    return false;
                }
            });
        }else if (level_user.equals("2")){
            getView().setFocusableInTouchMode(true);
            getView().requestFocus();
            getView().setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                        startActivity(new Intent(getActivity(), Main3Activity.class));
                        getActivity().finish();
                        return true;
                    }
                    return false;
                }
            });
        }else {
            getView().setFocusableInTouchMode(true);
            getView().requestFocus();
            getView().setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        getActivity().finish();
                        return true;
                    }
                    return false;
                }
            });
        }

    }
}
