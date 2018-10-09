package pl.coderstrust.accounting.model;

public enum InsuranceType {
  PENSION("PENSION"),
  HEALTH("HEALTH");

  private String insuranceType;

  InsuranceType(String insuranceType) {
    this.insuranceType = insuranceType;
  }

  public String getInsuranceTypeString() {
    return insuranceType;
  }
}
