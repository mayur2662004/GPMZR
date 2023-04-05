package com.example.gpmpro;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Student_Login_Activity extends AppCompatActivity {

    EditText log_email,log_pass;

    TextView log_already_register;
    Button btn_log;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        log_email = findViewById(R.id.email_log);
        log_pass = findViewById(R.id.pass_log);
        log_already_register = findViewById(R.id.tv_alredy_reg);

        btn_log = findViewById(R.id.login_Btn);


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
                    Toast.makeText(Student_Login_Activity.this, "Login in Successfully.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Student_Login_Activity.this,Scan_activity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}