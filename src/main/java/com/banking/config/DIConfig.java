package com.banking.config;

import com.banking.services.BankingServiceImpl;
import com.banking.services.IBankingService;
import com.google.inject.AbstractModule;

/** Dependency Injection Configs */
public class DIConfig extends AbstractModule {

  @Override
  protected void configure() {
    bind(IBankingService.class).to(BankingServiceImpl.class);
  }
}
