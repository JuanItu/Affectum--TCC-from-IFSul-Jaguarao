package com.example.affectum.bean;

public class Informacoes {
    private Integer id;
    private Integer idRegistro;
    private Integer idSentimento;
    private String data;
    private String hora;
    private String acontecimento;
    private String infoExtra;

    public Informacoes() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(Integer idRegistro) {
        this.idRegistro = idRegistro;
    }

    public Integer getIdSentimento() {
        return idSentimento;
    }

    public void setIdSentimento(Integer idSemtimento) {
        this.idSentimento = idSemtimento;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getAcontecimento() {
        return acontecimento;
    }

    public void setAcontecimento(String acontecimento) {
        this.acontecimento = acontecimento;
    }

    public String getInfoExtra() {
        return infoExtra;
    }

    public void setInfoExtra(String infoExtra) {
        this.infoExtra = infoExtra;
    }
}
