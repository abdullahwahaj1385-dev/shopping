package com.example.shopping.Controller;

import com.example.shopping.DTO.CustomerDTO;
import com.example.shopping.model.Customer;
import com.example.shopping.service.CustomerService;
import org.springframework.web.bind.annotation.*;

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











    }



