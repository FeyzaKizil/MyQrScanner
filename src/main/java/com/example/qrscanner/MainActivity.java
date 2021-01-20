package com.example.qrscanner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView textView;
    IntentResult forlink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);

        textView = findViewById(R.id.textView);

        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, PackageManager.PERMISSION_GRANTED);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
    }

    public void onClick(View view) {

               switch(view.getId()) {
            case R.id.button1 :
                IntentIntegrator intentIntegrator = new IntentIntegrator(this);
                intentIntegrator.setCaptureActivity(CaptureAct.class);
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                intentIntegrator.setPrompt("Scanning Code...");
                intentIntegrator.initiateScan();
                break;
            case R.id.button2 :
                if (forlink != null) {
                    if(Patterns.WEB_URL.matcher(forlink.getContents()).matches()) {
                        // Open URL
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(forlink.getContents()));
                        startActivity(browserIntent);
                    }
                }
                else{
                    textView.setText("Sorry, don't have link to go :( ");
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        this.forlink = intentResult;

        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                textView.setText("Sorry, cancelled :( ");
            }
            else {
                textView.setText(intentResult.getContents());

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}