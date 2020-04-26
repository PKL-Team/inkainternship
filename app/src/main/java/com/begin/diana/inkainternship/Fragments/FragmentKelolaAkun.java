package com.begin.diana.inkainternship.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.begin.diana.inkainternship.Activities.MainActivity;
import com.begin.diana.inkainternship.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentKelolaAkun extends Fragment {
    Button btnLogout;
    FirebaseAuth mAuth;
    FirebaseUser user;
    TextView namaUser, emailUser, magangUser;
    ImageView imageUser;
    private final String TAG = this.getClass().getName().toUpperCase();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kelola_akun,container,false);

        btnLogout = view.findViewById(R.id.btnLogout);
        namaUser = view.findViewById(R.id.akunNama);
        emailUser = view.findViewById(R.id.akunEmail);
        magangUser = view.findViewById(R.id.akunMagang);
        imageUser = view.findViewById(R.id.akunImageUser);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = rootRef.child("Users");
        Log.v("USERID", userRef.getKey());


        //tampilkan image user dg Glide
        Glide.with(this).load(user.getPhotoUrl()).into(imageUser);

        userRef.addValueEventListener(new ValueEventListener() {
            String email = user.getEmail();
            String nama, jenisMagang;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot keyId: dataSnapshot.getChildren()) {
                    if (keyId.child("email").getValue().equals(email)) {
                        nama = keyId.child("nama").getValue(String.class);
                        jenisMagang = keyId.child("jenisMagang").getValue(String.class);
                        break;
                    }
                }
                namaUser.setText(nama);
                emailUser.setText(email);
                magangUser.setText(jenisMagang);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
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
                    getFragmentManager().beginTransaction().replace(R.id.container_fragment,
                            new FragmentPengaturan()).commit();
                    return true;
                }
                return false;
            }
        });
    }

}
