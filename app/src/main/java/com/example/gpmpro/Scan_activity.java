package com.example.gpmpro;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class Scan_activity extends AppCompatActivity {


    WebView wb;
    CardView bonafide;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        wb = findViewById(R.id.web);
        bonafide = findViewById(R.id.bonafide);

       bonafide.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

//               ScanCode();

               startActivity(new Intent(getApplicationContext(),BonafiteCertificateForm.class));
           }
       });

        WebSettings webSettings = wb.getSettings();
        webSettings.setJavaScriptEnabled(true);
        wb.setWebViewClient(new Callback());


    }

    private void ScanCode() {

        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);

    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(),result ->{
             if (result.getContents() !=null)
             {

                 if (result.getContents().equalsIgnoreCase("I Love You")){
//                     wb.loadUrl("https://docs.google.com/forms/d/e/1FAIpQLSfw4oBbMQQcEZzch9NqbE_A6BZRyW4jqfzx2bURRWsKK_cyOA/viewform?usp=sf_link ");
//                     bonafide.setVisibility(View.GONE);

                     startActivity(new Intent(getApplicationContext(),LeavingCertificateForm.class));

                 }
                 else {
                     Toast.makeText(this, "Wrong QR Code", Toast.LENGTH_SHORT).show();
                 }


             }
    });

    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
            return false;
        }
    }
}