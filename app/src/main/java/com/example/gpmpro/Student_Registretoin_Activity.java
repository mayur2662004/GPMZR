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

    CardView cd_reg,cd_log;

    StorageReference storageReference;

    FirebaseFirestore firebaseFirestore;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_registretoin);

        cd_reg=findViewById(R.id.cd_reg);
        cd_log=findViewById(R.id.lod_cd);


        rg_full_name = findViewById(R.id.full_name_reg);
        rg_email = findViewById(R.id.email_reg);
        rg_pass = findViewById(R.id.password_reg);
        rg_phone = findViewById(R.id.phono_reg);

        log_email = findViewById(R.id.email_log);
        log_pass = findViewById(R.id.pass_log);

        rg_new_user = findViewById(R.id.tv_newuser_reg);
        log_already_register = findViewById(R.id.tv_alredy_reg);

        btn_log = findViewById(R.id.login_Btn);
        btn_reg = findViewById(R.id.RegisterBtn);


        rg_new_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               cd_reg.setVisibility(view.VISIBLE);
               cd_log.setVisibility(view.GONE);
            }
        });

        log_already_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cd_reg.setVisibility(view.GONE);
                cd_log.setVisibility(view.VISIBLE);
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
                else if (rg_phone.getText().toString().length()<8){
                    rg_phone.setError("Please Enter Valid Mobile No.");

                }
                else {
                    Toast.makeText(Student_Registretoin_Activity.this, "Registration is  Successfully.", Toast.LENGTH_SHORT).show();
//                    addData()

                }

                btn_log.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (log_email.getText().toString().isEmpty()){
                            log_email.setError("Email is Required");
                        }
                        else if (!log_email.getText().toString().contains("@") || !log_email.getText().toString().contains(".")){
                            log_email.setError("Invalid Email !! Please Correct Email Address");
                        }
                        else if (log_pass.getText().toString().isEmpty()){
                            log_pass.setError("Password is Required");

                        }
                        else if (log_pass.getText().toString().length()<8){
                            log_pass.setError("Password Must be greater than 8 Character");
                        }
                        else {
                            Toast.makeText(Student_Registretoin_Activity.this, "Login in Successfully.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Student_Registretoin_Activity.this,Scan_activity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });

            }
        });







    }

}