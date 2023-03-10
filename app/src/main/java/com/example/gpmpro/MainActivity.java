package com.example.gpmpro;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class MainActivity extends AppCompatActivity {


    WebView wb;
    CardView bonafide;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wb = findViewById(R.id.web);
        bonafide = findViewById(R.id.bonafide);

       bonafide.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               ScanCode();
           }
       });

        WebSettings webSettings = wb.getSettings();
        webSettings.setJavaScriptEnabled(true);
        wb.setWebViewClient(new Callback());




    }

    private void ScanCode() {

        ScanOptions options = new ScanOptions();
        options.setPrompt("Volum up to flash on");
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);

    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(),result ->{
             if (result.getContents() !=null)
             {

                 if (result.getContents().equalsIgnoreCase("I Love You")){
                     wb.loadUrl("https://docs.google.com/forms/d/e/1FAIpQLSeI6Bn4uw6B6y7fgr0OYIVtmnJkZ73WdFh4ESILuE1DBDa8CA/viewform?usp=pp_url");
                     bonafide.setVisibility(View.GONE);
                 }
                 else {
                     Toast.makeText(this, "Wrong QR Code", Toast.LENGTH_SHORT).show();
                 }

//                 AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this);
//                 builder.setTitle("Result");
//                 builder.setMessage(result.getContents());
//                 builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                     @Override
//                     public void onClick(DialogInterface dialogInterface, int i) {
//                         dialogInterface.dismiss();
//                     }
//                 }).show();

             }
    });

    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
            return false;
        }
    }
}