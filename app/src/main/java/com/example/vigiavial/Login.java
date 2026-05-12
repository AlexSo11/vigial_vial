package com.example.vigiavial;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    EditText correo, password;
    Button btnLogin, btnCrearCuenta;

    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        correo   = findViewById(R.id.correo);
        password = findViewById(R.id.password);
        btnLogin       = findViewById(R.id.btnLogin);
        btnCrearCuenta = findViewById(R.id.btnCrearCuenta);

        DB = new DBHelper(this);

        // BOTÓN LOGIN
        btnLogin.setOnClickListener(v -> {

            String cor  = correo.getText().toString().trim();
            String pass = password.getText().toString();

            if (cor.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (DB.verificarLogin(cor, pass)) {
                Toast.makeText(this, "¡Bienvenido!", Toast.LENGTH_SHORT).show();

                // ✅ Se pasa el correo a MainActivity para identificar al usuario
                Intent intent = new Intent(Login.this, MainActivity.class);
                intent.putExtra("correo", cor);
                startActivity(intent);
                finish(); // evita regresar al login con el botón atrás
            } else {
                Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show();
            }

        });

        // BOTÓN REGISTRO
        btnCrearCuenta.setOnClickListener(v -> {
            startActivity(new Intent(Login.this, Registro.class));
        });
    }
}
