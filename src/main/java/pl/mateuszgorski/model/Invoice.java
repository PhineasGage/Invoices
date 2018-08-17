package pl.mateuszgorski.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

// TODO why do you add "Model" to name? I wouldn't change it - please check what is generated from it.
@ApiModel(value = "InvoiceModel", description = "Sample model for the Invoice")
public class Invoice {

  @ApiModelProperty(value = "id assigned by database", example = "1")
  private int id;

  @ApiModelProperty(value = "identifier provided by client", example = "6/2018")
  private String identifier;

  @ApiModelProperty(value = "Date in YYYY_MM_DD", example = "2018-12-08")
  private LocalDate issueDate;

  @ApiModelProperty(value = "Date in YYYY_MM_DD", example = "2018-12-06")
  private LocalDate saleDate;

  @ApiModelProperty(value = "Location where sale was made", example = "Krakow")
  private String salePlace;

  // TODO no need for description? :)
  private Company buyer;
  private Company seller;
  private List<InvoiceEntry> entries = new ArrayList<>();

  public Invoice() {
  }

  public Invoice(int id, String identifier, LocalDate issueDate, LocalDate saleDate,
      String salePlace, Company buyer, Company seller, List<InvoiceEntry> entries) {
    this.id = id;
    this.identifier = identifier;
    this.issueDate = issueDate;
    this.saleDate = saleDate;
    this.salePlace = salePlace;
    this.buyer = buyer;
    this.seller = seller;
    this.entries = entries;
  }

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public LocalDate getIssueDate() {
    return issueDate;
  }

  public void setIssueDate(LocalDate issueDate) {
    this.issueDate = issueDate;
  }

  public LocalDate getSaleDate() {
    return saleDate;
  }

  public void setSaleDate(LocalDate saleDate) {
    this.saleDate = saleDate;
  }

  public String getSalePlace() {
    return salePlace;
  }

  public void setSalePlace(String salePlace) {
    this.salePlace = salePlace;
  }

  public Company getSeller() {
    return seller;
  }

  public void setSeller(Company seller) {
    this.seller = seller;
  }

  public void setEntries(List<InvoiceEntry> entries) {
    this.entries = entries;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Company getBuyer() {
    return buyer;
  }

  public void setBuyer(Company buyer) {
    this.buyer = buyer;
  }

  public List<InvoiceEntry> getEntries() {
    return entries;
  }

  void addInvoiceEntry(InvoiceEntry invoiceEntry) {
    entries.add(invoiceEntry);
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    Invoice invoice = (Invoice) object;
    return getId() == invoice.getId();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }


  @JsonIgnore
  public BigDecimal getTotalNetValue() {
    BigDecimal netValue = BigDecimal.ZERO;
    for (InvoiceEntry entry : entries) {
      netValue = netValue.add(entry.getNetValue().multiply(BigDecimal.ONE.subtract(getBuyer().getDiscount())));
    }
    return netValue;
  }

  @JsonIgnore
  public BigDecimal getVatValue() {
    BigDecimal vatValue = BigDecimal.ZERO;
    for (InvoiceEntry entry : entries) {
      vatValue = vatValue.add((entry.getNetValue())
          .multiply(BigDecimal.ONE.subtract(getBuyer().getDiscount())) // TODO extract final price calculation to method
          .multiply(entry.getVatRate().getVatRateValue()));
    }
    return vatValue;
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this,
        ToStringStyle.MULTI_LINE_STYLE, true, true);
  }
}