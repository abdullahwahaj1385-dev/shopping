package com.example.shopping.Controller;

import com.example.shopping.DTO.CustomerDTO;
import com.example.shopping.model.Customer;
import com.example.shopping.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {

        this.customerService = customerService;
    }

    // 1. Elenco  tutti i clienti
    @GetMapping("/all")
    public List<Customer> getAllCustomers() {
        return customerService.findAll();
    }
    
      //La creazione
    @PostMapping("/createNewCustomer")
    public String createCustomer(@RequestBody CustomerDTO customerdto) {
       try {
           Customer nuovoCliente = new Customer();

           nuovoCliente.setFullName(customerdto.getFullName());
           nuovoCliente.setEmail(customerdto.getEmail());
           nuovoCliente.setPhone(customerdto.getPhone());

           customerService.saveCustomer(nuovoCliente);


           return "cliente inserito";
       }

       catch(Exception e) {
               throw new org.springframework.web.server.ResponseStatusException(
                       org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR, "Errore di inserimento", e);

           }

    }

    //La Modifica
    @PutMapping("/{id}")
    public String updateCustomer(@PathVariable Long id, @RequestBody CustomerDTO customerdto) {
        try {
            Customer existingCustomer = customerService.findById(id);
            if (existingCustomer == null) {

                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente non trovato");
            }

            existingCustomer.setFullName(customerdto.getFullName());
            existingCustomer.setEmail(customerdto.getEmail());
            existingCustomer.setPhone(customerdto.getPhone());

            customerService.saveCustomer(existingCustomer);
            return "cliente modificato";

        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Errore di modifica", e);
        }
    }


    //Delete
    @DeleteMapping("/{id}")
    public String deleteCustomer(@PathVariable Long id) {
        try {
            customerService.deleteCustomer(id);
            return "cliente cancellato";
        } catch (Exception e) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR, "Errore di cancellazione", e);
        }
    }

    // 5. Restituisce utente per ID
    @GetMapping("/{id}")
    public Customer getCustomerById(@PathVariable Long id) {
        Customer customer = customerService.findById(id);
        if (customer == null) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.NOT_FOUND, "Cliente non trovato");
        }
        return customer;
    }
}








