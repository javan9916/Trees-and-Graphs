package com.example.estudiantes.graphsandtrees.classes;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Router {
    public String nombre;
    public LatLng ubicacion;
    public String descripcion;
    public Register raiz;
    public Circle area;
    public Marker marker;
    public Register sigRe;
    public Router sigRo;
    public Cable sigC, antC;
    public boolean marca;
    public boolean conexo;

    public Router(String nombre, LatLng ubicacion, String descripcion) {
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.descripcion = descripcion;
        this.marca = false;
    }

    public void setArea(Circle area) {
        this.area = area;
    }

    public Circle getArea() {
        return this.area;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public Marker getMarker() {
        return this.marker;
    }

    public void setRaiz(Register raiz) {
        this.raiz = raiz;
    }

    public Register getRaiz() {
        return raiz;
    }

}
