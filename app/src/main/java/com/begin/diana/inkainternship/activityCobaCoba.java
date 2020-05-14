package com.begin.diana.inkainternship;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.begin.diana.inkainternship.Activities.LoginActivity;
import com.begin.diana.inkainternship.Activities.Main2Activity;
import com.begin.diana.inkainternship.Activities.MainActivity;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.shockwave.pdfium.PdfDocument;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

public class activityCobaCoba extends AppCompatActivity {

    Button btnTanpa, btnDengan, btnCoba;
    TextView scanFile;

    private int pageNumber = 0;

    private String pdfFileName;
    private PDFView pdfView;
    public ProgressDialog pDialog;
    public static final int FILE_PICKER_REQUEST_CODE = 1;
    private String pdfPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coba_coba);

        btnTanpa = findViewById(R.id.btnTanpaLogin);
        btnDengan = findViewById(R.id.btnDenganLogin);
        btnCoba = findViewById(R.id.btnLogin);
        scanFile = findViewById(R.id.scanFile);

        btnTanpa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent open = new Intent(activityCobaCoba.this, MainActivity.class);
                finish();
                startActivity(open);
            }
        });

        btnDengan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent open = new Intent(activityCobaCoba.this, Main2Activity.class);
                finish();
                startActivity(open);
            }
        });

        btnCoba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent coba = new Intent(activityCobaCoba.this, LoginActivity.class);
                finish();
                startActivity(coba);
            }
        });

        scanFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchPicker();
            }
        });
    }

    private void launchPicker() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(intent,1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            // Get the Uri of the selected file
            Uri uri = data.getData();
            String uriString = uri.toString();
            File myFile = new File(uriString);

//            String path = getFilePathFromURI(this, uri);

            String fileName = getFileName(uri);
            scanFile.setText(fileName);
//            Log.d("ioooo",path);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public static String getFileName(Uri uri) {
        if (uri == null) return null;
        String fileName = null;
        String path = uri.getPath();
        int cut = path.lastIndexOf('/');
        if (cut != -1) {
            fileName = path.substring(cut + 1);
        }
        return fileName;
    }

}
