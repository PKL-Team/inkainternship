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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class FragmentEditInformasiPeserta extends Fragment {
    Button btnSimpan;
    EditText eNama, eId, eNilai;
    ProgressDialog loading;

    Context mContext;
    BaseApiService mApiService;

    SharedPrefManager sharedPrefManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_informasi_peserta,container,false);

        inItComponents(view);

        sharedPrefManager = new SharedPrefManager(getActivity());
        String level_user = sharedPrefManager.getSPLevel();
        String id = sharedPrefManager.getSPId();
        if (level_user.equals("1")){
            loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
            tampilDataPrakerin(id);
        }else if (level_user.equals("2")){
            loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
            tampilDataPkl(id);
        }

        return view;
    }

    private void inItComponents(View view) {
        mContext = getActivity();
        mApiService = UtilsApi.getAPIService();

        eNama = view.findViewById(R.id.editNama);
        eId = view.findViewById(R.id.editID);
        eNilai = view.findViewById(R.id.editNilai);
        btnSimpan = view.findViewById(R.id.btnSimpanInformasi);
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
                                    String raport = jsonRESULTS.getJSONObject("user").getString("raport");

                                    eNama.setText(nama);
                                    eId.setText(nis);
                                    eNilai.setText(raport);
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
                                    String ipk = jsonRESULTS.getJSONObject("user").getString("ipk");
                                    eNama.setText(nama);
                                    eId.setText(nim);
                                    eNilai.setText(ipk);

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

    @Override
    public void onResume(){
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                getFragmentManager().beginTransaction().replace(R.id.container_fragment,
                        new FragmentInformasiPeserta()).commit();
                return true;
            }
            return false;
            }
        });
    }
}
