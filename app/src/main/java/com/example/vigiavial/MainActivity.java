package com.example.vigiavial;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {

    CardView btnReporte, btnHistorialPersonal, btnHistorialGlobal;
    TextView tvBienvenida;
    String correo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvBienvenida        = findViewById(R.id.tvBienvenida);
        btnReporte          = findViewById(R.id.btnReporte);
        btnHistorialPersonal = findViewById(R.id.btnHistorialPersonal);
        btnHistorialGlobal  = findViewById(R.id.btnHistorialGlobal);

        // ✅ Recibe el correo desde Login
        correo = getIntent().getStringExtra("correo");

        if (correo != null && !correo.isEmpty()) {
            // Mostrar solo la parte antes del @
            String nombre = correo.split("@")[0];
            tvBienvenida.setText("¡Hola, " + nombre + "!");
        }

        btnReporte.setOnClickListener(v -> {
            Intent i = new Intent(this, Reporte.class);
            i.putExtra("correo", correo); // ✅ pasa correo al reporte
            startActivity(i);
        });

        btnHistorialPersonal.setOnClickListener(v -> {
            Intent i = new Intent(this, HistorialPersonal.class);
            i.putExtra("correo", correo); // ✅ pasa correo al historial
            startActivity(i);
        });

        btnHistorialGlobal.setOnClickListener(v -> {
            startActivity(new Intent(this, HistorialGlobal.class));
        });
    }
}
