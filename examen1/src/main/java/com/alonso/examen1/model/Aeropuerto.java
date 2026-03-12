package com.alonso.examen1.model;

/**
 * Modelo que representa la tabla 'aeropuertos' de la base de datos.
 *
 * SQL original:
 * CREATE TABLE aeropuertos (
 *     id      INT          PRIMARY KEY,
 *     nombre  VARCHAR(200),
 *     ciudad  VARCHAR(100),
 *     pais    VARCHAR(100),
 *     codigo  VARCHAR(10)
 * );
 */
public class Aeropuerto {

    private int    id;
    private String nombre;
    private String ciudad;
    private String pais;
    private String codigo;

    /* ──────────── Constructors ──────────── */

    public Aeropuerto() {}

    public Aeropuerto(int id, String nombre, String ciudad, String pais, String codigo) {
        this.id     = id;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.pais   = pais;
        this.codigo = codigo;
    }

    /* ──────────── Getters & Setters ──────────── */

    public int getId()                  { return id; }
    public void setId(int id)           { this.id = id; }

    public String getNombre()           { return nombre; }
    public void setNombre(String n)     { this.nombre = n; }

    public String getCiudad()           { return ciudad; }
    public void setCiudad(String c)     { this.ciudad = c; }

    public String getPais()             { return pais; }
    public void setPais(String p)       { this.pais = p; }

    public String getCodigo()           { return codigo; }
    public void setCodigo(String c)     { this.codigo = c; }

    @Override
    public String toString() {
        return "Aeropuerto{id=" + id + ", nombre='" + nombre + "', ciudad='" + ciudad +
               "', pais='" + pais + "', codigo='" + codigo + "'}";
    }
}
