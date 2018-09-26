package pl.coderstrust.accounting.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


@ApiModel(value = "Company", description = "Model for the Company")

@Entity
public class Company implements ItemsForDatabase<Company> {

  @ApiModelProperty(value = "id assigned by database", example = "1")
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

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

  public Company(Company company) {
    this.name = company.name;
    this.nip = company.nip;
    this.street = company.street;
    this.postalCode = company.postalCode;
    this.city = company.city;
    this.discount = company.discount;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Override
  public Company makeDeepCopy(Company item) {
    return new Company(item);
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
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }

    if (other == null || getClass() != other.getClass()) {
      return false;
    }

    Company company = (Company) other;

    return new EqualsBuilder()
        .append(id, company.id)
        .append(name, company.name)
        .append(nip, company.nip)
        .append(street, company.street)
        .append(postalCode, company.postalCode)
        .append(city, company.city)
        .append(discount, company.discount)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(id)
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

  public static class CompanyBuilder {

    Company company = new Company();

    public CompanyBuilder id(int id) {
      company.id = id;
      return this;
    }

    public CompanyBuilder name(String name) {
      company.name = name;
      return this;
    }

    public CompanyBuilder nip(String nip) {
      company.nip = nip;
      return this;
    }

    public CompanyBuilder street(String street) {
      company.street = street;
      return this;
    }

    public CompanyBuilder postalCode(String postalCode) {
      company.postalCode = postalCode;
      return this;
    }

    public CompanyBuilder city(String city) {
      company.city = city;
      return this;
    }

    public CompanyBuilder discount(BigDecimal discount) {
      company.discount = discount;
      return this;
    }

    public Company build() {
      return company;
    }
  }
}