package com.banking.domain;

import com.banking.exception.BankingException;
import com.banking.util.CommonValidator;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect
public class Account {

  @JsonProperty(required = true)
  private String accountNumber;

  @JsonProperty(required = true)
  private String name;

  @JsonProperty(required = true)
  private String country;

  @JsonProperty(required = true)
  private double amount;

  @JsonProperty(required = true)
  private String currency;

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(final String currency) throws BankingException {
    CommonValidator.validateInput(currency);
    this.currency = currency;
  }

  public String getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(final String accountNumber) throws BankingException {
    CommonValidator.validateInput(accountNumber);
    this.accountNumber = accountNumber;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) throws BankingException {
    CommonValidator.validateInput(name);
    this.name = name;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(final String country) throws BankingException {
    CommonValidator.validateInput(country);
    this.country = country;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }
}
