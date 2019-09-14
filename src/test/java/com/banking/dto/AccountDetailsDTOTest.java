package com.banking.dto;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

public class AccountDetailsDTOTest {



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

  @Test
  public void equalsTest() {
    Assert.assertFalse(accountDetailsDTO.equals(accountDetailsDTO1));

  }

  @Test
  public void hashCodeTest() {
    Assert.assertNotNull(accountDetailsDTO.hashCode());
  }

  @Test
  public void toStringTest() {
    accountDetailsDTO.toString();
  }
}
