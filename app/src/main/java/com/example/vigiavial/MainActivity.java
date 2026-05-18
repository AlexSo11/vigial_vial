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

        tvBienvenida         = findViewById(R.id.tvBienvenida);
        btnReporte           = findViewById(R.id.btnReporte);
        btnHistorialPersonal = findViewById(R.id.btnHistorialPersonal);
        btnHistorialGlobal   = findViewById(R.id.btnHistorialGlobal);

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Inicia el simulador cuando la app está en primer plano
        EstadoSimulador.getInstance().iniciar(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Detiene el simulador cuando la app va a segundo plano
        EstadoSimulador.getInstance().detener();
    }
}
