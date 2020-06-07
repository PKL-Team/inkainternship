package com.begin.diana.inkainternship.apihelper;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
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

    // Fungsi ini untuk memanggil API http://inkainternship.000webhostapp.com/android/daftarAwalPrakerin.php
    @Multipart
    @POST("daftarAwalPrakerin.php")
    Call<ResponseBody> daftarAwalPrakerin(
            @Part MultipartBody.Part file, @Part("filename1") RequestBody name,
            @Part MultipartBody.Part file2, @Part("filename2") RequestBody name2,
            @Part MultipartBody.Part file3, @Part("filename3") RequestBody name3,
            @Part MultipartBody.Part file4, @Part("filename4") RequestBody name4,
            @Part("id") String id,
            @Part("nama") String nama,
            @Part("nis") String nis,
            @Part("raport") String raport,
            @Part("sekolah") String sekolah,
            @Part("divisi") String divisi);


    // Fungsi ini untuk memanggil API http://inkainternship.000webhostapp.com/android/daftarAwalPkl.php
    @Multipart
    @POST("daftarAwalPkl.php")
    Call<ResponseBody> daftarAwalPkl(
            @Part MultipartBody.Part file, @Part("filename1") RequestBody name,
            @Part MultipartBody.Part file2, @Part("filename2") RequestBody name2,
            @Part MultipartBody.Part file3, @Part("filename3") RequestBody name3,
            @Part MultipartBody.Part file4, @Part("filename4") RequestBody name4,
            @Part MultipartBody.Part file5, @Part("filename5") RequestBody name5,
            @Part("id") String id,
            @Part("nama") String nama,
            @Part("nim") String nim,
            @Part("ipk") String ipk,
            @Part("kampus") String kampus,
            @Part("divisi") String divisi);


    // Fungsi ini untuk memanggil API http://inkainternship.000webhostapp.com/android/tampilPendaftarPrakerin.php
    @FormUrlEncoded
    @POST("tampilPendaftarPrakerin.php")
    Call<ResponseBody> tampilPendaftarPrakerin(@Field("id") String id);

    // Fungsi ini untuk memanggil API http://inkainternship.000webhostapp.com/android/tampilPendaftarPkl.php
    @FormUrlEncoded
    @POST("tampilPendaftarPkl.php")
    Call<ResponseBody> tampilPendaftarPkl(@Field("id") String id);

    // Fungsi ini untuk memanggil API http://inkainternship.000webhostapp.com/android/daftarUlangPrakerin.php
    @Multipart
    @POST("daftarUlangPrakerin.php")
    Call<ResponseBody> daftarUlangPrakerin(
            @Part MultipartBody.Part file, @Part("filename") RequestBody name,
            @Part("id") String id);

    // Fungsi ini untuk memanggil API http://inkainternship.000webhostapp.com/android/daftarUlangPkl.php
    @Multipart
    @POST("daftarUlangPkl.php")
    Call<ResponseBody> daftarUlangPkl(
            @Part MultipartBody.Part file, @Part("filename") RequestBody name,
            @Part("id") String id);

    // Fungsi ini untuk memanggil API http://inkainternship.000webhostapp.com/android/editInformasiPrakerin.php
    @FormUrlEncoded
    @POST("editInformasiPrakerin.php")
    Call<ResponseBody> editInformasiPrakerin(@Field("id") String id,
                                             @Field("nama") String nama,
                                             @Field("nis") String nis,
                                             @Field("raport") String raport);

    // Fungsi ini untuk memanggil API http://inkainternship.000webhostapp.com/android/editInformasiPkl.php
    @FormUrlEncoded
    @POST("editInformasiPkl.php")
    Call<ResponseBody> editInformasiPkl(@Field("id") String id,
                                        @Field("nama") String nama,
                                        @Field("nim") String nim,
                                        @Field("ipk") String ipk);

    @FormUrlEncoded
    @POST("cekAwalPrakerin.php")
    Call<ResponseBody> cekAwalPrakerin(@Field("id") String id);

    @FormUrlEncoded
    @POST("cekUlangPrakerin.php")
    Call<ResponseBody> cekUlangPrakerin(@Field("id") String id);

    @FormUrlEncoded
    @POST("cekAwalPkl.php")
    Call<ResponseBody> cekAwalPkl(@Field("id") String id);

    @FormUrlEncoded
    @POST("cekUlangPkl.php")
    Call<ResponseBody> cekUlangPkl(@Field("id") String id);

    @GET("getJurPrakerin.php")
    Call<ResponseBody> getJurPrakerin();

    @GET("getJur.php")
    Call<ResponseBody> getJur();

    @GET("getPeriode.php")
    Call<ResponseBody> getPeriode();

    @GET("getProv.php")
    Call<ResponseBody> getProv();

    @FormUrlEncoded
    @POST("getKab.php")
    Call<ResponseBody> getKab(@Field("id") String id);

    @FormUrlEncoded
    @POST("getPt.php")
    Call<ResponseBody> getPt(@Field("id") String id);

    @FormUrlEncoded
    @POST("getSekolah.php")
    Call<ResponseBody> getSekolah(@Field("id") String id);

    @FormUrlEncoded
    @POST("reqLihatPkl.php")
    Call<ResponseBody> requestLihatPkl(
            @Field("id_jurusan") String id_jurusan,
            @Field("id_periode") String id_periode);

    @FormUrlEncoded
    @POST("reqLihatPrakerin.php")
    Call<ResponseBody> requestLihatPrakerin(
            @Field("id_jurusan") String id_jurusan,
            @Field("id_periode") String id_periode);
}
