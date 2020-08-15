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

public class FragmentDaftarUlang extends Fragment {
    Button btnDaftar;
    TextView tvNama, tvSekolah, tvDivisi, status1, status2,status3;
    ProgressDialog loading;
    LinearLayout layout;
    Context mContext;
    BaseApiService mApiService;

    private String url = "";
    private String path = "";
    private static final int BUFFER_SIZE = 1024 * 2;
    private static final String IMAGE_DIRECTORY = "/inka";

    SharedPrefManager sharedPrefManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daftar_ulang,container,false);
        inItComponents(view);
        toDoDaftarUlang();

        return view;
    }

    private void toDoDaftarUlang() {
        sharedPrefManager = new SharedPrefManager(getActivity());
        String id = sharedPrefManager.getSPId();
        loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
        tampilData(id);
        cekPrakerin(id);

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

    private void inItComponents(View view) {
        mContext = getActivity();
        mApiService = UtilsApi.getAPIService();

        tvNama = view.findViewById(R.id.duNama);
        tvSekolah = view.findViewById(R.id.duSekolah);
        tvDivisi = view.findViewById(R.id.duDivisi);
        btnDaftar = view.findViewById(R.id.btnDaftarUlang);
        layout = view.findViewById(R.id.layoutDaftarUlang);
        status1 = view.findViewById(R.id.status1); // belum daftar ulang
        status2 = view.findViewById(R.id.status2); // belum verifikasi
        status3 = view.findViewById(R.id.status3); // sudah daftar ulang
    }

    private void cekPrakerin(String id) {
        mApiService.cekUlangPrakerin(id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    loading.dismiss();
                    try {
                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                        if (jsonRESULTS.getString("error").equals("false")){
                            String cekExisted = jsonRESULTS.getString("result");
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
        mApiService.tampilPendaftarPrakerin(id)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            loading.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("error").equals("false")){
                                    String nama = jsonRESULTS.getJSONObject("user").getString("nama");
                                    String sekolah = jsonRESULTS.getJSONObject("user").getString("sekolah");
                                    String divisi = jsonRESULTS.getJSONObject("user").getString("penempatan");

                                    tvNama.setText(nama);
                                    tvSekolah.setText(sekolah);
                                    tvDivisi.setText(divisi);
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
        Call<ResponseBody> call = getResponse.daftarUlangPrakerin(id, daftar);
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
                                startActivity(new Intent(getActivity(), Main2Activity.class));
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
