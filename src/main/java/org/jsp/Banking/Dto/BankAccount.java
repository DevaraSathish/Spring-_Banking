package org.jsp.Banking.Dto;

import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;

@Entity
@Component
@Data
public class BankAccount {
@Id
@SequenceGenerator(name="acno",sequenceName ="acno",initialValue= 100211236,allocationSize =1 )
@GeneratedValue(generator = "acno")
long number;
String type;
double banklimit;
double amount;
boolean status;

@OneToMany(cascade =CascadeType.ALL)
List<BankTranscation> bankTranscation;
}
