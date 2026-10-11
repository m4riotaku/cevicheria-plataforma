package com.cevicheria.platform.service;

import java.time.LocalDate;
import java.util.List;

import com.cevicheria.platform.model.Customer;

public interface CustomerService {

	List<Customer> getAllCustomers();

	Customer getCustomerById(Long id);

	List<Customer> getCustomersByName(String name);

	List<Customer> getCustomersByDocumentType(String documentType);

	List<Customer> getCustomersByDocumentNumber(String documentNumber);

	List<Customer> getCustomersByPhone(String phone);

	List<Customer> getCustomersByEmail(String email);

	List<Customer> getCustomersByBirthDate(LocalDate birthDate);

	List<Customer> getCustomersByPreferences(String preferences);

	Customer saveCustomer(Customer customer);

	Customer updateCustomer(Long id, Customer customer);

	void deleteCustomerById(Long id);

}
