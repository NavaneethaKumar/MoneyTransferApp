package com.banking.services;

import com.banking.dto.AccountDetailsDTO;
import com.banking.exception.BankingException;


public interface IBankingService {

     void register(AccountDetailsDTO accountDetails);
     double withdraw(String accountNumber,double amount) throws BankingException;
     boolean transfer(String fromAccount,String toAccount, double amount) throws BankingException;
     int getAvailableBalance(String accountNumber) throws BankingException;
}
