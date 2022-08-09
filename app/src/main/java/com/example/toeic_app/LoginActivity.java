package com.example.toeic_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    private EditText email, password;
    private Button loginB;
    private TextView forgotPassB, singUpB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginB = findViewById(R.id.LoginButton);
        forgotPassB = findViewById(R.id.forgot);
        singUpB = findViewById(R.id.singUp);

        loginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ValidateData()){
                    login();
                }
            }
        });

        singUpB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

   //  Kiêm tra nhập thông itn
    private boolean ValidateData(){
        if(email.getText().toString().isEmpty()){
            email.setError("Vui lòng nhập email");
            return false;
        }
        if(password.getText().toString().isEmpty()){
            password.setError("Vui lòng nhập mật khẩu");
            return false;
        }
        return true;
    }

    private void login(){

    }


}