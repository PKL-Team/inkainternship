package com.begin.diana.inkainternship.Fragments;

import android.Manifest;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.begin.diana.inkainternship.Activities.LoginActivity;
import com.begin.diana.inkainternship.Activities.Main2Activity;
import com.begin.diana.inkainternship.Activities.Main3Activity;
import com.begin.diana.inkainternship.R;
import com.begin.diana.inkainternship.SharedPrefManager;
import com.begin.diana.inkainternship.apihelper.BaseApiService;
import com.begin.diana.inkainternship.apihelper.UtilsApi;
import com.begin.diana.inkainternship.spinner.AdapterSpinner;
import com.begin.diana.inkainternship.spinner.PilihSpinnerModel;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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

public class FragmentDaftarAwalPkl2 extends Fragment {

    Spinner spinnerProv, spinnerKab, spinnerKampus, spinnerJur;

    EditText inputNama, inputNim, inputIpk;
    TextView scan1,scan2,scan3,scan4,scan5;
    private String url = "";
    private String path1 = "";
    private String path2 = "";
    private String path3 = "";
    private String path4 = "";
    private String path5 = "";

    private static final int BUFFER_SIZE = 1024 * 2;
    private static final String IMAGE_DIRECTORY = "/inka";

    Button btnDaftarAwal;
    BaseApiService mApiService;
    SharedPrefManager sharedPrefManager;
    ProgressDialog loading;

    //===============spinner
    AdapterSpinner adapter1, adapter2, adapter3, adapter4;
    List<PilihSpinnerModel> listProv = new ArrayList<PilihSpinnerModel>();
    List<PilihSpinnerModel> listKab = new ArrayList<PilihSpinnerModel>();
    List<PilihSpinnerModel> listPt = new ArrayList<PilihSpinnerModel>();
    List<PilihSpinnerModel> listJur = new ArrayList<PilihSpinnerModel>();
    String nama_prov, nama_kab, id_pt, id_jur, nama_jur;
    Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daftar_awal_pkl_2,container,false);
        inItComponents(view);
        spinner(view);
        requestMultiplePermissions();
        scanClick();

        btnDaftarAwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPrefManager = new SharedPrefManager(getActivity());
                String id = sharedPrefManager.getSPId();
                String nama = inputNama.getText().toString();
                String nim =  inputNim.getText().toString();
                String ipk = inputIpk.getText().toString();
                String id_kuota = sharedPrefManager.getSpIDKuota();

                if (id.isEmpty() || nama.isEmpty() || nim.isEmpty() || ipk.isEmpty() || id_kuota.isEmpty()){
                    showMessage("Mohon lengkapi semua field masukan");
                }
//                else if (path1.isEmpty() || path2.isEmpty() || path3.isEmpty() || path4.isEmpty()){
//                    showMessage("Beberapa/Semua File Scan belum dipilih");
//                }
                else {
                    loading = ProgressDialog.show(getActivity(), null, "Harap Tunggu...", true, false);
//                    uploadPDF(path1,path2,path3,path4,path5,id,nama,nim,ipk,id_pt,id_jur,id_kuota);
                    upload(id,nama,nim,ipk,id_pt,id_jur,id_kuota);
                }
            }
        });

        return view;
    }

    private void inItComponents(View view){
        mContext = getActivity();
        mApiService = UtilsApi.getAPIService();

        scan1 = view.findViewById(R.id.scan1);
        scan2 = view.findViewById(R.id.scan2);
        scan3 = view.findViewById(R.id.scan3);
        scan4 = view.findViewById(R.id.scan4);
        scan5 = view.findViewById(R.id.scan5);
        inputNama = view.findViewById(R.id.daNamaPkl);
        inputNim = view.findViewById(R.id.daNim);
        inputIpk = view.findViewById(R.id.daIpk);
        btnDaftarAwal = view.findViewById(R.id.btnDaftarAwal2Pkl);
    }

    private void spinner(View view) {
        spinnerProv = view.findViewById(R.id.spProvinsi);
        spinnerKab = view.findViewById(R.id.spKabupaten);
        spinnerKampus = view.findViewById(R.id.spNamaKampus);
        spinnerJur = view. findViewById(R.id.spNamaJurusan);

        callProv();
        adapter1 = new AdapterSpinner(getActivity(), listProv);
        spinnerProv.setAdapter(adapter1);

        spinnerProv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                listKab.clear();
                nama_prov = listProv.get(position).getId();
                callKab(nama_prov);
                adapter2 = new AdapterSpinner(getActivity(), listKab);
                spinnerKab.setAdapter(adapter2);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        spinnerKab.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                listPt.clear();
                nama_kab = listKab.get(position).getId();
                callPt(nama_kab);
                adapter3 = new AdapterSpinner(getActivity(), listPt);
                spinnerKampus.setAdapter(adapter3);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        spinnerKampus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                listJur.clear();
                id_pt = listPt.get(position).getId();
                callJur(id_pt);
                adapter4 = new AdapterSpinner(getActivity(), listJur);
                spinnerJur.setAdapter(adapter4);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        spinnerJur.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_jur = listJur.get(position).getId();
                nama_jur = listJur.get(position).getNama();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void callProv() {
        mApiService.getProv().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                        JSONArray dataArray = jsonRESULTS.getJSONArray("data");

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject dataobj = dataArray.getJSONObject(i);
                            PilihSpinnerModel item = new PilihSpinnerModel();

                            item.setId(dataobj.getString("nama_prov"));
                            item.setNama(dataobj.getString("nama_prov"));

                            listProv.add(item);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    adapter1.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
            }
        });
    }

    private void callKab(String id) {
        mApiService.getKab(id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                        JSONArray dataArray = jsonRESULTS.getJSONArray("data");

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject dataobj = dataArray.getJSONObject(i);
                            PilihSpinnerModel item = new PilihSpinnerModel();

                            item.setId(dataobj.getString("nama_kab"));
                            item.setNama(dataobj.getString("nama_kab"));

                            listKab.add(item);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    adapter2.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
            }
        });
    }

    private void callPt(String id) {
        mApiService.getPt(id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                        JSONArray dataArray = jsonRESULTS.getJSONArray("data");

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject dataobj = dataArray.getJSONObject(i);
                            PilihSpinnerModel item = new PilihSpinnerModel();

                            item.setId(dataobj.getString("id_pt"));
                            item.setNama(dataobj.getString("nama_pt"));

                            listPt.add(item);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    adapter3.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
            }
        });
    }

    private void callJur(String id) {
        mApiService.getJurUniv(id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                        JSONArray dataArray = jsonRESULTS.getJSONArray("data");

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject dataobj = dataArray.getJSONObject(i);
                            PilihSpinnerModel item = new PilihSpinnerModel();

                            item.setId(dataobj.getString("id_jur_univ"));
                            item.setNama(dataobj.getString("jurusan_univ"));

                            listJur.add(item);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    adapter4.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
            }
        });
    }

    //fungsi2 untuk mengambil dan upload file pdf

    private void scanClick() {
        scan1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                startActivityForResult(intent,1);
            }
        });
        scan2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                startActivityForResult(intent,2);
            }
        });
        scan3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                startActivityForResult(intent,3);
            }
        });
        scan4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                startActivityForResult(intent,4);
            }
        });
        scan5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                startActivityForResult(intent,5);
            }
        });
    }

    private void upload(final String id,final String nama,final String nis,final String raport,
                           final String id_pt, final String id_jur, final  String id_kuota){

        String pdfname = String.valueOf(Calendar.getInstance().getTimeInMillis());

        //Create a file object using file path
        File file1 = new File(path1);
        RequestBody requestBody1 = RequestBody.create(MediaType.parse("*/*"), file1);
        MultipartBody.Part fileToUpload1 = MultipartBody.Part.createFormData("filename1", file1.getName(), requestBody1);
        RequestBody filename1 = RequestBody.create(MediaType.parse("text/plain"), pdfname);

        File file2 = new File(path2);
        RequestBody requestBody2 = RequestBody.create(MediaType.parse("*/*"), file2);
        MultipartBody.Part fileToUpload2 = MultipartBody.Part.createFormData("filename2", file2.getName(), requestBody2);
        RequestBody filename2 = RequestBody.create(MediaType.parse("text/plain"), pdfname);

        File file3 = new File(path3);
        RequestBody requestBody3 = RequestBody.create(MediaType.parse("*/*"), file3);
        MultipartBody.Part fileToUpload3 = MultipartBody.Part.createFormData("filename3", file3.getName(), requestBody3);
        RequestBody filename3 = RequestBody.create(MediaType.parse("text/plain"), pdfname);

        File file4 = new File(path4);
        RequestBody requestBody4 = RequestBody.create(MediaType.parse("*/*"), file4);
        MultipartBody.Part fileToUpload4 = MultipartBody.Part.createFormData("filename4", file4.getName(), requestBody4);
        RequestBody filename4 = RequestBody.create(MediaType.parse("text/plain"), pdfname);

        File file5 = new File(path5);
        RequestBody requestBody5 = RequestBody.create(MediaType.parse("*/*"), file5);
        MultipartBody.Part fileToUpload5 = MultipartBody.Part.createFormData("filename5", file5.getName(), requestBody5);
        RequestBody filename5 = RequestBody.create(MediaType.parse("text/plain"), pdfname);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UtilsApi.BASE_URL_API)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        BaseApiService getResponse = retrofit.create(BaseApiService.class);
        Call<ResponseBody> call = getResponse.daftarAwalPkl2(
                id, nama, nis, raport, id_pt, id_jur, id_kuota,
                fileToUpload1, filename1,
                fileToUpload2, filename2,
                fileToUpload3, filename3,
                fileToUpload4, filename4,
                fileToUpload5, filename5 );
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
                            showMessage("BERHASIL DAFTAR");
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Get the Uri of the selected file
            Uri uri = data.getData();
            String uriString = uri.toString();
            File myFile = new File(uriString);

            String fileName = getFileName(uri);
            scan1.setText(fileName);

            path1 = getFilePathFromURI(getActivity(),uri);
            Log.d("ioooo",path1);
        }
        if (requestCode == 2 && resultCode == RESULT_OK) {
            // Get the Uri of the selected file
            Uri uri = data.getData();
            String uriString = uri.toString();
            File myFile = new File(uriString);

            String fileName = getFileName(uri);
            scan2.setText(fileName);

            path2 = getFilePathFromURI(getActivity(),uri);
            Log.d("ioooo",path2);
        }
        if (requestCode == 3 && resultCode == RESULT_OK) {
            // Get the Uri of the selected file
            Uri uri = data.getData();
            String uriString = uri.toString();
            File myFile = new File(uriString);

            String fileName = getFileName(uri);
            scan3.setText(fileName);

            path3 = getFilePathFromURI(getActivity(),uri);
            Log.d("ioooo",path3);
        }
        if (requestCode == 4 && resultCode == RESULT_OK) {
            // Get the Uri of the selected file
            Uri uri = data.getData();
            String uriString = uri.toString();
            File myFile = new File(uriString);

            String fileName = getFileName(uri);
            scan4.setText(fileName);

            path4 = getFilePathFromURI(getActivity(),uri);
            Log.d("ioooo",path4);
        }
        if (requestCode == 5 && resultCode == RESULT_OK) {
            // Get the Uri of the selected file
            Uri uri = data.getData();
            String uriString = uri.toString();
            File myFile = new File(uriString);

            String fileName = getFileName(uri);
            scan5.setText(fileName);

            path5 = getFilePathFromURI(getActivity(),uri);
            Log.d("ioooo",path5);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static String getFilePathFromURI(Context context, Uri contentUri) {
        //copy file and send new file path
        String fileName = getFileName(contentUri);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }
        if (!TextUtils.isEmpty(fileName)) {
            File copyFile = new File(wallpaperDirectory + File.separator + fileName);
            // create folder if not exists

            copy(context, contentUri, copyFile);
            return copyFile.getAbsolutePath();
        }
        return null;
    }

    public static String getFileName(Uri uri) {
        if (uri == null) return null;
        String fileName = null;
        String path = uri.getPath();
        int cut = path.lastIndexOf('/');
        if (cut != -1) {
            fileName = path.substring(cut + 1);
        }
        return fileName;
    }

    public static void copy(Context context, Uri srcUri, File dstFile) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);
            if (inputStream == null) return;
            OutputStream outputStream = new FileOutputStream(dstFile);
            copystream(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int copystream(InputStream input, OutputStream output) throws Exception, IOException {
        byte[] buffer = new byte[BUFFER_SIZE];

        BufferedInputStream in = new BufferedInputStream(input, BUFFER_SIZE);
        BufferedOutputStream out = new BufferedOutputStream(output, BUFFER_SIZE);
        int count = 0, n = 0;
        try {
            while ((n = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
                out.write(buffer, 0, n);
                count += n;
            }
            out.flush();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                Log.e(e.getMessage(), String.valueOf(e));
            }
            try {
                in.close();
            } catch (IOException e) {
                Log.e(e.getMessage(), String.valueOf(e));
            }
        }
        return count;
    }

    private void  requestMultiplePermissions(){
        Dexter.withActivity(getActivity())
                .withPermissions(

                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getActivity(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getActivity(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void showMessage(String message){
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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
                            new FragmentDaftarAwalPkl()).commit();
                    return true;
                }
                return false;
            }
        });
    }
}
