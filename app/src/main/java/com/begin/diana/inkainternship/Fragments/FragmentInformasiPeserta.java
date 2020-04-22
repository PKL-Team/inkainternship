package com.begin.diana.inkainternship.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.begin.diana.inkainternship.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentInformasiPeserta extends Fragment {
    Button btnEdit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_informasi_peserta,container,false);

        btnEdit = view.findViewById(R.id.btnEditInfo);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.container_fragment,
                        new FragmentEditInformasiPeserta()).commit();
            }
        });
        return view;
    }
}
