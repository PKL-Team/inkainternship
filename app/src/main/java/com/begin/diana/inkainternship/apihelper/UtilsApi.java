package com.begin.diana.inkainternship.apihelper;

public class UtilsApi {
//    public static final String BASE_URL_API = "http://192.168.43.36/inka/";
    public static final String BASE_URL_API = "https://inkainternship.000webhostapp.com/android/";

    // Mendeklarasikan Interface BaseApiService
    public static BaseApiService getAPIService(){
        return RetrofitClient.getClient(BASE_URL_API).create(BaseApiService.class);
    }
}
