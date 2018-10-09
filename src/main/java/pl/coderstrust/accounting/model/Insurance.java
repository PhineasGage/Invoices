package pl.coderstrust.accounting.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@ApiModel(value = "InsuranceModel", description = "Sample model for the Insurance")
@Entity
public class Insurance implements ItemsForDatabase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @ApiModelProperty(value = "Issue Date in YYYY_MM_DD", example = "2018-12-08")
  private LocalDate issueDate;

  @ApiModelProperty(value = "Type of insurance", example = "PENSION")
  @Enumerated(EnumType.STRING)
  private InsuranceType type;

  @ApiModelProperty(value = "Amount of insurance", example = "800.00")
  private BigDecimal amount;

  @ApiModelProperty(value = "Nip of a company this insurance belongs to", example = "8421622720")
  private String nip;

  public Insurance() {
  }

  public Insurance(Insurance insurance) {
    this.id = insurance.id;
    this.issueDate = insurance.issueDate;
    this.type = insurance.type;
    this.amount = insurance.amount;
    this.nip = insurance.nip;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Override
  public Insurance makeDeepCopy(Object item) {
    return new Insurance((Insurance)item);
  }

  public LocalDate getIssueDate() {
    return issueDate;
  }

  public void setIssueDate(LocalDate issueDate) {
    this.issueDate = issueDate;
  }

  public InsuranceType getType() {
    return type;
  }

  public void setType(InsuranceType type) {
    this.type = type;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public String getNip() {
    return nip;
  }

  public void setNip(String nip) {
    this.nip = nip;
  }

  @Override
  public boolean equals(Object insurance) {
    if (this == insurance) {
      return true;
    }

    if (insurance == null || getClass() != insurance.getClass()) {
      return false;
    }

    Insurance that = (Insurance) insurance;

    return new EqualsBuilder()
        .append(issueDate, that.issueDate)
        .append(type, that.type)
        .append(amount, that.amount)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return Objects.hash(issueDate, type, amount);
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE, true, true);
  }

  public static class InsuranceBuilder {

    Insurance insurance = new Insurance();

    public InsuranceBuilder id(int id) {
      insurance.id = id;
      return this;
    }

    public InsuranceBuilder issueDate(LocalDate issueDate) {
      insurance.issueDate = issueDate;
      return this;
    }

    public InsuranceBuilder type(InsuranceType type) {
      insurance.type = type;
      return this;
    }

    public InsuranceBuilder amount(BigDecimal amount) {
      insurance.amount = amount;
      return this;
    }

    public InsuranceBuilder nip(String nip) {
      insurance.nip = nip;
      return this;
    }

    public Insurance build() {
      return insurance;
    }
  }
}