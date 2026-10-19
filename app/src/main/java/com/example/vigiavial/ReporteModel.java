package com.example.vigiavial;

/** Modelo de un reporte. */
public class ReporteModel {
    public int id;
    public String correoUsuario, tipoTransporte, numeroUnidad;
    public String descripcion, fotoPath, estado, fecha;

    public ReporteModel(int id, String correoUsuario, String tipoTransporte,
                        String numeroUnidad, String descripcion,
                        String fotoPath, String estado, String fecha) {
        this.id = id;
        this.correoUsuario = correoUsuario;
        this.tipoTransporte = tipoTransporte;
        this.numeroUnidad = numeroUnidad;
        this.descripcion = descripcion;
        this.fotoPath = fotoPath;
        this.estado = estado;
        this.fecha = fecha;
    }
}