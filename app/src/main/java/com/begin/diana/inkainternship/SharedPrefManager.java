package com.begin.diana.inkainternship;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    public static final String SP_INKAINTERNSHIP_APP = "spInkaApp";

    public static final String SP_ID = "spId";
    public static final String SP_NAMA = "spNama";
    public static final String SP_EMAIL = "spEmail";
    public static final String SP_LEVEL = "spLevel";
    public static final String SP_FOTO = "spFoto";
    public static final String SP_DIVISI = "spDivisi";
    public static final String SP_IDKUOTA = "spIDKuota";

    public static final String SP_SUDAH_LOGIN = "spSudahLogin";

    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    public SharedPrefManager(Context context){
        sp = context.getSharedPreferences(SP_INKAINTERNSHIP_APP, Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    public void saveSPString(String keySP, String value){
        spEditor.putString(keySP, value);
        spEditor.commit();
    }

    public void saveSPInt(int keySP, int value){
        spEditor.putInt(String.valueOf(keySP), value);
        spEditor.commit();
    }

    public void saveSPBoolean(String keySP, boolean value){
        spEditor.putBoolean(keySP, value);
        spEditor.commit();
    }

    public String getSPNama(){
        return sp.getString(SP_NAMA, "");
    }

    public String getSPEmail(){
        return sp.getString(SP_EMAIL, "");
    }

    public String getSPLevel(){
        return sp.getString(SP_LEVEL, "");
    }

    public String getSPFoto(){
        return sp.getString(SP_FOTO, "");
    }

    public String getSPId(){
        return sp.getString(SP_ID, "");
    }

    public  String getSpDivisi(){
        return  sp.getString(SP_DIVISI, "");
    }

    public  String getSpIDKuota(){
        return  sp.getString(SP_IDKUOTA, "");
    }


    public Boolean getSPSudahLogin(){
        return sp.getBoolean(SP_SUDAH_LOGIN, false);
    }
}
