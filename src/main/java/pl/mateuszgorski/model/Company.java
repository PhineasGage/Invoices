package pl.mateuszgorski.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.stereotype.Repository;

@Repository // TODO what?? Why do you mark POJO as repository?
@ApiModel(value = "Company", description = "Model for the Company")
public class Company {

  @ApiModelProperty(value = "Name of the Company", example = "Poldim SA")
  private String name;

  @ApiModelProperty(value = "Tax identification number", example = "9930105608")
  private String nip;

  @ApiModelProperty(value = "Adress, street", example = "Zakladowa")
  private String street;

  @ApiModelProperty(value = "Adress, postal code", example = "31-200")
  private String postalCode;

  @ApiModelProperty(value = "Adress, city", example = "Krakow")
  private String city;

  @ApiModelProperty(value = "BigDecimal, discount", example = "0.1")
  private BigDecimal discount;

  public Company() {
  }

  public Company(String name, String nip, String street, String postalCode, String city,
      BigDecimal discount) {
    this.name = name;
    this.nip = nip;
    this.street = street;
    this.postalCode = postalCode;
    this.city = city;
    this.discount = discount;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getNip() {
    return nip;
  }

  public void setNip(String nip) {
    this.nip = nip;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public BigDecimal getDiscount() {
    return discount;
  }

  public void setDiscount(BigDecimal discount) {
    this.discount = discount;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    Company company = (Company) obj;

    return new EqualsBuilder()
        .append(discount, company.discount)
        .append(name, company.name)
        .append(nip, company.nip)
        .append(street, company.street)
        .append(postalCode, company.postalCode)
        .append(city, company.city)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(name)
        .append(nip)
        .append(street)
        .append(postalCode)
        .append(city)
        .append(discount)
        .toHashCode();
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this,
        ToStringStyle.MULTI_LINE_STYLE, true, true);
  }
}
