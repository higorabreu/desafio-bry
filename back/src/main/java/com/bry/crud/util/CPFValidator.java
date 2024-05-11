package com.bry.crud.util;

import java.util.regex.Pattern;

public class CPFValidator {

  public static boolean isValid(String cpf) {
      cpf = cpf.replaceAll("[^0-9]", "");

      if (cpf.length() != 11) {
          return false;
      }
      if (Pattern.matches("(\\d)\\1{10}", cpf)) {
        return false;
    }

      int sum = 0;
      for (int i = 0; i < 9; i++) {
          sum += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
      }
      int remainder = 11 - (sum % 11);
      int digit1 = (remainder == 10 || remainder == 11) ? 0 : remainder;

      if (digit1 != Character.getNumericValue(cpf.charAt(9))) {
          return false;
      }

      sum = 0;
      for (int i = 0; i < 10; i++) {
          sum += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
      }
      remainder = 11 - (sum % 11);
      int digit2 = (remainder == 10 || remainder == 11) ? 0 : remainder;

      return digit2 == Character.getNumericValue(cpf.charAt(10));
  }
}

