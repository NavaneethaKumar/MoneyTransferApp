package com.banking.dto;

public class AccountDetailsDTO {

  private String accountNumber;
  private String name;
  private String country;
  private String currency;
  private double amount;

  public String getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o == null || getClass() != o.getClass()) return false;

    AccountDetailsDTO that = (AccountDetailsDTO) o;

    return new org.apache.commons.lang3.builder.EqualsBuilder()
        .append(amount, that.amount)
        .append(accountNumber, that.accountNumber)
        .append(name, that.name)
        .append(country, that.country)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new org.apache.commons.lang3.builder.HashCodeBuilder(17, 37)
        .append(accountNumber)
        .append(name)
        .append(country)
        .append(amount)
        .toHashCode();
  }

  @Override
  public String toString() {
    return "AccountDetailsDTO{"
        + "accountNumber='"
        + accountNumber
        + '\''
        + ", name='"
        + name
        + '\''
        + ", country='"
        + country
        + '\''
        + ", accountBalance="
        + amount
        + '}';
  }
}
