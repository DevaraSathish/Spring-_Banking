package org.jsp.Banking.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.jsp.Banking.Dto.BankAccount;
import org.jsp.Banking.Dto.BankTranscation;
import org.jsp.Banking.Dto.Customer;
import org.jsp.Banking.Dto.Login;
import org.jsp.Banking.Exception.MyException;
import org.jsp.Banking.Helper.Mailverification;
import org.jsp.Banking.Helper.ResponseStructure;
import org.jsp.Banking.Repository.Bankrepository;
import org.jsp.Banking.Repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
@Autowired
CustomerRepository repository;

@Autowired
Mailverification mailverification;

@Autowired
BankAccount account;
@Autowired
Bankrepository bankrepository;

@Autowired
BankTranscation transcation;

public ResponseStructure<Customer> save(Customer customer) throws MyException{
ResponseStructure<Customer>structure=new ResponseStructure<>();
int age=Period.between(customer.getDob().toLocalDate(),LocalDate.now()).getYears();
if(age<18) {
	throw new MyException("you should be +18 to create account");
	
}
else {
	Random random=new Random();
	int otp=random.nextInt(100000, 888888);
	customer.setOtp(otp);
	
//	mailverification.sendmail(customer);
	
	structure.setMessage("Verification email");
	structure.setCode(HttpStatus.PROCESSING.value());
	structure.setData(repository.save(customer));
}

return structure;
}

public ResponseStructure<Customer> verify(int custid, int otp) throws MyException{
	ResponseStructure<Customer>structure = new ResponseStructure<>();
Optional<Customer> optional=repository.findById(custid);
if(optional.isEmpty()) {
	
  throw new MyException("Check Id and Try Again");
}
else {
	Customer customer=optional.get();
	if(customer.getOtp()==otp) {
		structure.setCode(HttpStatus.CREATED.value());
		structure.setMessage("Account Created Successfully");
		customer.setStatus(true);
		structure.setData(repository.save(customer));
	}
	else {
		throw new MyException("otp is not valid");
	}
}
	return structure;
}

public ResponseStructure<Customer> login(Login login) throws MyException {
ResponseStructure<Customer> structure=new ResponseStructure<>();
Optional<Customer> optional=repository.findById(login.getId());
if(optional.isEmpty()) {
	
  throw new MyException("Invalid Customer Id");
}
else {
	Customer customer=optional.get();
	if(customer.getPassword().equals(login.getPassword())) {
		if(customer.isStatus()) {
			structure.setCode(HttpStatus.ACCEPTED.value());
			structure.setMessage("Login success");
			structure.setData(customer);
		}else {
			throw new MyException("verify your email first");
		}
	}
	else {
		throw new MyException("Invali password");
	}
}

	return structure;

}

public ResponseStructure<Customer> createAccount(int cust_id, String type) throws MyException {
	ResponseStructure<Customer> structure=new ResponseStructure<>();
	Optional<Customer> optional=repository.findById(cust_id);
	if(optional.isEmpty()) {
		
	  throw new MyException("Invalid Customer Id");
	}
	else {
		Customer customer=optional.get();
		List<BankAccount> list=customer.getAccounts();
		
		boolean flag =true;
		for(BankAccount account:list) {
			if(account.getType().equals(type)) {
				flag=false;
				break;
			}
		}
	if(!flag) {
		throw  new MyException(type+"Account already exists");
	}
	account.setType("savings");
	if(type.equals("savings")) 
	{
		account.setBanklimit(5000);
	}
	else
	{
		account.setBanklimit(10000);
	}
	list.add(account);
	customer.setAccounts(list);
	
	structure.setCode(HttpStatus.ACCEPTED.value());
	structure.setMessage("account created wait for management approve");
	structure.setData(repository.save(customer));
	}
	return  structure;
}

public ResponseStructure<List<BankAccount>> fetchAllTrue(int custid) throws MyException {
	ResponseStructure<List<BankAccount>> structure=new ResponseStructure<List<BankAccount>>();
	Optional<Customer> optional=repository.findById(custid);
	Customer customer=optional.get();
	List<BankAccount> list= customer.getAccounts();
	List<BankAccount>res= new ArrayList<BankAccount>();
	for(BankAccount account:list) {
		if(account.isStatus()) {
			
		}
	}
	if(res.isEmpty()) {
		throw new MyException("No Active Accounts Found");
	}
	else {
		structure.setCode(HttpStatus.FOUND.value());
		structure.setMessage("Accounts Found");
		structure.setData(res);
	}
	return structure;
}

public ResponseStructure<Double> checkBalance(long acno) {
	ResponseStructure<Double> structure=new ResponseStructure<>();
	Optional<BankAccount> optional=bankrepository.findById(acno);
	BankAccount account=optional.get();
	
	structure.setCode(HttpStatus.FOUND.value());
	structure.setData(account.getAmount());
	structure.setMessage("Data found");
	return structure;
}

public ResponseStructure<BankAccount> deposit(long acno, double amount) {
	ResponseStructure<BankAccount>structure=new ResponseStructure<BankAccount>();
	BankAccount account=bankrepository.findById(acno).get();
	account.setAmount(account.getAmount()+amount);
	
	transcation.setDateTime(LocalDateTime.now());
	transcation.setDeposit(amount);
	transcation.setBalance(account.getAmount());
	List<BankTranscation>transcations=account.getBankTranscation();
	transcations.add(transcation);
	
	account.setBankTranscation(transcations);
	
	structure.setCode(HttpStatus.ACCEPTED.value());
	structure.setData(bankrepository.save(account));
	structure.setMessage("Amount added Successfully");
	return structure;
}

public ResponseStructure<BankAccount> withdraw(long acno, double amount) throws MyException {
	ResponseStructure<BankAccount> structure=new ResponseStructure<BankAccount>();
	BankAccount account=bankrepository.findById(acno).get();
	
	if(amount>account.getBanklimit())
	{
		throw new MyException("Out of limit");
	}
	else {
		if(amount>account.getAmount())
		{
		throw new MyException("Insufficient funds");
		}
		else {
	account.setAmount(account.getAmount()+amount);
	
	transcation.setDateTime(LocalDateTime.now());
	transcation.setDeposit(amount);
	transcation.setBalance(account.getAmount());
	List<BankTranscation>transcations=account.getBankTranscation();
	transcations.add(transcation);
	
	account.setBankTranscation(transcations);
	
	structure.setCode(HttpStatus.ACCEPTED.value());
	structure.setData(bankrepository.save(account));
	structure.setMessage("Amount withdraw Successfully");
		}
	}
	return structure;
		
}

public ResponseStructure<List<BankTranscation>> viewtranscation(long acno) throws MyException {
	ResponseStructure<List<BankTranscation>> structure=new ResponseStructure<List<BankTranscation>>();
			BankAccount account=bankrepository.findById(acno).get();
	List<BankTranscation>list=account.getBankTranscation();
	if(list.isEmpty()) {
		throw new MyException("No Transcation");
	}
	else {
		structure.setCode(HttpStatus.FOUND.value());
		structure.setMessage("Data Found");
		structure.setData(list);
	}
	return structure;
}	
}


