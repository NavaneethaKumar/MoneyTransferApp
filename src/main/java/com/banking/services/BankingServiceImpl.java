package com.banking.services;

import com.banking.dto.AccountDetailsDTO;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.banking.exception.BankingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BankingServiceImpl implements IBankingService {

    private static final Logger logger = LoggerFactory.getLogger(BankingServiceImpl.class);

    /*
     * InMemory Caches
     */
    private Map<String, AccountDetailsDTO> accountDetailsMap = new ConcurrentHashMap();


    @Override
    public void register(AccountDetailsDTO accountDetails) {
        accountDetailsMap.put(accountDetails.getAccountNumber(),accountDetails);
    }

    @Override
    public double withdraw(String accountNumber, double amount) throws BankingException {
        return 0;
    }

    @Override
    public boolean transfer(String fromAccount, String toAccount, double amount) throws BankingException {
        return false;
    }

    @Override
    public int getAvailableBalance(String accountNumber) throws BankingException {
        return 0;
    }
}
