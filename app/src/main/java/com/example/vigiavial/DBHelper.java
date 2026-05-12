package com.example.vigiavial;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "VigiaVial.db";

    public DBHelper(Context context) {
        super(context, "VigiaVial.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create Table usuarios(correo TEXT primary key, contrasena TEXT, nombre TEXT)");
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

        db.execSQL("drop Table if exists usuarios");

    }

    // Insertar usuario
    public Boolean insertarUsuario(String correo, String password, String nombre){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("correo", correo);
        values.put("contrasena", password);
        values.put("nombre", nombre);

        long result = db.insert("usuarios", null, values);

        if(result == -1){
            return false;
        }else{
            return true;
        }

    }

    // Verificar correo
    public Boolean verificarCorreo(String correo){

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("Select * from usuarios where correo = ?", new String[]{correo});

        if(cursor.getCount() > 0){
            return true;
        }else{
            return false;
        }

    }

    // Verificar login
    public Boolean verificarLogin(String correo, String password){

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("Select * from usuarios where correo = ? and contrasena = ?", new String[]{correo, password});

        if(cursor.getCount() > 0){
            return true;
        }else{
            return false;
        }

    }

    // Insertar reporte
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
        // Fecha actual
        String fecha = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm",
                java.util.Locale.getDefault()).format(new java.util.Date());
        values.put("fecha", fecha);
        long result = db.insert("reportes", null, values);
        return result != -1;
    }

    // Obtener reportes del usuario
    public Cursor obtenerReportesUsuario(String correo) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM reportes WHERE correo_usuario = ? ORDER BY id DESC",
                new String[]{correo});
    }

    // Obtener todos los reportes
    public Cursor obtenerTodosReportes() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM reportes ORDER BY id DESC", null);
    }

}
