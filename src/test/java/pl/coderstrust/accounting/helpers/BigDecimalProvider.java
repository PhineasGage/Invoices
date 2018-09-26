package pl.coderstrust.accounting.helpers;

import java.math.BigDecimal;

public class BigDecimalProvider {

  public static BigDecimal createBigDecimal(double value) {
    return new BigDecimal(value).setScale(2, BigDecimal.ROUND_HALF_UP);
  }
}
