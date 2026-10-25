package com.cevicheria.platform.service.impl;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.Customer;
import com.cevicheria.platform.repository.CustomerRepository;
import com.cevicheria.platform.service.CustomerService;
import com.cevicheria.platform.specification.CustomerSpecification;

import lombok.RequiredArgsConstructor;

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
	public List<Customer> getCustomersByPreferences(String preferences) {
	    return customerRepository.findByPreferences(preferences);
	}

	@Override
	public Customer saveCustomer(Customer customer) {
	    return customerRepository.save(customer);
	}

	@Override
	public Customer updateCustomer(Long id, Customer customer) {
	    Customer currentCustomer = customerRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Customer", id));

		if(customer.getName() != null) {
		    currentCustomer.changeName(customer.getName());
		}

		if(customer.getDocumentNumber() != null || customer.getDocumentType() != null) {
		    currentCustomer.changeDocument(customer.getDocumentType(), customer.getDocumentNumber());
		}

		if(customer.getPhone() != null) {
		    currentCustomer.changePhone(customer.getPhone());
		}

		if(customer.getEmail() != null) {
		    currentCustomer.changeEmail(customer.getEmail());
		}

		if(customer.getBirthDate() != null) {
		    currentCustomer.changeBirthDate(customer.getBirthDate());
		}

		if(customer.getPreferences() != null) {
		    currentCustomer.changePreferences(customer.getPreferences());
		}

		return customerRepository.save(currentCustomer);
	}

	@Override
	public void deleteCustomerById(Long id) {
        Customer customer = getCustomerById(id);
	    customerRepository.deleteById(customer.getId());
	}

	public List<Customer> searchCustomers(
            String name,
            String documentType,
            String documentNumber,
            String phone,
            String email,
            Boolean active) {

        Specification<Customer> spec = Specification
                .where(CustomerSpecification.nameContains(name))
                .and(CustomerSpecification.documentTypeEquals(documentType))
                .and(CustomerSpecification.documentNumberEquals(documentNumber))
                .and(CustomerSpecification.phoneEquals(phone))
                .and(CustomerSpecification.emailEquals(email))
                .and(CustomerSpecification.activeEquals(active));

        return customerRepository.findAll(spec);
    }
}
