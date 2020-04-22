package com.begin.diana.inkainternship.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.begin.diana.inkainternship.Activities.MainActivity;
import com.begin.diana.inkainternship.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentPengaturan extends Fragment {
    Button  btnKelola, btnTentang;
    TextView title;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pengaturan,container,false);
        title = view.findViewById(R.id.toolbarTitle);

        btnKelola = view.findViewById(R.id.btnKelolaAkun);
        btnKelola.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                title.setText("Pengaturan | Kelola Akun");
                getFragmentManager().beginTransaction().replace(R.id.container_fragment,
                        new FragmentKelolaAkun()).commit();
            }
        });

        btnTentang = view.findViewById(R.id.btnTentangAplikasi);
        btnTentang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                title.setText("Pengaturan | Tentang Aplikasi");
                getFragmentManager().beginTransaction().replace(R.id.container_fragment,
                        new FragmentTentangAplikasi()).commit();
            }
        });
        return view;
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
                    Intent back = new Intent(getActivity(), MainActivity.class);
                    getActivity().finish();
                    startActivity(back);
                    return true;
                }
                return false;
            }
        });
    }
}
