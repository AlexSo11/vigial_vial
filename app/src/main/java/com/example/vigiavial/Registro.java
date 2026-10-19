package com.example.vigiavial;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/** Pantalla de registro de usuarios. */
public class Registro extends AppCompatActivity {

    EditText nombre, apellido, correo, password;
    Button btnCrearCuenta;

    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        nombre       = findViewById(R.id.nombre);
        apellido     = findViewById(R.id.apellido);
        correo       = findViewById(R.id.correo);
        password     = findViewById(R.id.password);
        btnCrearCuenta = findViewById(R.id.btnCrearCuenta);

        DB = new DBHelper(this);

        btnCrearCuenta.setOnClickListener(v -> {

            String nom  = nombre.getText().toString().trim();
            String ape  = apellido.getText().toString().trim();
            String cor  = correo.getText().toString().trim();
            String pass = password.getText().toString();

            // Validar campos vacíos
            if (nom.isEmpty() || ape.isEmpty() || cor.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validar formato de correo
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(cor).matches()) {
                Toast.makeText(this, "Ingresa un correo válido", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validar longitud de contraseña
            if (pass.length() < 6) {
                Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                return;
            }

            // Nombre completo combinado
            String nombreCompleto = nom + " " + ape;

            // Verificar si el correo ya existe
            if (DB.verificarCorreo(cor)) {
                Toast.makeText(this, "Este correo ya está registrado", Toast.LENGTH_SHORT).show();
                return;
            }

            // Insertar usuario
            Boolean insert = DB.insertarUsuario(cor, pass, nombreCompleto);

            if (insert) {
                Toast.makeText(this, "¡Cuenta creada exitosamente!", Toast.LENGTH_SHORT).show();

                // Limpiar campos
                nombre.setText("");
                apellido.setText("");
                correo.setText("");
                password.setText("");

                // Ir al Login
                Intent intent = new Intent(Registro.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Error al crear la cuenta", Toast.LENGTH_SHORT).show();
            }

        });
    }
}