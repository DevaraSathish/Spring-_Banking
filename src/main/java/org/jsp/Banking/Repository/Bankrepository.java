package org.jsp.Banking.Repository;

import org.jsp.Banking.Dto.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface Bankrepository extends JpaRepository<BankAccount, Long>{

}
