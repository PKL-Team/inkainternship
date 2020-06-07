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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.begin.diana.inkainternship.Activities.Main2Activity;
import com.begin.diana.inkainternship.Activities.Main3Activity;
import com.begin.diana.inkainternship.Activities.MainActivity;
import com.begin.diana.inkainternship.R;
import com.begin.diana.inkainternship.SharedPrefManager;
import com.begin.diana.inkainternship.apihelper.BaseApiService;
import com.begin.diana.inkainternship.apihelper.UtilsApi;
import com.begin.diana.inkainternship.spinner.AdapterSpinner;
import com.begin.diana.inkainternship.spinner.PilihSpinnerModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentDaftarAwal extends Fragment {
    Button btnLihat, btnLanjutkan;
    TextView tvTahun, tvKuota, status;
    LinearLayout layout;
    private String cekExisted;

    SharedPrefManager sharedPrefManager;

    //===============spinner
    Spinner spinnerJurusan, spinnerPeriode;
    ProgressDialog loading;
    AdapterSpinner adapterJur, adapterPer;
    List<PilihSpinnerModel> listJur = new ArrayList<PilihSpinnerModel>();
    List<PilihSpinnerModel> listPer = new ArrayList<PilihSpinnerModel>();
    String id_jur, id_per, ids;
    Context mContext;
    BaseApiService mApiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daftar_awal,container,false);
        inItComponents(view);
        spinner(view);
        toDoDaftarAwal();

        return view;
    }

    private void inItComponents(View view) {
        mContext = getActivity();
        mApiService = UtilsApi.getAPIService();

        btnLihat = view.findViewById(R.id.btnLihat);
        btnLanjutkan = view.findViewById(R.id.btnLanjutkan);
        tvTahun = view.findViewById(R.id.tahun);
        tvKuota = view.findViewById(R.id.kuota);
        status = view.findViewById(R.id.statusAwal);
        layout = view.findViewById(R.id.layoutDaftarAwal);
    }

    private void toDoDaftarAwal() {
        sharedPrefManager = new SharedPrefManager(getActivity());
        String id = sharedPrefManager.getSPId();
        cekPrakerin(id);

        btnLihat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id_jur.isEmpty() || id_per.isEmpty()){
                    showMessage("Pilih Jurusan atau Periode untuk melihat");
                }else {
                    reqLihat(id_jur,id_per);
                }
            }
        });

        btnLanjutkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvKuota.getText().toString().equals("...") || tvKuota.getText().toString().equals("0") || tvKuota.getText().toString().isEmpty() ){
                    showMessage("Kuota tidak mencukupi untuk melakukan pendaftaran");
                }else {
                    if (id_jur.equals("3") || id_jur.equals("7")){
                        String divisi = "Dukungan dan Infrastruktur Bisnis";
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_DIVISI, divisi);
                    }else if (id_jur.equals("1") || id_jur.equals("9")){
                        String divisi = "Perakitan";
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_DIVISI, divisi);
                    }else if (id_jur.equals("5") || id_jur.equals("6") || id_jur.equals("8")){
                        String divisi = "Pengelasan";
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_DIVISI, divisi);
                    }else if (id_jur.equals("4")){
                        String divisi = "Multimedia";
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_DIVISI, divisi);
                    }else if (id_jur.equals("2")){
                        String divisi = "Human Capital";
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_DIVISI, divisi);
                    }
                    getFragmentManager().beginTransaction().replace(R.id.container_fragment,
                            new FragmentDaftarAwal2()).commit();
                }
            }
        });



    }

    private void spinner(View view) {
        spinnerJurusan = view.findViewById(R.id.spJurusan);
        spinnerPeriode = view.findViewById(R.id.spPeriode);

        spinnerJurusan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                id_jur = listJur.get(position).getId();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinnerPeriode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                id_per = listPer.get(position).getId();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);

        adapterJur = new AdapterSpinner(getActivity(), listJur);
        spinnerJurusan.setAdapter(adapterJur);
        callJurusan();

        adapterPer = new AdapterSpinner(getActivity(), listPer);
        spinnerPeriode.setAdapter(adapterPer);
        callPeriode();
    }

    private void reqLihat(String id_jur, String id_per) {
        mApiService.requestLihatPrakerin(id_jur,id_per)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
//                            loading.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("error").equals("false")){
                                    String tahun = jsonRESULTS.getJSONObject("user").getString("tahun");
                                    String kuota = jsonRESULTS.getJSONObject("user").getString("kuota");
                                    tvTahun.setText(tahun);
                                    tvKuota.setText(kuota);
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
//                            loading.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
//                        loading.dismiss();
                    }
                });
    }

    private void callJurusan() {
        mApiService.getJurPrakerin().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    loading.dismiss();
                    try {
                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                        JSONArray dataArray = jsonRESULTS.getJSONArray("data");

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject dataobj = dataArray.getJSONObject(i);
                            PilihSpinnerModel item = new PilihSpinnerModel();

                            item.setId(dataobj.getString("id_jur_smk"));
                            item.setNama(dataobj.getString("jurusan_smk"));

                            listJur.add(item);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    adapterJur.notifyDataSetChanged();
                }else {
                    loading.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
            }
        });
    }

    private void callPeriode() {
        mApiService.getPeriode().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    loading.dismiss();
                    try {
                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                        JSONArray dataArray = jsonRESULTS.getJSONArray("data");

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject dataobj = dataArray.getJSONObject(i);
                            PilihSpinnerModel item = new PilihSpinnerModel();

                            item.setId(dataobj.getString("id_periode"));
                            item.setNama(dataobj.getString("bulan"));

                            listPer.add(item);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    adapterPer.notifyDataSetChanged();
                }else {
                    loading.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
            }
        });
    }

    private void cekPrakerin(String id) {
        mApiService.cekAwalPrakerin(id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    loading.dismiss();
                    try {
                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                        if (jsonRESULTS.getString("error").equals("false")){
                            cekExisted = jsonRESULTS.getString("result");
                            if (cekExisted.equals("Yes")){
                                layout.setVisibility(View.INVISIBLE);
                                status.setVisibility(View.VISIBLE);
                            }else {
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
