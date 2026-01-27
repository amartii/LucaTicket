package com.lucatic.compra_service.dto;

public class CompraRequest {
    private String mail;
    private Integer idEvento;
    private DatosTarjeta datosTarjeta;

    public CompraRequest() {}

    public CompraRequest(String mail, Integer idEvento, DatosTarjeta datosTarjeta) {
        this.mail = mail;
        this.idEvento = idEvento;
        this.datosTarjeta = datosTarjeta;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Integer getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(Integer idEvento) {
        this.idEvento = idEvento;
    }

    public DatosTarjeta getDatosTarjeta() {
        return datosTarjeta;
    }

    public void setDatosTarjeta(DatosTarjeta datosTarjeta) {
        this.datosTarjeta = datosTarjeta;
    }
}
