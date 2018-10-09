package pl.coderstrust.accounting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang3.builder.EqualsBuilder;
import pl.coderstrust.accounting.model.Invoice.InvoiceBuilder;

@ApiModel(value = "InvoiceEntryModel", description = "Sample model for the Invoice Entry")
@Entity
public class InvoiceEntry {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @ApiModelProperty(value = "Description of entry", example = "Clamp")
  private String description;

  @ApiModelProperty(value = "Bigdecimal, net price", example = "10.86")
  private BigDecimal netPrice;

  @ApiModelProperty(value = "Vat rate", example = "0.23")
  private BigDecimal vatRate;

  @ApiModelProperty(value = "quantity", example = "10")
  private BigDecimal quantity;

  public String category;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public InvoiceEntry() {
  }

  public InvoiceEntry(InvoiceEntry invoiceEntry) {
    this.description = invoiceEntry.description;
    this.netPrice = invoiceEntry.netPrice;
    this.vatRate = invoiceEntry.vatRate;
    this.quantity = invoiceEntry.quantity;
    this.category = invoiceEntry.category;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public BigDecimal getNetPrice() {
    return netPrice;
  }

  public void setNetPrice(BigDecimal netPrice) {
    this.netPrice = netPrice;
  }

  public BigDecimal getVatRate() {
    return vatRate;
  }

  public void setVatRate(BigDecimal vatRate) {
    this.vatRate = vatRate;
  }

  public BigDecimal getQuantity() {
    return quantity;
  }

  public void setQuantity(BigDecimal quantity) {
    this.quantity = quantity;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  @Override
  public boolean equals(Object invoiceEntry) {
    if (this == invoiceEntry) {
      return true;
    }

    if (invoiceEntry == null || getClass() != invoiceEntry.getClass()) {
      return false;
    }

    InvoiceEntry that = (InvoiceEntry) invoiceEntry;

    return new EqualsBuilder()
        .append(description, that.description)
        .append(netPrice, that.netPrice)
        .append(vatRate, that.vatRate)
        .append(quantity, that.quantity)
        .append(category, that.category)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getDescription());
  }

  @JsonIgnore
  public BigDecimal getNetValueBeforeDiscount() {
    return getNetPrice().multiply(getQuantity());
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this,
        ToStringStyle.MULTI_LINE_STYLE, true, true);
  }

  public static List<InvoiceEntry> makeDeepCopyListOfEntries(List<InvoiceEntry> entries) {
    return entries.stream()
        .map(n -> new InvoiceEntry(n))
        .collect(Collectors.toList());
  }

  public InvoiceEntry makeDeepCopy(Object item) {
    return new InvoiceEntry((InvoiceEntry) item);
  }

  public static class InvoiceEntryBuilder {

    InvoiceEntry invoiceEntry = new InvoiceEntry();

    public InvoiceEntryBuilder id(int id) {
      invoiceEntry.id = id;
      return this;
    }

    public InvoiceEntryBuilder description(String description) {
      invoiceEntry.description = description;
      return this;
    }

    public InvoiceEntryBuilder netPrice(BigDecimal netPrice) {
      invoiceEntry.netPrice = netPrice;
      return this;
    }

    public InvoiceEntryBuilder vatRate(BigDecimal vatRate) {
      invoiceEntry.vatRate = vatRate;
      return this;
    }

    public InvoiceEntryBuilder quantity(BigDecimal quantity) {
      invoiceEntry.quantity = quantity;
      return this;
    }

    public InvoiceEntryBuilder category(String category) {
      invoiceEntry.category = category;
      return this;
    }

    public InvoiceEntry build() {
      return invoiceEntry;
    }
  }
}