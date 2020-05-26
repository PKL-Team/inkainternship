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
import android.widget.Spinner;

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
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentDaftarAwalPkl extends Fragment {
    Button btnLihat, btnLanjutkan;

    SharedPrefManager sharedPrefManager;


    //===============spinner
    Spinner spinnerJurusan, spinnerPeriode;
    ProgressDialog loading;
    AdapterSpinner adapterJur, adapterPer;
    List<PilihSpinnerModel> listJur = new ArrayList<PilihSpinnerModel>();
    List<PilihSpinnerModel> listPer = new ArrayList<PilihSpinnerModel>();
    String x, y, ids;
    Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daftar_awal_pkl,container,false);
        btnLihat = view.findViewById(R.id.btnLihatPkl);
        btnLanjutkan = view.findViewById(R.id.btnLanjutkanDA);
        mContext = getActivity();
        spinner(view);
        btnLanjutkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.container_fragment,
                        new FragmentDaftarAwalPkl2()).commit();
            }
        });
        return view;
    }

    private void spinner(View view) {
        spinnerJurusan = view.findViewById(R.id.spJurusan);
        spinnerPeriode = view.findViewById(R.id.spPeriode);
        spinnerJurusan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                x = listJur.get(position).getId();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        spinnerPeriode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                y = listPer.get(position).getId();
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

    private void callJurusan() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UtilsApi.BASE_URL_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BaseApiService getResponse = retrofit.create(BaseApiService.class);
        Call<ResponseBody> call = getResponse.getJur();
        Log.d("assss","asss");
        call.enqueue(new Callback<ResponseBody>() {
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

                            item.setId(dataobj.getString("id_jur_pt"));
                            item.setNama(dataobj.getString("jurusan_pt"));

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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UtilsApi.BASE_URL_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BaseApiService getResponse = retrofit.create(BaseApiService.class);
        Call<ResponseBody> call = getResponse.getPeriode();
        Log.d("assss","asss");
        call.enqueue(new Callback<ResponseBody>() {
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
