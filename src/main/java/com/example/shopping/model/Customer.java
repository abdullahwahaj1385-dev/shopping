package com.example.shopping.model;

import jakarta.persistence.*;

@Entity
@Table(name="customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Rimosso final per consentire la modifica dei campi
    private String fullName;
    private String email;
    private String phone;

    public Customer() {

    }

    // aggiungi il get
    public Long getId() {
        return id;
    }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }



}