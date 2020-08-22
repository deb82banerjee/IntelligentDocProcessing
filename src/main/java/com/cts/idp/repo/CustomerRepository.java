package com.cts.idp.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.idp.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, String> {

}
