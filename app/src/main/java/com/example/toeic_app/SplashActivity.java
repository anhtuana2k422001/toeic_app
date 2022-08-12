package com.example.toeic_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private TextView appNameLoad;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        appNameLoad = findViewById(R.id.appNameLoad);

        // Set font chữ
        Typeface typeface = ResourcesCompat.getFont(this,R.font.blacklist);
        appNameLoad.setTypeface(typeface);

        //Tạo animation cho loader
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.myanim);
        appNameLoad.setAnimation(animation);

        mAuth = FirebaseAuth.getInstance(); // Khởi tạo

        // Khởi tạo để truy vấn vào cơ sở dữ liệu firebase
        DbQuery.g_firestore = FirebaseFirestore.getInstance();


        new Thread(){
            @Override
            public void run(){
                try {
                    sleep(3000); // Đợi 3 giây để load
                }catch (InterruptedException e){
                    e.printStackTrace();
                }

                // Nêu hệ thống đã đăng nhập trước đó thì vào main chính không thì bắt đăng nhập lại
                if(mAuth.getCurrentUser() != null){


                    // load dữ liệu data
                    DbQuery.loadData(new MyCompleteListener() {
                        @Override
                        public void onSuccess() {
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(intent);
                            SplashActivity.this.finish();
                        }

                        @Override
                        public void onFailure() {
                            Toast.makeText(SplashActivity.this, "Đã xảy ra sự cố. Vui lòng thử lại !", Toast.LENGTH_SHORT).show();

                        }
                    });
                }else{
                    // Di chuyển vào main
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    SplashActivity.this.finish();
                }

            }
        }.start();
    }
}