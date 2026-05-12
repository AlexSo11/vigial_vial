package com.example.vigiavial;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ReporteAdapter extends RecyclerView.Adapter<ReporteAdapter.ViewHolder> {

    Context context;
    List<ReporteModel> lista;
    boolean esPersonal;

    public ReporteAdapter(Context context, List<ReporteModel> lista, boolean esPersonal) {
        this.context = context;
        this.lista = lista;
        this.esPersonal = esPersonal;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = esPersonal ? R.layout.item_reporte_personal : R.layout.item_reporte_global;
        View v = LayoutInflater.from(context).inflate(layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        ReporteModel r = lista.get(pos);

        h.tvTransporte.setText(r.tipoTransporte);
        h.tvUnidad.setText("Unidad: " + r.numeroUnidad);
        h.tvDescripcion.setText(r.descripcion);
        h.tvFecha.setText(r.fecha);

        if (esPersonal) {
            // Colores activo / inactivo
            int colorActivo  = 0xFF4F8EF7;
            int colorVerde   = 0xFF4CAF50;
            int colorInactivo = 0xFF1E2E45;
            int textoActivo  = 0xFF4F8EF7;
            int textoVerde   = 0xFF4CAF50;
            int textoInactivo = 0xFF445566;

            // Paso 1 — siempre activo
            h.barPaso1.setBackgroundColor(colorActivo);
            h.tvPaso1.setTextColor(textoActivo);

            // Paso 2
            boolean paso2 = r.estado.equals("En Proceso") || r.estado.equals("Solucionado");
            h.barPaso2.setBackgroundColor(paso2 ? colorActivo : colorInactivo);
            h.tvPaso2.setTextColor(paso2 ? textoActivo : textoInactivo);

            // Paso 3
            boolean paso3 = r.estado.equals("Solucionado");
            h.barPaso3.setBackgroundColor(paso3 ? colorVerde : colorInactivo);
            h.tvPaso3.setTextColor(paso3 ? textoVerde : textoInactivo);

        } else {
            // Badge de estado para historial global
            h.tvEstado.setText(r.estado);
            int color;
            switch (r.estado) {
                case "Solucionado": color = 0xFF4CAF50; break;
                case "En Proceso":  color = 0xFFFF9800; break;
                default:            color = 0xFF4F8EF7; break;
            }
            h.tvEstado.getBackground().setTint(color);
        }
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTransporte, tvUnidad, tvDescripcion, tvFecha;
        // Personal
        View barPaso1, barPaso2, barPaso3;
        TextView tvPaso1, tvPaso2, tvPaso3;
        // Global
        TextView tvEstado;

        ViewHolder(View v) {
            super(v);
            tvTransporte = v.findViewById(R.id.tvTransporte);
            tvUnidad     = v.findViewById(R.id.tvUnidad);
            tvDescripcion = v.findViewById(R.id.tvDescripcion);
            tvFecha      = v.findViewById(R.id.tvFecha);

            barPaso1 = v.findViewById(R.id.barPaso1);
            barPaso2 = v.findViewById(R.id.barPaso2);
            barPaso3 = v.findViewById(R.id.barPaso3);
            tvPaso1  = v.findViewById(R.id.tvPaso1);
            tvPaso2  = v.findViewById(R.id.tvPaso2);
            tvPaso3  = v.findViewById(R.id.tvPaso3);

            tvEstado = v.findViewById(R.id.tvEstado);
        }
    }
}
