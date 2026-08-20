package com.example.shopping.Controller;

import com.example.shopping.DTO.FatturaDTO;
import com.example.shopping.model.Fattura;
import com.example.shopping.service.FatturaService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/fatture")
public class FatturaController {

    private final FatturaService fatturaService;

    public FatturaController(FatturaService fatturaService) {
        this.fatturaService = fatturaService;
    }

    // 1. Elenco di tutte le fatture
    @GetMapping("/all")
    public List<Fattura> getAllFatture() {
        return fatturaService.findAll();
    }

    // 2. Creazione di una nuova fattura
    @PostMapping("/createNewFattura")
    public String createFattura(@RequestBody FatturaDTO fatturaDto) {
        try {
            Fattura nuovaFattura = new Fattura();

            nuovaFattura.setNumero(fatturaDto.getNumero());
            nuovaFattura.setCliente(fatturaDto.getCliente());

            // Convertiamo la stringa del DTO nel Double o LocalDate se serve,
            // oppure passiamo le stringhe se il tuo modello usa String.
            // Qui assumiamo che usi i metodi setter del tuo modello Fattura:
            nuovaFattura.setImporto(Double.valueOf(fatturaDto.getTotale()));
            nuovaFattura.setData(java.time.LocalDate.parse(fatturaDto.getData()));

            fatturaService.saveFattura(nuovaFattura);
            return "fattura inserita";
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Errore di inserimento fattura", e);
        }
    }

    // 3. Modifica di una fattura esistente
    @PutMapping("/{id}")
    public String updateFattura(@PathVariable Long id, @RequestBody FatturaDTO fatturaDto) {
        try {
            Fattura existingFattura = fatturaService.findById(id);
            if (existingFattura == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Fattura non trovata");
            }

            existingFattura.setNumero(fatturaDto.getNumero());
            existingFattura.setCliente(fatturaDto.getCliente());
            existingFattura.setImporto(Double.valueOf(fatturaDto.getTotale()));
            existingFattura.setData(java.time.LocalDate.parse(fatturaDto.getData()));

            fatturaService.saveFattura(existingFattura);
            return "fattura modificata";

        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Errore di modifica fattura", e);
        }
    }

    // 4. Cancellazione di una fattura
    @DeleteMapping("/{id}")
    public String deleteFattura(@PathVariable Long id) {
        try {
            fatturaService.deleteFattura(id);
            return "fattura cancellata";
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Errore di cancellazione fattura", e);
        }
    }

    // 5. Restituisce una singola fattura tramite ID
    @GetMapping("/{id}")
    public Fattura getFatturaById(@PathVariable Long id) {
        Fattura fattura = fatturaService.findById(id);
        if (fattura == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Fattura non trovata");
        }
        return fattura;
    }
}
