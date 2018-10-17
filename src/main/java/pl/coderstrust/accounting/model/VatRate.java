package pl.coderstrust.accounting.model;

import java.math.BigDecimal;

public enum VatRate {
  NORMAL(BigDecimal.valueOf(0.23), 0),
  REDUCED_8(BigDecimal.valueOf(0.08), 1),
  REDUCED_7(BigDecimal.valueOf(0.07), 2),
  REDUCED_4(BigDecimal.valueOf(0.04), 3),
  ZERO(BigDecimal.ZERO, 4);

  private BigDecimal vatRate;

  private int numberOfEnum;

  VatRate(BigDecimal vatRate, int numberOfEnum) {
    this.numberOfEnum = numberOfEnum;
    this.vatRate = vatRate;
  }

  public BigDecimal getVatRateValue() {
    return vatRate;
  }

  public int getNumberOfEnum() {
    return numberOfEnum;
  }

  public static VatRate findByKey(int integer) {
    VatRate[] testEnums = VatRate.values();
    for (VatRate testEnum : testEnums) {
      if (testEnum.numberOfEnum == integer) {
        return testEnum;
      }
    }
    return null;
  }
}