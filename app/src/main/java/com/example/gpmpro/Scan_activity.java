package com.example.gpmpro;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
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
               startActivity(new Intent(getApplicationContext(),AdminViewBonafiteData.class));

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

                     startActivity(new Intent(getApplicationContext(),BonafiteCertificateForm.class));

                 }
                 else {
                     Toast.makeText(this, "Wrong QR Code", Toast.LENGTH_SHORT).show();
                 }


             }
    });

    static class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
            return false;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.logOut:
                AlertDialog.Builder builder = new AlertDialog.Builder(Scan_activity.this);
                String[] options = {"Yes", "No"};
                builder.setTitle("Are you sure to Logout ?");


                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
                    }
                }).setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(),Student_Registretoin_Activity.class));
                        finish();
                        Toast.makeText(getApplicationContext(), "Logout", Toast.LENGTH_SHORT).show();
                    }
                }).create().show();
                return  true;
            default:

        }


        return super.onOptionsItemSelected(item);
    }



}