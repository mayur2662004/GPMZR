package com.example.gpmpro;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Student_profile extends AppCompatActivity {
 TextView stud_name,stud_enroll,stud_date,stud_branch,stud_year;
 ImageView stud_verify;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        stud_date=findViewById(R.id.std_date);
        stud_name=findViewById(R.id.stud_name);
        stud_enroll=findViewById(R.id.stud_enrollment);
        stud_branch=findViewById(R.id.stud_branch);
        stud_year=findViewById(R.id.stud_year);

        stud_verify=findViewById(R.id.stud_varify);

    }
}