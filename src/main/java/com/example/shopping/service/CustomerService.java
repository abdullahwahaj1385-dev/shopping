package com.example.shopping.service;

import com.example.shopping.model.Customer;
import com.example.shopping.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    //metodo per ottenere da db tutta la lista clienti
    public List<Customer> findAll(){
        return customerRepository.findAll();
    }

    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }
     // la funzionalità
    public void deleteCustomer(Long id){

        customerRepository.deleteById(id);

    }

}


