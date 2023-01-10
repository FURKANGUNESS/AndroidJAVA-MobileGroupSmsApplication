package com.example.mobilegroupsms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    //Gerekli viewlari atiyorum
    EditText emailEditText, passwordEditText;
    Button loginButton;
    Button loginRegisterButton;

    FirebaseAuth mAuth; //Firebase Authentication

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Gerekli viewlari degiskenlere atiyorum.

        emailEditText = findViewById(R.id.login_emailEditText);
        passwordEditText = findViewById(R.id.login_passwordEditText);

        loginButton = findViewById(R.id.login_loginButton);
        loginRegisterButton = findViewById(R.id.login_loginRegisterButton);

        mAuth = FirebaseAuth.getInstance(); //Burada uretilmiyor.Firebase bize otomatik olarak veriyor.

        loginRegisterButton.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));//RegisterActivity ye gonder ve kapat
            finish(); //Sonrasinda ise kapansin.
        });

        loginButton.setOnClickListener(view -> {
            String email = emailEditText.getText().toString(); //Gerekli olan emaili edittext uzerinden tostringlerle aliyorum
            String password = passwordEditText.getText().toString();
            ; //Gerekli olan sifreyi edittext uzerinden tostringlerle aliyorum

            //Burada bir kontrol yapip eger e-mail ve password alanlari bos ise doldurulmasi gerektigini belirtiyorum.

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Lütfen tüm alanlarını doldurun !", Toast.LENGTH_SHORT).show();
                return;
            }

            //Burada firebase sifrenin minimum 6 karakter olmasini istedigi icin sifre karakter kontrolu yapiyorum.

            if (password.length() < 6) {
                Toast.makeText(LoginActivity.this, "Oluşturduğunuz şifre minimum 6 karakter olmalıdır !", Toast.LENGTH_SHORT).show();
                return;
            }

            //Kullanici kontrolleri gecmis ise signInWithEmailAndPassword ile mail ve sifre bilgileri araciligiyla bir gırıs olusturuyorum.
            //Bu islem asenkron bir islem oldugundan addOnCompleteListener ile bir dinleyici ekliyorum.

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {    //Giriş isleminin basarili olup olmadigini kontrol ediyorum.

                    Toast.makeText(LoginActivity.this, "Giriş başarılıdır !", Toast.LENGTH_SHORT).show();   //Giriş basarili oldugunda kullaniciya girişin basarili oldugunu gosteriyorum.
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));   //Giriş yonlendirme basarili oldugunda artik main activity acilacak.
                    finish(); //Ekran kapanacak.
                } else {
                    Toast.makeText(LoginActivity.this, "Giriş başarısızdır !", Toast.LENGTH_SHORT).show();   //Giriş basarisiz oldugunda kullaniciya ana ekranina yonlendiriyorum.
                    finish();
                }
            });
        });
    }
}