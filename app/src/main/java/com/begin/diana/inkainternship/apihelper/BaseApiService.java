package com.begin.diana.inkainternship.apihelper;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface BaseApiService {

    // Fungsi ini untuk memanggil API http://inkainternship.000webhostapp.com/android/login.php
    @FormUrlEncoded
    @POST("login.php")
    Call<ResponseBody> loginRequest(@Field("email") String email,
                                    @Field("password") String password);

    // Fungsi ini untuk memanggil API http://inkainternship.000webhostapp.com/android/register.php
    @FormUrlEncoded
    @POST("register.php")
    Call<ResponseBody> registerRequest(@Field("nama") String nama,
                                       @Field("email") String email,
                                       @Field("password") String password,
                                       @Field("no_telp") String no_telp,
                                       @Field("jenis_kegiatan") String jenis_kegiatan,
                                       @Field("foto_profile") String foto_profile);

    // Fungsi ini untuk memanggil API http://inkainternship.000webhostapp.com/android/tampilAkun.php
    @FormUrlEncoded
    @POST("tampilAkun.php")
    Call<ResponseBody> tampilAkunRequest(@Field("id") String id);

    String IMAGEURL = "http://192.168.43.36/inka/";
    @Multipart
    @POST("upload_file_prakerin.php")
    Call<String> uploadPdf(
            @Part MultipartBody.Part file, @Part("filename1") RequestBody name,
            @Part MultipartBody.Part file2, @Part("filename2") RequestBody name2,
            @Part MultipartBody.Part file3, @Part("filename3") RequestBody name3,
            @Part MultipartBody.Part file4, @Part("filename4") RequestBody name4
    );
}
