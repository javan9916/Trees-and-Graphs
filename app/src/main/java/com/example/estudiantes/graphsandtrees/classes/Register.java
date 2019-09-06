package com.example.estudiantes.graphsandtrees.classes;

public class Register {
    public int id;
    public String fecha;
    public String tipoRevision;
    public String estado;
    public Register izq, der;

    public Register(int id, String fecha, String tipoRevision, String estado) {
        this.id = id;
        this.fecha = fecha;
        this.tipoRevision = tipoRevision;
        this.estado = estado;
    }
}
