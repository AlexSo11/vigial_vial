package com.example.vigiavial;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class Reporte extends AppCompatActivity {

    Spinner spinnerTransporte;
    EditText etNumeroUnidad, etDescripcion;
    Button btnSeleccionarFoto, btnReportar;
    ImageView ivPreview;
    DBHelper DB;
    String correo, fotoPath = "";

    ActivityResultLauncher<String> seleccionarImagen = registerForActivityResult(
            new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    fotoPath = uri.toString();
                    ivPreview.setImageURI(uri);
                    ivPreview.setVisibility(android.view.View.VISIBLE);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte);

        correo = getIntent().getStringExtra("correo");
        DB = new DBHelper(this);

        spinnerTransporte = findViewById(R.id.spinnerTransporte);
        etNumeroUnidad = findViewById(R.id.etNumeroUnidad);
        etDescripcion = findViewById(R.id.etDescripcion);
        btnSeleccionarFoto = findViewById(R.id.btnSeleccionarFoto);
        btnReportar = findViewById(R.id.btnReportar);
        ivPreview = findViewById(R.id.ivPreview);

        // Tipos de transporte ZMG
        String[] transportes = {
                "Selecciona un tipo...",
                "Macrobús", "Mi Macro Periférico", "Tren Ligero",
                "Trolebús", "Camión Urbano", "Taxi", "Moto Taxi", "Otro"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, transportes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTransporte.setAdapter(adapter);

        btnSeleccionarFoto.setOnClickListener(v ->
                seleccionarImagen.launch("image/*"));

        btnReportar.setOnClickListener(v -> {
            String transporte = spinnerTransporte.getSelectedItem().toString();
            String unidad = etNumeroUnidad.getText().toString();
            String descripcion = etDescripcion.getText().toString();

            if (transporte.equals("Selecciona un tipo...") ||
                    unidad.isEmpty() || descripcion.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            Boolean ok = DB.insertarReporte(correo, transporte, unidad, descripcion, fotoPath);
            if (ok) {
                Toast.makeText(this, "Reporte enviado correctamente", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error al enviar el reporte", Toast.LENGTH_SHORT).show();
            }
        });
    }
}