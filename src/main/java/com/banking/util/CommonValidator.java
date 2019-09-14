package com.banking.util;

import com.banking.exception.BankingException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Validate user input fields .
 *  */
public class CommonValidator {

  static final Pattern PATTERN = Pattern.compile("^[0-9a-zA-Z]+$");

  private CommonValidator() {}

  public static void validateInput(String input) throws BankingException {
    Matcher matcher = PATTERN.matcher(input);
    if (!matcher.matches()) {
      throw new BankingException("400", "Invalid Input");
    }
  }
}
