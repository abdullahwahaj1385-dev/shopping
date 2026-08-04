package com.example.shopping.DTO;

public class OrderDTO {

    private String idOrdine;
    private String cliente;
    private String totale;
    private String stato;

    public String getIdOrdine() {
        return idOrdine;
    }

    public void setIdOrdine(String idOrdine) {
        this.idOrdine = idOrdine;
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

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }
}
