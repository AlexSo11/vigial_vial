package com.example.vigiavial;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "VigiaVial.db";

    // ─────────────────────────────────────────────────
    // VARIABLE GLOBAL: intervalo de avance de estados
    // Cambia este valor para ajustar la velocidad
    // ─────────────────────────────────────────────────
    public static final long INTERVALO_AVANCE_MS = 10_000; // 10 segundos

    public DBHelper(Context context) {
        super(context, DBNAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE usuarios(" +
                "correo TEXT PRIMARY KEY, " +
                "contrasena TEXT, " +
                "nombre TEXT)");

        db.execSQL("CREATE TABLE reportes(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "correo_usuario TEXT, " +
                "tipo_transporte TEXT, " +
                "numero_unidad TEXT, " +
                "descripcion TEXT, " +
                "foto_path TEXT, " +
                "estado TEXT DEFAULT 'Recibido', " +
                "fecha TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        db.execSQL("DROP TABLE IF EXISTS reportes");
        onCreate(db);
    }

    // ── Usuarios ──────────────────────────────────────

    public Boolean insertarUsuario(String correo, String password, String nombre) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("correo", correo);
        values.put("contrasena", password);
        values.put("nombre", nombre);
        long result = db.insert("usuarios", null, values);
        return result != -1;
    }

    public Boolean verificarCorreo(String correo) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM usuarios WHERE correo = ?", new String[]{correo});
        boolean existe = cursor.getCount() > 0;
        cursor.close();
        return existe;
    }

    public Boolean verificarLogin(String correo, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM usuarios WHERE correo = ? AND contrasena = ?",
                new String[]{correo, password});
        boolean ok = cursor.getCount() > 0;
        cursor.close();
        return ok;
    }

    // ── Reportes ──────────────────────────────────────

    public Boolean insertarReporte(String correo, String tipoTransporte,
                                   String numeroUnidad, String descripcion, String fotoPath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("correo_usuario", correo);
        values.put("tipo_transporte", tipoTransporte);
        values.put("numero_unidad", numeroUnidad);
        values.put("descripcion", descripcion);
        values.put("foto_path", fotoPath);
        values.put("estado", "Recibido");
        String fecha = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm",
                java.util.Locale.getDefault()).format(new java.util.Date());
        values.put("fecha", fecha);
        long result = db.insert("reportes", null, values);
        return result != -1;
    }

    public Cursor obtenerReportesUsuario(String correo) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT * FROM reportes WHERE correo_usuario = ? ORDER BY id DESC",
                new String[]{correo});
    }

    public Cursor obtenerTodosReportes() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT * FROM reportes ORDER BY id DESC", null);
    }

    // ── Avance de estados (llamado por el simulador) ──

    /**
     * Avanza UN paso a todos los reportes que no estén en "Solucionado":
     *   Recibido  →  En Proceso
     *   En Proceso  →  Solucionado
     * Retorna cuántos registros fueron actualizados.
     */
    public int avanzarEstados() {
        SQLiteDatabase db = this.getWritableDatabase();
        int total = 0;

        // Primero: En Proceso → Solucionado
        ContentValues v2 = new ContentValues();
        v2.put("estado", "Solucionado");
        total += db.update("reportes", v2,
                "estado = ?", new String[]{"En Proceso"});

        // Luego: Recibido → En Proceso
        ContentValues v1 = new ContentValues();
        v1.put("estado", "En Proceso");
        total += db.update("reportes", v1,
                "estado = ?", new String[]{"Recibido"});

        return total;
    }
}
