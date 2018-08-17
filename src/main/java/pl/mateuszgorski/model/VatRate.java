package pl.mateuszgorski.model;

import java.math.BigDecimal;

public enum VatRate {
  NORMAL(BigDecimal.valueOf(0.23)),
  REDUCED_8(BigDecimal.valueOf(0.08)), // TODO not used even in tests :)
  REDUCED_7(BigDecimal.valueOf(0.07)), // TODO not used even in tests :)
  REDUCED_4(BigDecimal.valueOf(0.04)),
  ZERO(BigDecimal.ZERO);

  private BigDecimal vatRate;

  VatRate(BigDecimal vatRate) {
    this.vatRate = vatRate;
  }

  public BigDecimal getVatRateValue() {
    return vatRate;
  }
}