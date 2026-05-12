package com.example.vigiavial;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class HistorialPersonal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_personal);

        // ✅ Recibe el correo desde MainActivity
        String correo = getIntent().getStringExtra("correo");

        DBHelper DB = new DBHelper(this);
        RecyclerView rv = findViewById(R.id.rvReportes);
        TextView tvVacio = findViewById(R.id.tvVacio);

        rv.setLayoutManager(new LinearLayoutManager(this));

        List<ReporteModel> lista = new ArrayList<>();

        // ✅ Solo carga si el correo no es null
        if (correo != null && !correo.isEmpty()) {
            Cursor cursor = DB.obtenerReportesUsuario(correo);
            while (cursor.moveToNext()) {
                lista.add(new ReporteModel(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7)
                ));
            }
            cursor.close();
        }

        if (lista.isEmpty()) {
            tvVacio.setVisibility(View.VISIBLE);
            rv.setVisibility(View.GONE);
        } else {
            rv.setAdapter(new ReporteAdapter(this, lista, true));
        }
    }
}
