package com.begin.diana.inkainternship.Fragments;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.begin.diana.inkainternship.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentDaftarAwal2 extends Fragment {

    Spinner spinnerProv, spinnerKab, spinnerSekolah;

    private String[] listProv = {"Pilih Provinsi", "bla", "bla"};
    private String[] listKab = {"Pilih Kab.", "bla", "bla"};
    private String[] listSekolah = {"Pilih Sekolah", "bla", "bla"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daftar_awal_2,container,false);
        spinner(view);
        return view;
    }

    private void spinner(View view) {

        spinnerProv = view.findViewById(R.id.spProvinsi);
        spinnerKab = view.findViewById(R.id.spKabupaten);
        spinnerSekolah = view.findViewById(R.id.spNamaSekolah);

        // inisialiasi Array Adapter dengan memasukkan string array di atas
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, listProv);
        final ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, listKab);
        final ArrayAdapter<String> adapter3 = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, listSekolah);

        // mengeset Array Adapter tersebut ke Spinner
        spinnerProv.setAdapter(adapter);
        spinnerKab.setAdapter(adapter2);
        spinnerSekolah.setAdapter(adapter3);
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
                            new FragmentDaftarAwal()).commit();
                    return true;
                }
                return false;
            }
        });
    }
}
