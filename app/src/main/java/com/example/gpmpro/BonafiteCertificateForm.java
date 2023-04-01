package com.example.gpmpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.pdf417.encoder.PDF417;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import android.os.Handler;
import java.util.logging.LogRecord;

public class BonafiteCertificateForm extends AppCompatActivity {

    static final String rq = "Required!!";
    EditText et_name_of_std, et_name_of_middle, et_name_of_last, et_name_of_enroll_num, et_sub;
    TextView tv_upload_file,tv_date;
    Button btn_submit;
//    RadioButton rd_civil, rb_co, rb_it, rb_mech, rb_ee;

    String branchinfo,Yearinfo;
    RadioGroup rg,rg_year;

    // Date
    DatePickerDialog.OnDateSetListener onDateSetListener;

    String date;

    StorageReference storageReference;
    FirebaseFirestore firebaseFirestore;


    int SELECT_PDF = 1;
    ProgressDialog pd;

    String userID;



    Handler mHandler;

    Boolean checkItUpload = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonafite_certificate_form);
        getSupportActionBar().hide();



        storageReference = FirebaseStorage.getInstance().getReference();

        firebaseFirestore = FirebaseFirestore.getInstance();
        userID = UUID.randomUUID().toString();


        et_name_of_std = findViewById(R.id.Et_name_of_stud);
        et_name_of_middle = findViewById(R.id.Et_name_of_middle);
        et_name_of_last = findViewById(R.id.Et_name_of_last);
        et_name_of_enroll_num = findViewById(R.id.Et_enroll_num);
        et_sub = findViewById(R.id.Et_sub);
        rg = findViewById(R.id.ed);
        rg_year = findViewById(R.id.year);

        pd = ProgressDialog.show(this,"Loading ...","Please Wait",false,false);
        pd.dismiss();

        tv_upload_file = findViewById(R.id.Et_upload_file);
        tv_date = findViewById(R.id.Tv_date);




        tv_upload_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                // We will be redirected to choose pdf
                galleryIntent.setType("application/pdf");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent,"Select PDF Files.."),1);
            }
        });


        btn_submit = findViewById(R.id.submit_button);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int radioButtonID = rg.getCheckedRadioButtonId();
                View radioButton = rg.findViewById(radioButtonID);
                int idx = rg.indexOfChild(radioButton);
                RadioButton r = (RadioButton) rg.getChildAt(idx);
                branchinfo = r.getText().toString().trim();

            }
        });

        rg_year.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int radioButtonID = rg_year.getCheckedRadioButtonId();
                View radioButton = rg_year.findViewById(radioButtonID);
                int idc = rg_year.indexOfChild(radioButton);
                RadioButton r = (RadioButton)  rg_year.getChildAt(idc);
                Yearinfo = r.getText().toString().trim();
            }
        });


        //It for date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        BonafiteCertificateForm.this, com.google.android.material.R.style.MaterialAlertDialog_MaterialComponents_Picker_Date_Calendar,
                        onDateSetListener,year,month,day);

                //Theme_Material_Light_Dialog_MinWidth

                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.rgb(230,223,223)));

                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                if (month<=9){ //
                    date  = year+"/"+"0"+month+"/"+dayOfMonth;
                }
                else if (dayOfMonth<=9){
                    date  = year+"/"+month+"/"+"0"+dayOfMonth;
                }
                else {
                    date  = year+"/"+month+"/"+dayOfMonth;
                }
                if (month<=9 && dayOfMonth<=9){
                    date  = year+"/"+"0"+month+"/"+"0"+dayOfMonth;
                }
                tv_date.setText(date);
            }
        };

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());

        tv_date.setText(df.format(c));


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = et_name_of_std.getText().toString().trim();
                String middelname = et_name_of_middle.getText().toString().trim();
                String lastname = et_name_of_last.getText().toString().trim();
                String date = tv_date.getText().toString().trim();
                String enrollmentno = et_name_of_enroll_num.getText().toString().trim();
                String branch = branchinfo;
                String year = Yearinfo;
                String subject = et_sub.getText().toString().trim();

                if (name.isEmpty()) {
                    et_name_of_std.setError(rq);
                } else if (middelname.isEmpty()) {
                    et_name_of_middle.setError(rq);
                } else if (lastname.isEmpty()) {
                    et_name_of_last.setError(rq);
                } else if (date.equalsIgnoreCase("Date")) {
                    tv_date.setError(rq);
                } else if (enrollmentno.isEmpty()) {
                    et_name_of_enroll_num.setError(rq);
                }
                else if (enrollmentno.length()<10 || enrollmentno.length()>10 ) {
                    et_name_of_enroll_num.setError("Enter Valid Enrollment Number");
                }
                else if (branchinfo == null) {
                    Toast.makeText(BonafiteCertificateForm.this, "Branch is " + rq, Toast.LENGTH_SHORT).show();

                }
                else if (Yearinfo == null) {
                    Toast.makeText(BonafiteCertificateForm.this, "Year is " + rq, Toast.LENGTH_SHORT).show();

                }
                else if (subject.isEmpty()) {
                    et_sub.setError(rq);
                }
                else if (checkItUpload) {
                    Toast.makeText(BonafiteCertificateForm.this, "Upload your payment receipt ", Toast.LENGTH_SHORT).show();
                }

                else {
                    addDataToFirebase(name,middelname,lastname,date,enrollmentno,branch,year,subject);
                }

            }
        });
        getSupportActionBar().hide();

    }

    private void addDataToFirebase(String name, String middelname, String lastname, String date, String enrollmentno, String branch, String year, String subject) {
        Map<String,Object> map=new HashMap<>();
        map.put("Id",userID);
        map.put("Name",name);
        map.put("MiddleName",middelname);
        map.put("LastName",lastname);
        map.put("Date",date);
        map.put("EnrollmentNo",enrollmentno);
        map.put("Branch",branch);
        map.put("Year",year);
        map.put("Subject",subject);

        firebaseFirestore.collection("StudentBonafiteCertificateApplicationForm").document(userID).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(BonafiteCertificateForm.this, "You Application send Successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if (requestCode == 1 && resultCode == RESULT_OK && data!=null&&data.getData()!=null){
            uploadpdfToFirebase(data.getData());
        }
        else {
            pd.show();
            Toast.makeText(this, "Failed to add ", Toast.LENGTH_SHORT).show();
            pd.dismiss();
        }


    }

    private void uploadpdfToFirebase(Uri data) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        mHandler = new Handler();


        StorageReference reference = storageReference.child("Uploads/"+userID+"Bonafite.pdf");
        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete());
                        Uri uri = uriTask.getResult();
                        tv_upload_file.setText(uri.toString());
                        checkItUpload = false;
                        Toast.makeText(BonafiteCertificateForm.this, "Uplaoded", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progress = (100.0 *snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                progressDialog.setMessage("Uploading.. "+(int)progress+"%");
                            }
                        },100);

                    }
                });


    }

    
}
