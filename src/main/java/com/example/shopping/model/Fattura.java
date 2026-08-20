package com.example.shopping.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "fatture")
public class Fattura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numero;
    private String cliente;
    private Double importo;
    private String merceComprata; // La merce acquistata dal cliente
    private LocalDate data;

    public Fattura() {}

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }
    public Double getImporto() { return importo; }
    public void setImporto(Double importo) { this.importo = importo; }
    public String getMerceComprata() { return merceComprata; }
    public void setMerceComprata(String merceComprata) { this.merceComprata = merceComprata; }
    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }
}
