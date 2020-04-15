package com.begin.diana.inkainternship;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentDaftarAwal extends Fragment {
    Button btnLanjutkan;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daftar_awal,container,false);

        btnLanjutkan = view.findViewById(R.id.btnLanjutkanDA);
        btnLanjutkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.container_fragment,
                        new FragmentDaftarAwal2()).commit();
            }
        });
        return view;
    }
}
