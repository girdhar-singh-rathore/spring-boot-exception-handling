package com.example.resource;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.domain.Customer;
import com.example.exceptions.CustomerNotFoundException;
import com.example.service.CustomerService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	@GetMapping("customers")
	public List<Customer> getAllCustomer() {
		log.debug("getting all customers..");
		return customerService.listAll();
	}

	@PostMapping("customers")
	public void addCustomer(@RequestBody Customer customer) {
		log.debug("Customer saved: {}", customer);
		customerService.saveOrUpdate(customer);
	}

	@PutMapping("customers")
	public ResponseEntity<Customer> updateCustomer(@Valid @RequestBody Customer customerDetails) {
		Customer customer = customerService.getById(customerDetails.getId());
		if (customer == null) {
			log.debug("customer is not available: {}", customerDetails);
			throw new CustomerNotFoundException("Customer is not available: " + customerDetails.getId());
		}
		Customer updatedCustomer = customerService.saveOrUpdate(customerDetails);
		log.debug("customer updated: {}", updatedCustomer);
		return ResponseEntity.ok(updatedCustomer);
	}

	@GetMapping("/customers/{id}")
	public ResponseEntity<Customer> getCustomer(@PathVariable(value = "id") String id) {
		Customer customer = customerService.getById(id);
		log.debug("getting customer with id: {}", id);
		if (customer == null) {
			log.debug("customer not available with id: {}", id);
			throw new CustomerNotFoundException("Customer is not available: " + id);
		}
		log.debug("getting customer by id: customer details is: {}", customer);
		return ResponseEntity.ok().body(customer);
	}

	@DeleteMapping("/customers/{id}")
	public ResponseEntity<Customer> deleteCustomer(@PathVariable(value = "id") String id) {
		log.debug("deleting customer having id: {}", id);
		Customer customer = customerService.getById(id);
		if (customer == null) {
			log.debug("customer with id not avaible: {}", id);
			throw new CustomerNotFoundException("Customer is not available: " + id);
		}
		customerService.delete(id);
		log.debug("customer deleted successfully: {}", customer);
		return ResponseEntity.ok().build();
	}
}
