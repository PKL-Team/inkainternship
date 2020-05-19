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
    Button btnEdit;

    TextView tvNama, tvId, tvNilai;
    ProgressDialog loading;

    Context mContext;
    BaseApiService mApiService;

    SharedPrefManager sharedPrefManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_informasi_peserta,container,false);
        inItComponents(view);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.container_fragment,
                        new FragmentEditInformasiPeserta()).commit();
            }
        });

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

        tvNama = view.findViewById(R.id.ipNama);
        tvId = view.findViewById(R.id.ipID);
        tvNilai = view.findViewById(R.id.ipNilai);
        btnEdit = view.findViewById(R.id.btnEditInfo);
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

                                    tvNama.setText(nama);
                                    tvId.setText(nis);
                                    tvNilai.setText(raport);
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
                                    tvNama.setText(nama);
                                    tvId.setText(nim);
                                    tvNilai.setText(ipk);

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
