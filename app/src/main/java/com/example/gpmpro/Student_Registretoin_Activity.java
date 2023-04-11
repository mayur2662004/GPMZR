package com.example.gpmpro;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.util.regex.Pattern;

public class Student_Registretoin_Activity extends AppCompatActivity {

    EditText rg_full_name,rg_email,rg_pass,rg_phone,log_email,log_pass;
    TextView rg_new_user,log_already_register;
    Button btn_reg,btn_log;

    StorageReference storageReference;
    FirebaseFirestore firebaseFirestore;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_registretoin);

        rg_full_name = findViewById(R.id.full_name_reg);
        rg_email = findViewById(R.id.email_reg);
        rg_pass = findViewById(R.id.password_reg);
        rg_phone = findViewById(R.id.phono_reg);

        rg_new_user = findViewById(R.id.tv_newuser_reg);
        btn_reg = findViewById(R.id.RegisterBtn);

        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = rg_full_name.getText().toString().trim();
                String email = rg_email.getText().toString().trim();
                String pass = rg_pass.getText().toString().trim();
                String phone = rg_phone.getText().toString().trim();
                if (name.isEmpty()){
                    rg_full_name.setError("Full Name is Required!");
                }
                else if (email.isEmpty()){
                    rg_email.setError("Email Address is Required !");
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    rg_email.setError("Invalid Email !! Please Correct Email Address");
                }
                else if (pass.isEmpty()){
                    rg_pass.setError("Password is Required");
                }
                else if (pass.length()<8){
                    rg_pass.setError("Password Must be greater than 8 Character");
                } else if(phone.isEmpty()){
                    rg_phone.setError("Phone No Required ! ");
                }
                else if (rg_phone.getText().toString().length()<8){
                    rg_phone.setError("Please Enter Valid Mobile No.");
                }
                else {
                    Toast.makeText(Student_Registretoin_Activity.this, "Registration is  Successfully.", Toast.LENGTH_SHORT).show();
//                    addData()
                }
            }
        });
    }

}