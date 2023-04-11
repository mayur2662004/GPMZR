package com.example.gpmpro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AdminViewSpecificData extends AppCompatActivity {

    TextView date,subject,name,enrollment,branch,year;
    String id,verify,emailId;


    WebView pdf;

    ProgressDialog pd;

    StorageReference storageReference;

    ImageView verifyImage;
    TextView showStatus;

    String pdfURl;


    Dialog dialog,dialogReject;


    MaterialButton no,yes;

    FirebaseFirestore firebaseFirestore;

    TextInputEditText note;
    MaterialButton btn;


    @SuppressLint({"MissingInflatedId", "ResourceAsColor"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_specific_data);

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


        }


        if (verify.equalsIgnoreCase("False")){
            verifyImage.setImageResource(R.drawable.pending_logo);
            showStatus.setText("Pending");
            showStatus.setTextColor(Color.parseColor("#FFEB3B"));//  red
            makeStudentApproved(id);
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
        }

        verifyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String veri = showStatus.getText().toString().trim();
                if (veri.equalsIgnoreCase("Pending")){

                    makeStudentApproved(id);
                }
                else  if (veri.equalsIgnoreCase("Rejected")){
                    Toast.makeText(AdminViewSpecificData.this, "Rejected!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(AdminViewSpecificData.this, "Already Approved", Toast.LENGTH_SHORT).show();
                }
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
                    Toast.makeText(AdminViewSpecificData.this, "Url Exception : "+e.getMessage(), Toast.LENGTH_LONG).show();
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

    private void makeStudentApproved(String id) {

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.verify_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.back_background));
        dialog.show();

        no = dialog.findViewById(R.id.no);
        yes = dialog.findViewById(R.id.yes);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                approved(id);
                dialog.dismiss();
                pd.show();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                rejectThisUser(emailId);

            }
        });

    }

    private void rejectThisUser(String emailId) {
        dialogReject = new Dialog(this);
        dialogReject.setContentView(R.layout.note_for_reject);
        dialogReject.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        dialogReject.getWindow().setBackgroundDrawable(getDrawable(R.drawable.back_background));
        dialogReject.show();

        note = dialogReject.findViewById(R.id.note);
        btn = dialogReject.findViewById(R.id.submit);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String no = note.getText().toString().trim();
                if (no.isEmpty()){
                    note.setError("Required!!");
                }
                else {
                    reject(no);
                    sendEmail(no);
                    dialogReject.dismiss();
                }
            }
        });

    }

    private void approved(String id) {
        firebaseFirestore.collection("StudentBonafiteCertificateApplicationForm").document(id).update("Verify","True").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(AdminViewSpecificData.this, "Enrollment no : "+enrollment+" is Approved Successfully!!!", Toast.LENGTH_SHORT).show();
                pd.dismiss();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AdminViewSpecificData.this, "", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        });
    }

    private void reject(String note) {

        Map<String,Object> map=new HashMap<>();
        map.put("Verify","Rejected");
        map.put("Note",note);

        firebaseFirestore.collection("StudentBonafiteCertificateApplicationForm").document(id).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(AdminViewSpecificData.this, "Enrollment no : "+enrollment+" is Rejected!!!", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AdminViewSpecificData.this, "", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        });
    }

    private void sendEmail(String note) {

        DocumentReference documentReference = firebaseFirestore.collection("Users")
                .document(emailId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                Toast.makeText(AdminViewSpecificData.this,  value.getString("email"), Toast.LENGTH_SHORT).show();
                String message = "GOVERNMENT POLYTECHNIC MURTIZAPUR...\n\n"+name.getText().toString()+" your bonafite application form is rejected due to "+note.toUpperCase()+"\n\nPlease contact to GOVERNMENT POLYTECHNIC MURTIZAPUR\n\nTHANKS!!";
                JavaMailAPI javaMailAPI = new JavaMailAPI(AdminViewSpecificData.this, value.getString("email"), "Your Bonafite Application form is rejected", message);
                javaMailAPI.execute();

            }
        });




    }
}