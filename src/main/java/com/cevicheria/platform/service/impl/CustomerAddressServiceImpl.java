package com.cevicheria.platform.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cevicheria.platform.repository.CustomerAddressRepository;
import com.cevicheria.platform.service.CustomerAddressService;
import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.CustomerAddress;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class CustomerAddressServiceImpl implements CustomerAddressService {

    private final CustomerAddressRepository customerAddressRepository;

	@Override
	public List<CustomerAddress> getAllCustomerAddress() {
		return this.customerAddressRepository.findAll();
	}

	@Override
	public CustomerAddress saveCustomerAddress(CustomerAddress customer) {
		return this.customerAddressRepository.save(customer);
	}

	@Override
	public CustomerAddress updateCustomerAddress(Long id, CustomerAddress customerAddress) {
	    CustomerAddress currentCustomerAddress = customerAddressRepository.findById(id).orElseThrow(
					() -> new ResourceNotFoundException("CustomerAddress", "ID", id));

		if(customerAddress.getLabel() != null) {
		    currentCustomerAddress.changeLabel(customerAddress.getLabel());
		}

		if(customerAddress.getAddressLine() != null ) {
		    currentCustomerAddress.changeAddressLine(customerAddress.getAddressLine());
		}

		if(customerAddress.getReference() != null) {
		    currentCustomerAddress.changeReference(customerAddress.getReference());
		}

		if(customerAddress.getUbigeoCode() != null) {
		    currentCustomerAddress.changeUbigeoCode(customerAddress.getUbigeoCode());
		}

		if(customerAddress.getDepartment() != null) {
		    currentCustomerAddress.changeDepartment(customerAddress.getDepartment());
		}

		if(customerAddress.getProvince() != null) {
		    currentCustomerAddress.changeProvince(customerAddress.getProvince());
		}

		if(customerAddress.getDistrict() != null) {
			currentCustomerAddress.changeDistrict(customerAddress.getDistrict());
		}
		
		return customerAddressRepository.save(currentCustomerAddress);
	}

	@Override
	public void deleteCustomerAddressById(Long id) {
        CustomerAddress customerAddress = customerAddressRepository.findById(id).orElseThrow(
					() -> new ResourceNotFoundException("CustomerAddress", "ID", id));
	    customerAddressRepository.deleteById(customerAddress.getId());
	}
}
