package com.example.affectum.bean;

import java.sql.Date;

public class RegistroEmocoes {


    private Integer id;
    private Sentimentos sentimento;
    private Date data;
    private  String observacao;

    public RegistroEmocoes() {
    }

    public Sentimentos getSentimento() {
        return sentimento;
    }

    public RegistroEmocoes(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setSentimento(Sentimentos sentimento) {
        this.sentimento = sentimento;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
