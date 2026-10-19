package com.example.vigiavial;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;


/** Pantalla de arranque; redirige al login tras 3 segundos. */
public class splash extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // Espera 3 segundos
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(splash.this, Login.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}