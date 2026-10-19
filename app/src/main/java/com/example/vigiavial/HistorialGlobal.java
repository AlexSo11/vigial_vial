package com.example.vigiavial;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/** Historial de todos los reportes con filtros por fecha. */
public class HistorialGlobal extends AppCompatActivity
        implements EstadoSimulador.OnEstadoCambiadoListener {

    RecyclerView rv;
    TextView tvVacio;
    Button btnHoy, btnSemana, btnMes;
    ReporteAdapter adapter;
    List<ReporteModel> todosLosReportes = new ArrayList<>();
    List<ReporteModel> lista = new ArrayList<>();
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_global);

        DB         = new DBHelper(this);
        rv         = findViewById(R.id.rvReportes);
        tvVacio    = findViewById(R.id.tvVacio);
        btnHoy     = findViewById(R.id.btnFiltroHoy);
        btnSemana  = findViewById(R.id.btnFiltroSemana);
        btnMes     = findViewById(R.id.btnFiltroMes);

        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReporteAdapter(this, lista, false);
        rv.setAdapter(adapter);

        cargarReportes();
        aplicarFiltro("hoy");
        marcarBoton(btnHoy);

        btnHoy.setOnClickListener(v -> {
            aplicarFiltro("hoy");
            marcarBoton(btnHoy);
        });

        btnSemana.setOnClickListener(v -> {
            aplicarFiltro("semana");
            marcarBoton(btnSemana);
        });

        btnMes.setOnClickListener(v -> {
            aplicarFiltro("mes");
            marcarBoton(btnMes);
        });
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
        todosLosReportes.clear();

        Cursor cursor = DB.obtenerTodosReportes();
        while (cursor.moveToNext()) {
            todosLosReportes.add(new ReporteModel(
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

        if (btnSemana != null && btnSemana.getBackgroundTintList() != null &&
                btnSemana.getBackgroundTintList().getDefaultColor() == 0xFF4F8EF7) {
            aplicarFiltro("semana");
        } else if (btnMes != null && btnMes.getBackgroundTintList() != null &&
                btnMes.getBackgroundTintList().getDefaultColor() == 0xFF4F8EF7) {
            aplicarFiltro("mes");
        } else {
            aplicarFiltro("hoy");
        }
    }

    private void aplicarFiltro(String filtro) {
        lista.clear();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        Calendar cal = Calendar.getInstance();

        if (filtro.equals("hoy")) {
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
        } else if (filtro.equals("semana")) {
            cal.add(Calendar.DAY_OF_YEAR, -7);
        } else if (filtro.equals("mes")) {
            cal.add(Calendar.MONTH, -1);
        }

        Date fechaLimite = cal.getTime();

        for (ReporteModel r : todosLosReportes) {
            try {
                Date fechaReporte = sdf.parse(r.fecha);
                if (fechaReporte != null && fechaReporte.after(fechaLimite)) {
                    lista.add(r);
                }
            } catch (Exception e) {
                // Si la fecha no se puede parsear, ignorar ese reporte
            }
        }

        if (lista.isEmpty()) {
            tvVacio.setVisibility(View.VISIBLE);
            rv.setVisibility(View.GONE);
        } else {
            tvVacio.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
        }

        adapter.notifyDataSetChanged();
    }

    private void marcarBoton(Button activo) {
        btnHoy.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFF1E2E45));
        btnSemana.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFF1E2E45));
        btnMes.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFF1E2E45));
        activo.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFF4F8EF7));
    }
}