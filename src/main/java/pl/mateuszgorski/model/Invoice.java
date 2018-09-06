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

@ApiModel(value = "Invoice", description = "Sample model for the Invoice")
public class Invoice {

  @ApiModelProperty(value = "id assigned by database")
  private int id;

  @ApiModelProperty(value = "identifier provided by client", example = "6/2018")
  private String identifier;

  @ApiModelProperty(value = "Date in YYYY_MM_DD", example = "2018-12-08")
  private LocalDate issueDate;

  @ApiModelProperty(value = "Date in YYYY_MM_DD", example = "2018-12-06")
  private LocalDate saleDate;

  @ApiModelProperty(value = "Location where sale was made", example = "Krakow")
  private String salePlace;

  @ApiModelProperty(value = "Company - buyer")
  private Company buyer;

  @ApiModelProperty(value = "Company - seller")
  private Company seller;

  @ApiModelProperty(value = "List of invoice entries")
  private List<InvoiceEntry> entries = new ArrayList<>();

  public Invoice() {
  }

  public Invoice(Invoice invoice) {
    this.identifier = invoice.identifier;
    this.issueDate = invoice.issueDate;
    this.saleDate = invoice.saleDate;
    this.salePlace = invoice.salePlace;
    this.buyer = new Company(invoice.getBuyer());
    this.seller = new Company(invoice.getSeller());
    this.entries = InvoiceEntry.deepCopyListOfEntries(invoice.entries);
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

  public void addInvoiceEntry(InvoiceEntry invoiceEntry) {
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
      netValue = netValue.add(getNetValueOfInvoiceEntry(entry));
    }
    return netValue;
  }

  @JsonIgnore
  public BigDecimal getVatValue() {
    BigDecimal vatValue = BigDecimal.ZERO;
    for (InvoiceEntry entry : entries) {
      vatValue = vatValue.add(getNetValueOfInvoiceEntry(entry)
          .multiply(entry.getVatRate()));
    }
    return vatValue;
  }

  private BigDecimal getNetValueOfInvoiceEntry(InvoiceEntry entry) {
    return entry.getNetValueBeforeDiscount().multiply(BigDecimal.ONE.subtract(getBuyer().getDiscount()));
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this,
        ToStringStyle.MULTI_LINE_STYLE, true, true);
  }

  public static class InvoiceBuilder {

    Invoice invoice = new Invoice();

    public InvoiceBuilder identifier(String identifier) {
      invoice.identifier = identifier;
      return this;
    }

    public InvoiceBuilder issueDate(LocalDate issueDate) {
      invoice.issueDate = issueDate;
      return this;
    }

    public InvoiceBuilder saleDate(LocalDate saleDate) {
      invoice.saleDate = saleDate;
      return this;
    }

    public InvoiceBuilder salePlace(String salePlace) {
      invoice.salePlace = salePlace;
      return this;
    }

    public InvoiceBuilder buyer(Company buyer) {
      invoice.buyer = buyer;
      return this;
    }

    public InvoiceBuilder seller(Company seller) {
      invoice.seller = seller;
      return this;
    }

    public InvoiceBuilder entries(List<InvoiceEntry> entries) {
      if (invoice.entries == null) {
        invoice.entries = new ArrayList<InvoiceEntry>();
      }
      invoice.entries.addAll(entries);
      return this;
    }

    public Invoice build() {
      return invoice;
    }
  }
}