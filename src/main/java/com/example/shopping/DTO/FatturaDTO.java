package com.example.shopping.DTO;

public class FatturaDTO {

    private String idFattura;
    private String numero;
    private String cliente;
    private String totale;
    private String data;

    public String getIdFattura() {
        return idFattura;
    }

    public void setIdFattura(String idFattura) {
        this.idFattura = idFattura;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getTotale() {
        return totale;
    }

    public void setTotale(String totale) {
        this.totale = totale;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
