package com.daniminguet.trabajofrasescelebres.models;

public class Usuario {
    private int id;
    private String nombre;
    private String contrasenya;
    private Byte admin;

    public Usuario() {
    }

    public Usuario(String nombre, String contrasenya) {
        this.nombre = nombre;
        this.contrasenya = contrasenya;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getContrasenya() {
        return contrasenya;
    }

    public Byte getAdmin() {
        return admin;
    }

    private void setId(int id) {
        this.id = id;
    }

    public void setAdmin(Byte admin) {
        this.admin = admin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Usuario usuario = (Usuario) o;
        return id == usuario.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", contrasenya='" + contrasenya + '\'' +
                ", admin=" + admin +
                '}';
    }
}
