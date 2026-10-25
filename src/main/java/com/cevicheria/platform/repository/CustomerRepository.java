package com.cevicheria.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.cevicheria.platform.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {

   	List<Customer> findByName(String name);

	List<Customer> findByDocumentNumber(String documentNumber);

	List<Customer> findByPhone(String phone);

	List<Customer> findByEmail(String email);

	List<Customer> findByPreferences(String preferences);
}
