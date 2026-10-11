package com.cevicheria.platform.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.Customer;
import com.cevicheria.platform.repository.CustomerRepository;
import com.cevicheria.platform.service.CustomerService;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
	public List<Customer> getAllCustomers() {
	    return customerRepository.findAll();
	}
	@Override
	public Customer getCustomerById(Long id) {
	    return customerRepository.findById(id).orElseThrow(
					() -> new ResourceNotFoundException("Customer", id));
	}

	@Override
	public List<Customer> getCustomersByName(String name) {
	    return customerRepository.findByName(name);
	}

	@Override
	public List<Customer> getCustomersByDocumentType(String documentType) {
	    return customerRepository.findByDocumentType(documentType);
	}

	@Override
	public List<Customer> getCustomersByDocumentNumber(String documentNumber) {
	    return customerRepository.findByDocumentNumber(documentNumber);
	}

	@Override
	public List<Customer> getCustomersByPhone(String phone) {
	    return customerRepository.findByPhone(phone);
	}

	@Override
	public List<Customer> getCustomersByEmail(String email) {
	    return customerRepository.findByEmail(email);
	}

	@Override
	public List<Customer> getCustomersByBirthDate(LocalDate birthDate) {
	    return customerRepository.findByBirthDate(birthDate);
	}

	@Override
	public List<Customer> getCustomersByPreferences(String preferences) {
	    return customerRepository.findByPreferences(preferences);
	}

	@Override
	public Customer saveCustomer(Customer customer) {
	    return customerRepository.save(customer);
	}

	@Override
	public Customer updateCustomer(Long id, Customer customer) {
	    Customer currentCustomer = getCustomerById(id);

	    currentCustomer.setActive(customer.getActive());

		if(customer.getName() != null) {
		    currentCustomer.setName(customer.getName());
		}

		if (customer.getDocumentType() != null) {
		    currentCustomer.setDocumentType(customer.getDocumentType());
		}

		if(customer.getDocumentNumber() != null) {
		    currentCustomer.setDocumentNumber(customer.getDocumentNumber());
		}

		if(customer.getPhone() != null) {
		    currentCustomer.setPhone(customer.getPhone());
		}

		if(customer.getEmail() != null) {
		    currentCustomer.setEmail(customer.getEmail());
		}

		if(customer.getBirthDate() != null) {
		    currentCustomer.setBirthDate(customer.getBirthDate());
		}

		if(customer.getAddresses() != null) {
		    currentCustomer.setAddresses(customer.getAddresses());
		}

		if(customer.getPreferences() != null) {
		    currentCustomer.setPreferences(customer.getPreferences());
		}

		return customerRepository.save(currentCustomer);
	}

	@Override
	public void deleteCustomerById(Long id) {
        Customer customer = getCustomerById(id);
	    customerRepository.deleteById(customer.getId());
	}
}
