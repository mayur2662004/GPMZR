package com.example.gpmpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Student_Login_Activity extends AppCompatActivity {

    EditText log_email,log_pass;

    TextView log_already_register;
    Button btn_log;

    FirebaseAuth firebaseAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);


        firebaseAuth = FirebaseAuth.getInstance();

        log_email = findViewById(R.id.email_log);
        log_pass = findViewById(R.id.pass_log);
        log_already_register = findViewById(R.id.tv_newuser_reg);

        btn_log = findViewById(R.id.login_Btn);

        log_already_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Student_Registretoin_Activity.class));
                finish();
            }
        });


        btn_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = log_email.getText().toString().trim();
                String pass = log_pass.getText().toString().trim();
                if (log_email.getText().toString().isEmpty()){
                    log_email.setError("Email is Required");
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    log_email.setError("Invalid Email !! Please Correct Email Address");
                }
                else if (pass.isEmpty()){
                    log_pass.setError("Password is Required");

                }
                else if (pass.length()<8){
                    log_pass.setError("Password Must be greater than 8 Character");
                }
                else {
                    checkLogin(email,pass);
                }
            }
        });
    }

    private void checkLogin(String email, String pass) {
        firebaseAuth.signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),Scan_activity.class));
                            finish();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Login Failed ",Toast.LENGTH_LONG).show();


                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
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