package com.cevicheria.platform.service;

import java.util.List;

import com.cevicheria.platform.model.CustomerAddress;

public interface CustomerAddressService {

	
	List<CustomerAddress> getAllCustomerAddress();

	CustomerAddress saveCustomerAddress(CustomerAddress customer);

	CustomerAddress updateCustomerAddress(Long id, CustomerAddress customer);

	void deleteCustomerAddressById(Long id);
}
