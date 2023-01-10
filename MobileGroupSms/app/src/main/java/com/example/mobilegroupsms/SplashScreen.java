package com.example.mobilegroupsms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {

    //Kullanici daha onceden splashscreenden giris yapmis mi kontrolu yapacagim.

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        findViewById(R.id.splash_register).setOnClickListener(v -> {
            startActivity(new Intent(SplashScreen.this, RegisterActivity.class));
        });

        findViewById(R.id.splash_login).setOnClickListener(v -> {
            startActivity(new Intent(SplashScreen.this, LoginActivity.class));
        });

        mAuth = FirebaseAuth.getInstance();

        //Eger suan ki kullanici null degilse daha onceden giris yaptiysa donduruyor
        //getCurrentUser da bir user tutuyor
        //Eger daha onceden giris yaptiysa hatirlanan bir kayit var.

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(SplashScreen.this, MainActivity.class));
            //finish(); burayi silersem logout oldugunda splash hep acik kalacaktir.
            Toast.makeText(SplashScreen.this, "Yönlendiriliyor !", Toast.LENGTH_SHORT).show();
        }
    }
}