package com.daniminguet.trabajofrasescelebres.models;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Frase implements Serializable{
    private int id;
    private String texto;
    private String fechaprogramada;
    private int autorId;
    private int categoriaId;

    public Frase() {
    }

    public Frase(String texto, String fechaProgramada, int autorId, int categoriaId) {
        this.texto = texto;
        this.fechaprogramada = fechaProgramada;
        this.autorId = autorId;
        this.categoriaId = categoriaId;
    }

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getFechaprogramada() {
        return this.fechaprogramada;
    }

    public void setFechaprogramada(String fechaprogramada) {
        this.fechaprogramada = fechaprogramada;
    }

    public int getAutorId() {
        return this.autorId;
    }

    public void setAutorId(int autorId) {
        this.autorId = autorId;
    }

    public int getCategoriaId() {
        return this.categoriaId;
    }

    public void setAutor(int autorId) {
        this.autorId = autorId;
    }

    public void setCategoriaId(int categoriaId) {
        this.categoriaId = categoriaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Frase frase = (Frase) o;
        return id == frase.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @NonNull
    @Override
    public String toString() {
        return "Frase{" +
                "id=" + id +
                ", texto='" + texto + '\'' +
                ", fechaProgramada='" + fechaprogramada + '\'' +
                ", autor=" + autorId +
                ", categoria=" + categoriaId +
                '}';
    }
}
