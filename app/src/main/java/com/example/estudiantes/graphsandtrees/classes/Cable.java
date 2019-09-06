package com.example.estudiantes.graphsandtrees.classes;

import com.google.android.gms.maps.model.Polyline;

public class Cable {
    public int distancia;
    public int velocidadTransferencia;
    public String tipo;
    public Polyline polyline;
    public Router origen;
    public Router destino;
    public Cable sigC, antC;
    public String tag1,tag2;

    public Cable(Router origen, Router destino) {
        this.destino = destino;
        this.origen = origen;
    }

    public void setPolyline(Polyline polyline) {
        this.polyline = polyline;
    }

    public Polyline getPolyline() {
        return this.polyline;
    }

    public void setDistancia(int distancia) {
        this.distancia = distancia;
    }

    public int getDistancia() {
        return distancia;
    }

    public void setVelocidadTransferencia(int velocidadTransferencia) {
        this.velocidadTransferencia = velocidadTransferencia;
    }

    public int getVelocidadTransferencia() {
        return velocidadTransferencia;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }
}
