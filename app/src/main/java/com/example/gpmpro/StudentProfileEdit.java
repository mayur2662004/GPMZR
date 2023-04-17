package com.example.gpmpro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.dynamite.DynamiteModule;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class StudentProfileEdit extends AppCompatActivity {

    CardView showPdf;


    WebView pdf;

    ProgressDialog pd;

    StorageReference storageReference;

    ImageView verifyImage;
    TextView showStatus;

    String pdfURl;
    FirebaseFirestore firebaseFirestore;

    Dialog dialogReject,dialogPdf;

    TextView date,subject,name,enrollment,branch,year;
    String id,verify,emailId;

    String nameS,middleS,lastNameS;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile_edit);

        firebaseFirestore = FirebaseFirestore.getInstance();

        date = findViewById(R.id.date);
        subject = findViewById(R.id.sub);
        name = findViewById(R.id.Name);
        enrollment = findViewById(R.id.enroll);
        branch = findViewById(R.id.branch);
        year = findViewById(R.id.year);
        pdf = findViewById(R.id.pdf);
        verifyImage = findViewById(R.id.verify);
        showStatus = findViewById(R.id.showStatus);
        pd = new ProgressDialog(this);

        pdf.getSettings().setJavaScriptEnabled(true);
        pdf.getSettings().setBuiltInZoomControls(true);
        pdf.getSettings().setDisplayZoomControls(true);



        if (Build.VERSION.SDK_INT >= 19) {
            pdf.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            pdf.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }


        pd.show();

        storageReference = FirebaseStorage.getInstance().getReference();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString("Id");
            verify = bundle.getString("Verify");
            date.setText(bundle.getString("Date"));
            name.setText(bundle.getString("Name") + " " + bundle.getString("MiddleName") + " " + bundle.getString("LastName"));
            enrollment.setText(bundle.getString("EnrollmentNo"));
            branch.setText(bundle.getString("Branch"));
            year.setText(bundle.getString("Year"));
            subject.setText(bundle.getString("Subject"));
            emailId= bundle.getString("UserId");

            nameS = bundle.getString("Name");
            middleS = bundle.getString("MiddleName");
            lastNameS = bundle.getString("LastName");


        }


        if (verify.equalsIgnoreCase("False")){
            verifyImage.setImageResource(R.drawable.pending_logo);
            showStatus.setText("Pending");
            showStatus.setTextColor(Color.parseColor("#FFEB3B"));//  red

        }
        else if (verify.equalsIgnoreCase("True")){
            verifyImage.setImageResource(R.drawable.yes_logo_new);
            showStatus.setText("Approved");
            showStatus.setTextColor(Color.parseColor("#12AD2B")); // green
        }
        else if (verify.equalsIgnoreCase("Rejected")){
            verifyImage.setImageResource(R.drawable.wrong_logo_new);
            showStatus.setText("Rejected");
            showStatus.setTextColor(Color.parseColor("#ED1D0E")); // green
            showDialogIn();
        }

        showPdf = findViewById(R.id.showPdf);

        showPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDilog();
            }
        });



        StorageReference profileRef = storageReference.child("Uploads/" + id + "Bonafite.pdf");
        pd.show();
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                try {
                    pdfURl = URLEncoder.encode(String.valueOf(uri),"UTF-8");
                }
                catch (Exception e){
                    Toast.makeText(StudentProfileEdit.this, "Url Exception : "+e.getMessage(), Toast.LENGTH_LONG).show();
                }
                pdf.loadUrl("https://docs.google.com/gview?embedded=true&url="+pdfURl);
                pd.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
            }
        });


    }

    private void showDilog() {
        dialogPdf = new Dialog(this);
        dialogPdf.setContentView(R.layout.pdf);
        dialogPdf.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        dialogPdf.getWindow().setBackgroundDrawable(getDrawable(R.drawable.back_background));
        dialogPdf.show();

        ImageView imageView = dialogPdf.findViewById(R.id.close);
        WebView pdf = dialogPdf.findViewById(R.id.pdf);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPdf.dismiss();
            }
        });

        StorageReference profileRef = storageReference.child("Uploads/" + id + "Bonafite.pdf");
        pd.show();
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                try {
                    pdfURl = URLEncoder.encode(String.valueOf(uri),"UTF-8");
                }
                catch (Exception e){
                    Toast.makeText(StudentProfileEdit.this, "Url Exception : "+e.getMessage(), Toast.LENGTH_LONG).show();
                }
                pdf.loadUrl("https://docs.google.com/gview?embedded=true&url="+pdfURl);
                pd.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
            }
        });

    }

    private void showDialogIn() {
        dialogReject = new Dialog(this);
        dialogReject.setContentView(R.layout.reject_pop);
        dialogReject.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        dialogReject.getWindow().setBackgroundDrawable(getDrawable(R.drawable.back_background));
        dialogReject.show();
        MaterialButton edit = dialogReject.findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(StudentProfileEdit.this,BonafiteCertificateForm.class);
                intent.putExtra("Name",nameS);
                intent.putExtra("MiddleName",middleS);
                intent.putExtra("LastName",lastNameS);
                intent.putExtra("Subject",subject.getText().toString().trim());
                intent.putExtra("EnrollmentNo",enrollment.getText().toString().trim());
                intent.putExtra("Branch",branch.getText().toString().trim());
                intent.putExtra("Year",year.getText().toString().trim());
                intent.putExtra("EmailId",emailId);
                startActivity(intent);

            }
        });
    }

}