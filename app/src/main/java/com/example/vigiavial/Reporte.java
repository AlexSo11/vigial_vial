package com.example.vigiavial;

import android.os.Bundle;
import android.widget.*;
import android.view.View;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;

public class Reporte extends AppCompatActivity {

    // Selector de transporte
    LinearLayout btnTransCamion, btnTransTren, btnTransBici, btnTransTaxi;
    TextView lblCamion, lblTren, lblBici, lblTaxi;
    LinearLayout transporteSeleccionado = null;
    String tipoTransporte = "";

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
                ivPreview.setVisibility(View.VISIBLE);
            }
        });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte);

        correo = getIntent().getStringExtra("correo");
        DB = new DBHelper(this);

        // Selector de transporte
        btnTransCamion = findViewById(R.id.btnTransCamion);
        btnTransTren   = findViewById(R.id.btnTransTren);
        btnTransBici   = findViewById(R.id.btnTransBici);
        btnTransTaxi   = findViewById(R.id.btnTransTaxi);
        lblCamion      = findViewById(R.id.lblCamion);
        lblTren        = findViewById(R.id.lblTren);
        lblBici        = findViewById(R.id.lblBici);
        lblTaxi        = findViewById(R.id.lblTaxi);

        // Campos y botones
        etNumeroUnidad    = findViewById(R.id.etNumeroUnidad);
        etDescripcion     = findViewById(R.id.etDescripcion);
        btnSeleccionarFoto = findViewById(R.id.btnSeleccionarFoto);
        btnReportar       = findViewById(R.id.btnReportar);
        ivPreview         = findViewById(R.id.ivPreview);

        // Asignar listeners de selección
        btnTransCamion.setOnClickListener(v -> seleccionarTransporte(btnTransCamion, lblCamion, "Camión"));
        btnTransTren.setOnClickListener(v   -> seleccionarTransporte(btnTransTren,   lblTren,   "Tren"));
        btnTransBici.setOnClickListener(v   -> seleccionarTransporte(btnTransBici,   lblBici,   "Bicicleta"));
        btnTransTaxi.setOnClickListener(v   -> seleccionarTransporte(btnTransTaxi,   lblTaxi,   "Taxi"));

        btnSeleccionarFoto.setOnClickListener(v -> seleccionarImagen.launch("image/*"));

        btnReportar.setOnClickListener(v -> {
            String unidad      = etNumeroUnidad.getText().toString().trim();
            String descripcion = etDescripcion.getText().toString().trim();

            if (tipoTransporte.isEmpty()) {
                Toast.makeText(this, "Selecciona un tipo de transporte", Toast.LENGTH_SHORT).show();
                return;
            }
            if (unidad.isEmpty() || descripcion.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            Boolean ok = DB.insertarReporte(correo, tipoTransporte, unidad, descripcion, fotoPath);
            if (ok) {
                Toast.makeText(this, "¡Reporte enviado!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error al enviar el reporte", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void seleccionarTransporte(LinearLayout btn, TextView lbl, String tipo) {
        // Deseleccionar el anterior
        if (transporteSeleccionado != null) {
            transporteSeleccionado.setBackground(
                getResources().getDrawable(R.drawable.transport_btn_unselected, getTheme()));
            // Resetear color del label anterior
            resetearLabel(transporteSeleccionado);
        }

        // Seleccionar el nuevo
        btn.setBackground(
            getResources().getDrawable(R.drawable.transport_btn_selected, getTheme()));
        lbl.setTextColor(Color.parseColor("#4F8EF7"));

        transporteSeleccionado = btn;
        tipoTransporte = tipo;
    }

    // Regresa el label a su color gris original
    private void resetearLabel(LinearLayout btn) {
        TextView[] labels = { lblCamion, lblTren, lblBici, lblTaxi };
        LinearLayout[] btns = { btnTransCamion, btnTransTren, btnTransBici, btnTransTaxi };
        for (int i = 0; i < btns.length; i++) {
            if (btns[i] == btn) {
                labels[i].setTextColor(Color.parseColor("#8899AA"));
                break;
            }
        }
    }
}
