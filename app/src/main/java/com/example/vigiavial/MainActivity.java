package com.example.vigiavial;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {

    CardView btnReporte, btnHistorialPersonal, btnHistorialGlobal;
    TextView tvBienvenida;
    Button btnCerrarSesion;
    String correo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvBienvenida         = findViewById(R.id.tvBienvenida);
        btnReporte           = findViewById(R.id.btnReporte);
        btnHistorialPersonal = findViewById(R.id.btnHistorialPersonal);
        btnHistorialGlobal   = findViewById(R.id.btnHistorialGlobal);
        btnCerrarSesion      = findViewById(R.id.btnCerrarSesion);

        correo = getIntent().getStringExtra("correo");

        if (correo != null && !correo.isEmpty()) {
            String nombre = correo.split("@")[0];
            tvBienvenida.setText("¡Hola, " + nombre + "!");
        }

        btnReporte.setOnClickListener(v -> {
            Intent i = new Intent(this, Reporte.class);
            i.putExtra("correo", correo);
            startActivity(i);
        });

        btnHistorialPersonal.setOnClickListener(v -> {
            Intent i = new Intent(this, HistorialPersonal.class);
            i.putExtra("correo", correo);
            startActivity(i);
        });

        btnHistorialGlobal.setOnClickListener(v ->
                startActivity(new Intent(this, HistorialGlobal.class)));

        btnCerrarSesion.setOnClickListener(v -> {
            new android.app.AlertDialog.Builder(this)
                    .setTitle("Cerrar sesión")
                    .setMessage("¿Estás seguro de que deseas cerrar sesión?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        getSharedPreferences("sesion", MODE_PRIVATE).edit().clear().apply();
                        Intent i = new Intent(MainActivity.this, Login.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        EstadoSimulador.getInstance().iniciar(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EstadoSimulador.getInstance().detener();
    }
}