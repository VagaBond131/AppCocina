package com.example.picana_apk.model;

public class Plato {
    private int id;
    private String nombre;
    private double precio;
    private String imagenUrl;
    private String categoria;
    private String peso;
    private String tiempo;

    public Plato(int id, String nombre, double precio, String imagenUrl, String categoria, String peso, String tiempo) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.imagenUrl = imagenUrl;
        this.categoria = categoria;
        this.peso = peso;
        this.tiempo = tiempo;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    public String getImagenUrl() { return imagenUrl; }
    public String getCategoria() { return categoria; }
    public String getPeso() { return peso; }
    public String getTiempo() { return tiempo; }
}