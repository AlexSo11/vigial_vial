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

/** Historial de los reportes del usuario actual. */
public class HistorialPersonal extends AppCompatActivity
        implements EstadoSimulador.OnEstadoCambiadoListener {

    RecyclerView rv;
    TextView tvVacio;
    ReporteAdapter adapter;
    List<ReporteModel> lista;
    String correo;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_personal);

        correo = getIntent().getStringExtra("correo");
        DB     = new DBHelper(this);

        rv      = findViewById(R.id.rvReportes);
        tvVacio = findViewById(R.id.tvVacio);
        rv.setLayoutManager(new LinearLayoutManager(this));

        lista   = new ArrayList<>();
        adapter = new ReporteAdapter(this, lista, true);
        rv.setAdapter(adapter);

        cargarReportes();
    }

    @Override
    protected void onResume() {
        super.onResume();
        EstadoSimulador.getInstance().setListener(this);
        EstadoSimulador.getInstance().iniciar(this);
        cargarReportes();
    }

    @Override
    protected void onPause() {
        super.onPause();
        EstadoSimulador.getInstance().removeListener();
        EstadoSimulador.getInstance().detener();
    }

    @Override
    public void onCambio() {
        cargarReportes();
    }

    private void cargarReportes() {
        lista.clear();

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
            tvVacio.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }
    }
}
