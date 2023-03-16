package org.jsp.Banking.Service;

import java.util.List;
import java.util.Optional;

import org.jsp.Banking.Dto.BankAccount;
import org.jsp.Banking.Dto.Management;
import org.jsp.Banking.Exception.MyException;
import org.jsp.Banking.Helper.ResponseStructure;
import org.jsp.Banking.Repository.Bankrepository;
import org.jsp.Banking.Repository.ManagementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ManagementService {
	@Autowired
	ManagementRepository repository;
	
	@Autowired
	Bankrepository repository2;
	public ResponseStructure<Management> save(Management management){
		ResponseStructure<Management>structure=new ResponseStructure<>();
		structure.setCode(HttpStatus.CREATED.value());
		structure.setMessage("Account created successfully");
		structure.setData(repository.save(management));
		return  structure;
	}
	public ResponseStructure<Management> login(Management management) throws MyException {
		ResponseStructure<Management> structure=new ResponseStructure<>();
		Management management1=repository.findByEmail(management.getEmail());
		if(management1==null) {
			
		  throw new MyException("Invalid Management Id");
		}
		else {
		
			if(management1.getPassword().equals(management.getPassword())) {
			
					structure.setCode(HttpStatus.ACCEPTED.value());
					structure.setMessage("Login success");
					structure.setData(management1);
				}
			
			else {
				throw new MyException("Invali password");
			}
		}

			return structure;

		}
	public ResponseStructure<List<BankAccount>> fetchAllAccounts() throws MyException {
		ResponseStructure<List<BankAccount>> structure=new ResponseStructure();
		List<BankAccount>list=repository2.findAll();
		if(list .isEmpty()) {
			throw new MyException("No Aacounts present");
			
		}
		else {
			structure.setCode(HttpStatus.FOUND.value());
			structure.setData(list);
			structure.setMessage("Data Found");
		}
		return structure;
	}
	public ResponseStructure<BankAccount> changestatus(long acno) {
	ResponseStructure<BankAccount> structure=new ResponseStructure<BankAccount>();
	Optional<BankAccount>optional=repository2.findById(acno);
	BankAccount account=optional.get();
	if(account.isStatus())
	{
		account.setStatus(false);
	}
	else {
		account.setStatus(true);
	}
	structure.setCode(HttpStatus.OK.value());
	structure.setMessage("changed status success");
	structure.setData(repository2.save(account));
		return structure;
	}
		
	}

