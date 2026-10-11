package com.cevicheria.platform.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cevicheria.platform.model.Customer;
import com.cevicheria.platform.service.CustomerService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public Customer getCustomerById(@PathVariable Long id) {
        return customerService.getCustomerById(id);
    }

    @GetMapping("/filter/documentType")
    public List<Customer> getCustomersByDocumentType(@RequestParam String documentType) {
        return customerService.getCustomersByDocumentType(documentType);
    }

    @GetMapping("/filter/document")
    public List<Customer> getCustomersByDocumentNumber(@RequestParam String documentNumber) {
        return customerService.getCustomersByDocumentNumber(documentNumber);
    }

    @GetMapping("/filter/phone")
    public List<Customer> getCustomersByPhone(@RequestParam String phone) {
        return customerService.getCustomersByPhone(phone);
    }

    @GetMapping("/filter/email")
    public List<Customer> getCustomersByEmail(@RequestParam String email) {
        return customerService.getCustomersByEmail(email);
    }

    @GetMapping("/filter/birthDate")
    public List<Customer> getCustomersByBirthDate(@RequestParam LocalDate birthDate) {
        return customerService.getCustomersByBirthDate(birthDate);
    }

    @GetMapping("/filter/preferences")
    public List<Customer> getCustomersByPreferences(@RequestParam String preferences) {
        return customerService.getCustomersByPreferences(preferences);
    }

    @PostMapping
    public Customer saveCustomer(@RequestBody Customer customer) {
        return customerService.saveCustomer(customer);
    }

    @PutMapping("/{id}")
    public Customer updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
        return customerService.updateCustomer(id, customer);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomerById(@PathVariable Long id) {
        customerService.deleteCustomerById(id);
    }
}
