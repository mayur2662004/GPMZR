package com.example.gpmpro;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Adimin_Login_Activity extends AppCompatActivity {

    EditText et_username,et_password;
    Button btn_admin_login;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adimin_login);

        et_username=findViewById(R.id.username_log_admin);
        et_password=findViewById(R.id.pass_log_admin);

        btn_admin_login=findViewById(R.id.login_Btn_adimin);

        btn_admin_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = "gpmzr2023";
                String id_helper = et_username.getText().toString();
                String pass = et_password.getText().toString();
                if(!id_helper.equalsIgnoreCase(username)){
                    et_username.setError("Please enter valid user name");
                }
                else if (!pass.equalsIgnoreCase("1241")){
                    et_password.setError("Please enter valid password");
                }
                else {
                    startActivity(new Intent(getApplicationContext(),AdminViewBonafiteData.class));
                    finish();
                }
            }
        });
    }
}