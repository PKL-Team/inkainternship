package com.begin.diana.inkainternship;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

public class DaftarActivity extends AppCompatActivity {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        final Spinner List = findViewById(R.id.listItem);
        button = findViewById(R.id.btnRegister);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu dropDownMenu = new PopupMenu(getApplicationContext(), button);
                dropDownMenu.getMenuInflater().inflate(R.menu.dropdown_menu, dropDownMenu.getMenu());
                dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Toast.makeText(getApplicationContext(), "You have clicked " + menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        return true;
                    }
                });
                dropDownMenu.show();
            }
        });


    }

}
