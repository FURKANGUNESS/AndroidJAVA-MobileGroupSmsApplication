package com.example.mobilegroupsms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    //gerekli viewlera degiskenleri atiyoruz

    EditText emailEditText, passwordEditText;
    Button registerButton;
    Button registerLoginButton;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailEditText = findViewById(R.id.register_emailEditText);
        passwordEditText = findViewById(R.id.register_passwordEditText);

        registerButton = findViewById(R.id.register_registerButton);
        registerLoginButton = findViewById(R.id.register_registerLoginButton);

        mAuth = FirebaseAuth.getInstance();

        //giris butonuna basildiginda

        registerLoginButton.setOnClickListener(v -> { //Login ekranina basildiginda loginactivity acilsin.
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish(); //Sonrasinda ise kapansin.
        });
        //Tum islemleri register butona basildiginda yapmak istiycem.

        registerButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString(); //emaili edittext uzerinden tostringlerle aliyorum
            String password = passwordEditText.getText().toString();

            //Burada bir kontrol yapip eger e-mail ve password alanlari bos ise doldurulmasi gerektigini belirtiyorum.

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Lütfen tüm alanlarını doldurun !", Toast.LENGTH_SHORT).show();
                return;
            }
            //Burada firebase sifrenin minimum 6 karakter olmasini istedigi icin sifre karakter kontrolu yapiyorum.

            if (password.length() < 6) {
                Toast.makeText(RegisterActivity.this, "Oluşturduğunuz şifre minimum 6 karakter olmalıdır !", Toast.LENGTH_SHORT).show();
                return;
            }

            //Kullanici kontrolleri gecmis ise createUserWithEmailAndPassword ile mail ve sifre bilgileri araciligiyla bir kayit olusturuyorum.
            //Bu islem asenkron bir islem oldugundan addOnCompleteListener ile bir dinleyici ekliyorum.

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {    //Kayit isleminin basarili olup olmadigini kontrol ediyorum.
                    Toast.makeText(RegisterActivity.this, "Kayıt işlemi başarılıdır !", Toast.LENGTH_SHORT).show();   //Kayit basarili oldugunda kullaniciya kayidin basarili oldugunu gosteriyorum.
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));//Kayit yonlendirme basarili oldugunda artik login activity acilacak.
                    finish(); //Ekran kapanacak.
                } else {
                    Toast.makeText(RegisterActivity.this, "Kayıt işlemi başarısızdır !", Toast.LENGTH_SHORT).show();   //Kayit basarisiz oldugunda kullaniciya giris ekranina yonlendiriyorum.
                    finish();
                }
            });
        });
    }
}