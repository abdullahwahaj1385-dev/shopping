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


    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    // la funzionalità
    public void deleteCustomer(Long id) {

        customerRepository.deleteById(id);


    }


    public Customer findById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }


}






