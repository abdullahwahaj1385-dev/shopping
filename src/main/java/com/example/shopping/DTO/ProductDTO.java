package com.example.shopping.DTO;

public class ProductDTO {

    private String nomeProduct;
    private String codice;
    private String prezzo;
    private String quantita;

    public String getNomeProduct() { return nomeProduct; }
    public void setNomeProduct(String nomeProduct) { this.nomeProduct = nomeProduct; }

    public String getCodice() { return codice; }
    public void setCodice(String codice) { this.codice = codice; }

    public String getPrezzo() { return prezzo; }
    public void setPrezzo(String prezzo) { this.prezzo = prezzo; }

    public String getQuantita() { return quantita; }
    public void setQuantita(String quantita) { this.quantita = quantita; }
}
