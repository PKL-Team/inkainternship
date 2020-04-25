package com.begin.diana.inkainternship.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.begin.diana.inkainternship.R;
import com.begin.diana.inkainternship.activityCobaCoba;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {
    private static int splashInterval = 2000;
    private final String TAG = this.getClass().getName().toUpperCase();
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (mAuth.getCurrentUser() != null){
                    jenisMagang();
                }else {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            }
        }, splashInterval);
    }

    private void jenisMagang() {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = rootRef.child("Users");
        Log.v("USERID", userRef.getKey());

        userRef.addValueEventListener(new ValueEventListener() {
            String email = user.getEmail();
            String jenisMagang;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot keyId: dataSnapshot.getChildren()) {
                    if (keyId.child("email").getValue().equals(email)) {
                        jenisMagang = keyId.child("jenisMagang").getValue(String.class);
                        if (jenisMagang.equals("Prakerin (SMK)")){
                            startActivity(new Intent(getApplicationContext(), Main2Activity.class));
                            finish();
                        }else if (jenisMagang.equals("PKL (Mahasiswa)")){
                            startActivity(new Intent(getApplicationContext(), Main3Activity.class));
                            finish();
                        }else {
                        }
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

}
