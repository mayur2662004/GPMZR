package com.example.gpmpro;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Student_Registretoin_Activity extends AppCompatActivity {

    EditText rg_full_name,rg_email,rg_pass,rg_phone,log_email,log_pass;
    TextView rg_new_user,log_already_register;
    Button btn_reg,btn_log;


    StorageReference storageReference;

    FirebaseFirestore firebaseFirestore;

    FirebaseAuth firebaseAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_registretoin);


        firebaseAuth = FirebaseAuth.getInstance();

        firebaseFirestore = FirebaseFirestore.getInstance();


        rg_full_name = findViewById(R.id.full_name_reg);
        rg_email = findViewById(R.id.email_reg);
        rg_pass = findViewById(R.id.password_reg);
        rg_phone = findViewById(R.id.phono_reg);
        log_already_register = findViewById(R.id.tv_alredy_reg);



        rg_new_user = findViewById(R.id.tv_newuser_reg);

        btn_reg = findViewById(R.id.RegisterBtn);


        log_already_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Student_Login_Activity.class));
                finish();
            }
        });


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
                else if (phone.isEmpty()){
                    rg_phone.setError("Please Enter Valid Mobile No.");

                }
                else {
                    addToFirebase(name,email,pass,phone);
                }



            }
        });







    }

    private void addToFirebase(String name, String email, String pass, String phone) {

        firebaseAuth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Register Successfully", Toast.LENGTH_SHORT).show();
                            String userId = firebaseAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = firebaseFirestore.collection("Users")
                                    .document(userId);
                            Map<String,Object> user = new HashMap<>();

                            user.put("Id",userId);
                            user.put("fullName",name);
                            user.put("email",email);
                            user.put("PhoneNo",phone);

                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG,"successes");
                                    startActivity(new Intent(getApplicationContext(),Scan_activity.class));
                                    finish();
                                }
                            });


                        }
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }

    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),Scan_activity.class));
            finish();
        }
    }
}