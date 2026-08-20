package com.example.shopping.service;

import com.example.shopping.model.Fattura;
import com.example.shopping.repository.FatturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FatturaService {

    @Autowired
    private FatturaRepository fatturaRepository;

    public List<Fattura> findAll() { return fatturaRepository.findAll(); }
    public Fattura saveFattura(Fattura fattura) { return fatturaRepository.save(fattura); }
    public void deleteFattura(Long id) { fatturaRepository.deleteById(id); }
    public Fattura findById(Long id) { return fatturaRepository.findById(id).orElse(null); }
}
