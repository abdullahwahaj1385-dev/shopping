package com.example.shopping.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name="orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String idOrdine;
    private String cliente;
    private String totale;
    private String stato;

    public Long getId() {
        return id;
    }

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
