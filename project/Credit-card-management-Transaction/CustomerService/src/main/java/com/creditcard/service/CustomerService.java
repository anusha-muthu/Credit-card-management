package com.creditcard.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.creditcard.entity.Customer;
import com.creditcard.repository.CustomerRepository;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer addCustomer(Customer customer) {
        if (customerRepository.existsByEmailAddress(customer.getEmailAddress())) {
            throw new RuntimeException("Email address already exists");
        }

        if (customerRepository.existsByPanNumber(customer.getPanNumber())) {
            throw new RuntimeException("PAN number already exists");
        }

        return customerRepository.save(customer);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));
    }

    public Customer updateCustomer(Long customerId, Customer updatedCustomer) {
        Customer existingCustomer = getCustomerById(customerId);

        existingCustomer.setCustomerName(updatedCustomer.getCustomerName());
        existingCustomer.setEmailAddress(updatedCustomer.getEmailAddress());
        existingCustomer.setMobileNumber(updatedCustomer.getMobileNumber());
        existingCustomer.setPanNumber(updatedCustomer.getPanNumber());

        return customerRepository.save(existingCustomer);
    }

    public void deleteCustomer(Long customerId) {
        Customer existingCustomer = getCustomerById(customerId);
        customerRepository.delete(existingCustomer);
    }
}