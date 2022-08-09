package com.example.toeic_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class SignUpActivity extends AppCompatActivity {
    private EditText username, email, password, confirmPass;
    private Button signUpButton;
    private ImageView backB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        username = findViewById(R.id.username);
        email = findViewById(R.id.emailID);
        password = findViewById(R.id.passwordSingUp);
        confirmPass = findViewById(R.id.confirm_pass);
        signUpButton = findViewById(R.id.singUpB);
        backB = findViewById(R.id.backB);

        // Bấm vào nút quay lại
        backB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Bấm đắng ký tài khoản
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}