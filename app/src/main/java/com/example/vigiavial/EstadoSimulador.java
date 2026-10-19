package com.example.vigiavial;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

/**
 * Singleton que avanza los estados de los reportes en background.
 * Cada pantalla lo inicia en onResume y lo detiene en onPause.
 *
 * Intervalo configurado en: DBHelper.INTERVALO_AVANCE_MS
 */
public class EstadoSimulador {

    private static EstadoSimulador instancia;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable tarea;
    private boolean corriendo = false;

    // Listener para notificar a las pantallas abiertas que recarguen
    public interface OnEstadoCambiadoListener {
        void onCambio();
    }

    private OnEstadoCambiadoListener listener;

    // ── Singleton ──────────────────────────────────────────────────

    public static EstadoSimulador getInstance() {
        if (instancia == null) {
            instancia = new EstadoSimulador();
        }
        return instancia;
    }

    private EstadoSimulador() {}

    // ── Control ────────────────────────────────────────────────────

    public void iniciar(Context context) {
        if (corriendo) return;
        corriendo = true;

        tarea = new Runnable() {
            @Override
            public void run() {
                if (!corriendo) return;

                // Avanza estados en la BD
                DBHelper db = new DBHelper(context.getApplicationContext());
                int actualizados = db.avanzarEstados();

                // Notifica al listener si hay una pantalla escuchando
                if (actualizados > 0 && listener != null) {
                    listener.onCambio();
                }

                // Reprogramar
                handler.postDelayed(this, DBHelper.INTERVALO_AVANCE_MS);
            }
        };

        handler.postDelayed(tarea, DBHelper.INTERVALO_AVANCE_MS);
    }

    public void detener() {
        corriendo = false;
        if (tarea != null) {
            handler.removeCallbacks(tarea);
        }
    }

    public void setListener(OnEstadoCambiadoListener l) {
        this.listener = l;
    }

    public void removeListener() {
        this.listener = null;
    }

    public boolean estaCorriendo() {
        return corriendo;
    }
}
