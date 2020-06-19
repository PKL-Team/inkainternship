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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentKelolaAkun extends Fragment {
    Button btnLogout;
    TextView namaUser, emailUser, magangUser, telpUser;
    ImageView imageUser;

    ProgressDialog loading;

    Context mContext;
    BaseApiService mApiService;

    SharedPrefManager sharedPrefManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kelola_akun,container,false);

        inItComponents(view);

        sharedPrefManager = new SharedPrefManager(getActivity());
        String id = sharedPrefManager.getSPId();
        loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
        tampilAkun(id);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, false);
                sharedPrefManager.saveSPString(sharedPrefManager.SP_ID, "");
                sharedPrefManager.saveSPString(sharedPrefManager.SP_NAMA, "");
                sharedPrefManager.saveSPString(sharedPrefManager.SP_EMAIL, "");
                sharedPrefManager.saveSPString(sharedPrefManager.SP_LEVEL, "");
                sharedPrefManager.saveSPString(sharedPrefManager.SP_FOTO, "");
                startActivity(new Intent(getActivity(), MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                getActivity().finish();
            }
        });
        return view;
    }

    private void inItComponents(View view) {

        mContext = getActivity();
        mApiService = UtilsApi.getAPIService();

        btnLogout = view.findViewById(R.id.btnLogout);
        namaUser = view.findViewById(R.id.akunNama);
        emailUser = view.findViewById(R.id.akunEmail);
        magangUser = view.findViewById(R.id.akunMagang);
        telpUser = view.findViewById(R.id.akunTelp);
        imageUser = view.findViewById(R.id.akunImageUser);
    }


    private void tampilAkun(String id) {
        mApiService.tampilAkunRequest(id)
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
//                                    String nama = jsonRESULTS.getJSONObject("user").getString("nama");
                                    String email = jsonRESULTS.getJSONObject("user").getString("email");
//                                    String jenis_kegiatan = jsonRESULTS.getJSONObject("user").getString("jenis_kegiatan");
//                                    String no_telp = jsonRESULTS.getJSONObject("user").getString("no_telp");
//                                    String foto = jsonRESULTS.getJSONObject("user").getString("foto_profile");

//                                    namaUser.setText(nama);
                                    emailUser.setText(email);
//                                    magangUser.setText(jenis_kegiatan);
//                                    telpUser.setText(no_telp);

//                                    RequestOptions requestOptions = new RequestOptions();
//                                    requestOptions.skipMemoryCache(true);
//                                    requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
//                                    requestOptions.placeholder(R.drawable.ic_account_circle_white_24dp);
//                                    requestOptions.error(R.drawable.ic_account_circle_white_24dp);
//                                    Glide.with(getActivity())
//                                            .load(UtilsApi.BASE_URL_API+foto)
//                                            .apply(requestOptions)
//                                            .into(imageUser);

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
                            new FragmentPengaturan()).commit();
                    return true;
                }
                return false;
            }
        });
    }

}
