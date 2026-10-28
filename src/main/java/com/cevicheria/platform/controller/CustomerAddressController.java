package com.cevicheria.platform.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cevicheria.platform.model.CustomerAddress;
import com.cevicheria.platform.service.CustomerAddressService;

import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping("/customer/address") 
@RequiredArgsConstructor 
public class CustomerAddressController {

	private final CustomerAddressService customerAddressService;

	@GetMapping 
	public List<CustomerAddress> getAllCustomerAddress() {
		return this.customerAddressService.getAllCustomerAddress();
	}

	@PostMapping
	public CustomerAddress saveCustomerAddress(@RequestBody CustomerAddress customerAddress) {
		return this.customerAddressService.saveCustomerAddress(customerAddress);
	}

	@PutMapping("/{id}")
	public CustomerAddress updateCustomerAddress(@RequestParam Long id, @RequestBody CustomerAddress customerAddress) {
		return this.customerAddressService.updateCustomerAddress(id, customerAddress);
	}
	@DeleteMapping("/{id}")
	public void deleteCustomerAddress(@RequestParam Long id) {
		this.customerAddressService.deleteCustomerAddressById(id);
	}
}	
