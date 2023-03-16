package org.jsp.Banking.Repository;

import org.jsp.Banking.Dto.Management;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagementRepository  extends JpaRepository<Management, Integer>{

	Management findByEmail(String email);

	

}
