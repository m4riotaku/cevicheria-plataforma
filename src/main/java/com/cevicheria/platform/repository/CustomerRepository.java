package com.cevicheria.platform.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

   	List<Customer> findByName(String name);

	List<Customer> findByDocumentType(String documentType);

	List<Customer> findByDocumentNumber(String documentNumber);

	List<Customer> findByPhone(String phone);

	List<Customer> findByEmail(String email);

	List<Customer> findByBirthDate(LocalDate birthDate);

	List<Customer> findByPreferences(String preferences);
}
