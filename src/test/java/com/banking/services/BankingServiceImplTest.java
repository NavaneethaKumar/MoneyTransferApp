package com.banking.services;

import com.banking.dto.AccountDetailsDTO;
import com.banking.exception.BankingException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

public class BankingServiceImplTest {

  @InjectMocks
  private BankingServiceImpl bankingService;

  private AccountDetailsDTO accountDetailsDTO = new AccountDetailsDTO();
  private AccountDetailsDTO accountDetailsDTO1 = new AccountDetailsDTO();

  @Before
  public void setUp() throws Exception {

    accountDetailsDTO.setAmount(200);
    accountDetailsDTO.setAccountNumber("HDFC1234");
    accountDetailsDTO.setCountry("INDIA");
    accountDetailsDTO.setCurrency("INR");
    accountDetailsDTO.setName("LittleJohn");
    accountDetailsDTO1.setAmount(100);
    accountDetailsDTO1.setAccountNumber("ICICI1234");
    accountDetailsDTO1.setCountry("INDIA");
    accountDetailsDTO1.setCurrency("INR");
    accountDetailsDTO1.setName("John");
    MockitoAnnotations.initMocks(this);
  }

  @After
  public void tearDown() throws Exception {
    accountDetailsDTO=null;
  }

  @Test
  public void register_Success() throws BankingException {
    bankingService.register(accountDetailsDTO);
  }

  @Test(expected = BankingException.class)
  public void getAccountDetails_InvalidAccount() throws BankingException {
    Assert.assertEquals(
        "INDIA",
        bankingService.getAccountDetails(accountDetailsDTO.getAccountNumber()).getCountry());
  }

  @Test
  public void getAccountDetails_Success() throws BankingException {
    bankingService.register(accountDetailsDTO);
    Assert.assertEquals(
        "INDIA",
        bankingService.getAccountDetails(accountDetailsDTO.getAccountNumber()).getCountry());
  }

  @Test
  public void deposit() throws BankingException {

    bankingService.register(accountDetailsDTO);
    bankingService.deposit(accountDetailsDTO.getAccountNumber(), 500);
    Assert.assertEquals(
        500,
        bankingService.getAccountDetails(accountDetailsDTO.getAccountNumber()).getAmount(),
        0.0);
  }

  @Test
  public void getAllAccountDetails() throws BankingException {
    bankingService.register(accountDetailsDTO);
    bankingService.register(accountDetailsDTO1);
    bankingService.getAllAccountDetails();
    Assert.assertEquals(2, bankingService.getAllAccountDetails().size());
  }

  @Test(expected = BankingException.class)
  public void transfer_InvalidAccount() throws BankingException {
    bankingService.register(accountDetailsDTO);
    bankingService.transfer(
        accountDetailsDTO.getAccountNumber(), accountDetailsDTO1.getAccountNumber(), 200);
  }

  @Test(expected = BankingException.class)
  public void transfer_InsufficientFunds() throws BankingException {
    bankingService.register(accountDetailsDTO);
    bankingService.register(accountDetailsDTO1);
    bankingService.transfer(
        accountDetailsDTO.getAccountNumber(), accountDetailsDTO1.getAccountNumber(), 1000);
  }

  @Test
  public void transfer_Success() throws BankingException {
    bankingService.register(accountDetailsDTO);
    bankingService.register(accountDetailsDTO1);
    bankingService.transfer(
        accountDetailsDTO.getAccountNumber(), accountDetailsDTO1.getAccountNumber(), 100);
    Assert.assertEquals(
        200,
        bankingService.getAccountDetails(accountDetailsDTO1.getAccountNumber()).getAmount(),
        0.0);
  }

  @Test(expected = BankingException.class)
  public void deleteAccount_InvalidAccount() throws BankingException {
    bankingService.deleteAccount("HDFC");
  }

  @Test(expected = BankingException.class)
  public void deleteAccount_InvalidAccountNumber() throws BankingException {
    bankingService.deleteAccount("HDFC-$$&&");
  }

  @Test
  public void deleteAccount_Success() throws BankingException {
    bankingService.register(accountDetailsDTO);
    bankingService.deleteAccount(accountDetailsDTO.getAccountNumber());
  }
}
