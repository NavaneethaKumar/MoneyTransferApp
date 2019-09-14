package com.banking.services;

import com.banking.dto.AccountDetailsDTO;
import com.banking.exception.BankingException;

import java.util.List;

public interface IBankingService {

  void register(AccountDetailsDTO accountDetails) throws BankingException;

  void deposit(String accountNumber, double amount) throws BankingException;

  AccountDetailsDTO transfer(String fromAccount, String toAccount, double amount)
      throws BankingException;

  AccountDetailsDTO getAccountDetails(String accountNumber) throws BankingException;

  List<AccountDetailsDTO> getAllAccountDetails() throws BankingException;

  void deleteAccount(String accountNumber) throws BankingException;
}
