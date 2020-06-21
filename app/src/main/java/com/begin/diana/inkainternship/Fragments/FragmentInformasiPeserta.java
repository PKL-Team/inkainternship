package com.begin.diana.inkainternship.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentInformasiPeserta extends Fragment {

    TextView tvNama, tvId, tvSekolah, tvJurusan, tvPenempatan, tvTahun, tvPeriode, status;
    LinearLayout layout;
    ProgressDialog loading;

    Context mContext;
    BaseApiService mApiService;

    SharedPrefManager sharedPrefManager;
    private String cekExisted;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_informasi_peserta,container,false);
        inItComponents(view);

        sharedPrefManager = new SharedPrefManager(getActivity());
        String level_user = sharedPrefManager.getSPLevel();
        String id = sharedPrefManager.getSPId();
        if (level_user.equals("SISWA")){
            loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
            cekPrakerin(id);
        }else if (level_user.equals("MAHASISWA")){
            loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
            cekPkl(id);
        }


        return view;
    }

    private void inItComponents(View view) {
        mContext = getActivity();
        mApiService = UtilsApi.getAPIService();

        tvNama = view.findViewById(R.id.ipNama);
        tvId = view.findViewById(R.id.ipID);
        tvSekolah = view.findViewById(R.id.ipSekolah);
        tvJurusan = view.findViewById(R.id.ipJuruan);
        tvPenempatan = view.findViewById(R.id.ipPenempatan);
        tvPeriode = view.findViewById(R.id.ipPeriode);
        tvTahun = view.findViewById(R.id.ipTahun);

        status = view.findViewById(R.id.status);
        layout = view.findViewById(R.id.layoutInformasiPeserta);
    }

    private void tampilDataPrakerin(String id) {
        mApiService.tampilPendaftarPrakerin(id)
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
                                    String nama = jsonRESULTS.getJSONObject("user").getString("nama");
                                    String nis = jsonRESULTS.getJSONObject("user").getString("nis");
                                    String periode = jsonRESULTS.getJSONObject("user").getString("periode");
                                    String tahun = jsonRESULTS.getJSONObject("user").getString("tahun");
                                    String kampus = jsonRESULTS.getJSONObject("user").getString("sekolah");
                                    String jurusan = jsonRESULTS.getJSONObject("user").getString("jurusan");
                                    String penempatan = jsonRESULTS.getJSONObject("user").getString("penempatan");

                                    tvNama.setText(nama);
                                    tvId.setText(nis);
                                    tvSekolah.setText(kampus);
                                    tvJurusan.setText(jurusan);
                                    tvPenempatan.setText(penempatan);
                                    tvPeriode.setText(periode);
                                    tvTahun.setText(tahun);
                                } else {
                                    // Jika login gagal
                                    String error_message = jsonRESULTS.getString("error_msg");
                                    Toast.makeText(mContext, error_message, Toast.LENGTH_SHORT).show();
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

    private void tampilDataPkl(String id) {
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
                                    String nim = jsonRESULTS.getJSONObject("user").getString("nim");
                                    String periode = jsonRESULTS.getJSONObject("user").getString("periode");
                                    String tahun = jsonRESULTS.getJSONObject("user").getString("tahun");
                                    String kampus = jsonRESULTS.getJSONObject("user").getString("kampus");
                                    String jurusan = jsonRESULTS.getJSONObject("user").getString("jurusan");
                                    String penempatan = jsonRESULTS.getJSONObject("user").getString("penempatan");

                                    tvNama.setText(nama);
                                    tvId.setText(nim);
                                    tvSekolah.setText(kampus);
                                    tvJurusan.setText(jurusan);
                                    tvPenempatan.setText(penempatan);
                                    tvPeriode.setText(periode);
                                    tvTahun.setText(tahun);

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
                            if (cekExisted.equals("1") || cekExisted.equals("2") || cekExisted.equals("3")){
                                layout.setVisibility(View.INVISIBLE);
                                status.setVisibility(View.VISIBLE);
                            }else if (cekExisted.equals("4")){
                                sharedPrefManager = new SharedPrefManager(getActivity());
                                String id = sharedPrefManager.getSPId();
                                loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                                tampilDataPkl(id);
                                layout.setVisibility(View.VISIBLE);
                                status.setVisibility(View.INVISIBLE);
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

    private void cekPrakerin(String id) {
        mApiService.cekUlangPrakerin(id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    loading.dismiss();
                    try {
                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                        if (jsonRESULTS.getString("error").equals("false")){
                            cekExisted = jsonRESULTS.getString("result");
                            if (cekExisted.equals("1") || cekExisted.equals("2") || cekExisted.equals("3")){
                                layout.setVisibility(View.INVISIBLE);
                                status.setVisibility(View.VISIBLE);
                            }else if (cekExisted.equals("4")){
                                sharedPrefManager = new SharedPrefManager(getActivity());
                                String id = sharedPrefManager.getSPId();
                                loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                                tampilDataPrakerin(id);
                                layout.setVisibility(View.VISIBLE);
                                status.setVisibility(View.INVISIBLE);
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
