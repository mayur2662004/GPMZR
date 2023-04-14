package com.example.gpmpro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
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

    TextView date,subject,name,enrollment,branch,year;
    String id,verify,emailId;


    WebView pdf;

    ProgressDialog pd;

    StorageReference storageReference;

    ImageView verifyImage;
    TextView showStatus;

    String pdfURl;
    FirebaseFirestore firebaseFirestore;

    Dialog dialog,dialogReject;


    MaterialButton no,yes;

    TextInputEditText note;
    MaterialButton btn;
    
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
        }


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

}