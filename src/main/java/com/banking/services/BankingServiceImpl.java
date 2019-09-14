package com.banking.services;

import com.banking.dto.AccountDetailsDTO;
import com.banking.exception.BankingException;
import com.banking.util.CommonValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/** Service Layer which does all functional operations. */
public class BankingServiceImpl implements IBankingService {

  private static final Logger logger = LoggerFactory.getLogger(BankingServiceImpl.class);

  /*
   * InMemory Caches
   */
  private Map<String, AccountDetailsDTO> accountDetailsMap = new ConcurrentHashMap();

  /**
   * Register Account Details.
   *
   * @param accountDetails
   */
  @Override
  public void register(AccountDetailsDTO accountDetails) {
    logger.info("register");
    accountDetailsMap.put(accountDetails.getAccountNumber(), accountDetails);
  }

  /**
   * Deposit Amount.
   *
   * @param accountNumber
   * @param amount
   * @throws BankingException
   */
  @Override
  public void deposit(String accountNumber, double amount) throws BankingException {
    logger.info("deposit");
    CommonValidator.validateInput(accountNumber);
    validateAccount(accountNumber);
    addAmount(accountNumber, amount);
  }

  /**
   * Transfer Funds from OneAccount to Other.
   *
   * @param fromAccount
   * @param toAccount
   * @param amount
   * @return
   * @throws BankingException
   */
  @Override
  public AccountDetailsDTO transfer(String fromAccount, String toAccount, double amount)
      throws BankingException {
    logger.info("transfer");
    CommonValidator.validateInput(fromAccount);
    CommonValidator.validateInput(toAccount);
    return transferAmount(fromAccount, toAccount, amount);
  }

  /**
   * Returns account details based on account number.
   *
   * @param accountNumber
   * @return
   * @throws BankingException
   */
  @Override
  public AccountDetailsDTO getAccountDetails(String accountNumber) throws BankingException {
    logger.info("getAccountDetails");
    CommonValidator.validateInput(accountNumber);
    return Optional.ofNullable(accountDetailsMap.get(accountNumber))
        .orElseThrow(() -> new BankingException("400", "Account Does Not Exists"));
  }

  /**
   * Returns list of all available account details.
   *
   * @return
   * @throws BankingException
   */
  @Override
  public List<AccountDetailsDTO> getAllAccountDetails() throws BankingException {
    logger.info("getAllAccountDetails");
    return accountDetailsMap.values().stream().collect(Collectors.toList());
  }

  /**
   * Delete account based on account number.
   *
   * @param accountNumber
   * @throws BankingException
   */
  @Override
  public void deleteAccount(String accountNumber) throws BankingException {
    logger.info("deleteAccount");
    CommonValidator.validateInput(accountNumber);
    validateAccount(accountNumber);
    accountDetailsMap.remove(accountNumber);
  }

  /**
   * Update amount based on account number.
   *
   * @param accountNumber
   * @param amount
   */
  private void addAmount(String accountNumber, double amount) {
    logger.info("addAmount");
    AccountDetailsDTO accountDetailsDTO = accountDetailsMap.get(accountNumber);
    accountDetailsDTO.setAmount(amount);
    accountDetailsMap.put(accountNumber, accountDetailsDTO);
  }

  /**
   * Transfer and validates account details.
   *
   * @param fromAccountNumber
   * @param toAccountNumber
   * @param amount
   * @return
   * @throws BankingException
   */
  private AccountDetailsDTO transferAmount(
      String fromAccountNumber, String toAccountNumber, double amount) throws BankingException {
    logger.info("transferAmount");
    validateAccount(fromAccountNumber);
    validateAccount(toAccountNumber);
    AccountDetailsDTO fromAccountDetailsDTO = accountDetailsMap.get(fromAccountNumber);
    double fromAccountAmount = fromAccountDetailsDTO.getAmount();
    if (fromAccountAmount < amount) {
      throw new BankingException("400", "Insufficient Funds");
    }
    fromAccountAmount = fromAccountAmount - amount;
    fromAccountDetailsDTO.setAmount(fromAccountAmount);
    accountDetailsMap.put(fromAccountNumber, fromAccountDetailsDTO);
    AccountDetailsDTO toAccountDetailsDTO = accountDetailsMap.get(toAccountNumber);
    toAccountDetailsDTO.setAmount(toAccountDetailsDTO.getAmount() + amount);
    accountDetailsMap.put(toAccountNumber, toAccountDetailsDTO);
    return toAccountDetailsDTO;
  }

  private void validateAccount(String fromAccountNumber) throws BankingException {
    if (accountDetailsMap.get(fromAccountNumber) == null) {
      throw new BankingException("400", "Account Does Not Exists");
    }
  }
}
