package org.jsp.Banking.Repository;

import org.jsp.Banking.Dto.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer>{

}
