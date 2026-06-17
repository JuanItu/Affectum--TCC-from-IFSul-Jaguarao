package com.example.affectum.bean;

public class Sentimentos {

    private Integer id;
    private String tipo;

    public Sentimentos() {
    }

    public Sentimentos(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) { this.id = id; }

    public String getTipo() { return this.tipo; }

    public void setTipo(String tipo) { this.tipo = tipo; }
}
