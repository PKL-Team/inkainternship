package com.begin.diana.inkainternship.Fragments;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.begin.diana.inkainternship.R;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentTentangAplikasi extends Fragment {
    FirebaseAuth mAuth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tentang_aplikasi,container,false);
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null){
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
        }else {
            getView().setFocusableInTouchMode(true);
            getView().requestFocus();
            getView().setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                        getFragmentManager().beginTransaction().replace(R.id.container_fragment,
                                new FragmentBeranda()).commit();
                        return true;
                    }
                    return false;
                }
            });
        }

    }
}
