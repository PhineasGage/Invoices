package pl.mateuszgorski.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.Objects;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@ApiModel(value = "InvoiceEntryModel", description = "Sample model for the Invoice Entry")
public class InvoiceEntry {

  @ApiModelProperty(value = "Description of entry", example = "Clamp")
  private String description;

  @ApiModelProperty(value = "Bigdecimal, net price", example = "10.86")
  private BigDecimal netPrice;

  @ApiModelProperty(value = "Vat rate enum", example = "REDUCED_8") // TODO would be good to change code so user specify 7, 23 etc - enum values are
  // private to application
  private VatRate vatRate;

  @ApiModelProperty(value = "quantity", example = "10")
  private BigDecimal quantity;

  public InvoiceEntry() {
  }

  public InvoiceEntry(String description, BigDecimal netPrice,
      VatRate vatRate, BigDecimal quantity) {
    this.description = description;
    this.netPrice = netPrice;
    this.vatRate = vatRate;
    this.quantity = quantity;
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

  public VatRate getVatRate() {
    return vatRate;
  }

  public void setVatRate(VatRate vatRate) {
    this.vatRate = vatRate;
  }

  public BigDecimal getQuantity() {
    return quantity;
  }

  public void setQuantity(BigDecimal quantity) {
    this.quantity = quantity;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true; // TODO only description matters? are 2 invoice entries with same desc and diffrent price equal???
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    InvoiceEntry that = (InvoiceEntry) object;
    return Objects.equals(getDescription(), that.getDescription());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getDescription());
  }

  @JsonIgnore
  BigDecimal getNetValue() {
    return getNetPrice().multiply(getQuantity());
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this,
        ToStringStyle.MULTI_LINE_STYLE, true, true);
  }
}