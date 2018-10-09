package pl.coderstrust.accounting.model;

public enum TaxType {

  LINEAR("LINEAR"),
  GRADED("GRADED");

  private String taxType;

  TaxType(String taxType) {
    this.taxType = taxType;
  }

  public String getTaxTypeString() {
    return taxType;
  }
}
