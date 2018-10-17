package pl.coderstrust.accounting.logic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderstrust.accounting.model.Insurance;
import pl.coderstrust.accounting.model.InsuranceType;
import pl.coderstrust.accounting.model.Invoice;
import pl.coderstrust.accounting.model.InvoiceEntry;
import pl.coderstrust.accounting.model.TaxType;

@Service
public class TaxCalculatorServiceWithDates {

  private InvoiceService invoiceService;
  private CompanyService companyService;
  private InsuranceService insuranceService;

  private static BigDecimal TAX_THRESHOLD = BigDecimal.valueOf(85528); //todo keep this value in database/properties file

  @Autowired
  public TaxCalculatorServiceWithDates(InvoiceService invoiceService,
                                       CompanyService companyService,
                                       InsuranceService insuranceService) {
    this.invoiceService = invoiceService;
    this.insuranceService = insuranceService;
    this.companyService = companyService;
  }

  private BigDecimal getValueFromInvoices(BiPredicate<Invoice, String> buyerOrSeller, Function<Invoice, BigDecimal> taxOrIncomeToBigDecimal,
      String nip, LocalDate startDate, LocalDate endDate) {

    return invoiceService
        .getInvoicesByIssueDate(startDate, endDate)
        .stream()
        .filter(invoice -> buyerOrSeller.test(invoice, nip))
        .map(taxOrIncomeToBigDecimal)
        .reduce(this::add)
        .orElse(BigDecimal.ZERO);
  }

  private BigDecimal add(BigDecimal sum, BigDecimal item) {
    return sum.add(item);
  }

  private boolean filterBuyerByNip(Invoice invoice, String nip) {
    return invoice.getBuyer().getNip().equals(nip);
  }

  private boolean filterSellerByNip(Invoice invoice, String nip) {
    return invoice.getSeller().getNip().equals(nip);
  }

  private BigDecimal taxToBigDecimal(Invoice invoice) {
    return getVatValue(invoice);
  }

  private BigDecimal incomeVatToBigDecimal(Invoice invoice) {
    return getIncomeVatValue(invoice);
  }

  private BigDecimal incomeToBigDecimal(Invoice invoice) {
    return getTotalNetValue(invoice);
  }

  private BigDecimal costToBigDecimal(Invoice invoice) {
    return getCostValue(invoice);
  }

  private Map<String, BigDecimal> taxDetail = new LinkedHashMap<>();

  public BigDecimal getIncome(String nip, LocalDate startDate, LocalDate endDate) {
    BigDecimal income = getValueFromInvoices(this::filterSellerByNip, this::incomeToBigDecimal, nip, startDate, endDate);
    taxDetail.put("Income: ", income.setScale(2, RoundingMode.HALF_UP));
    return income;
  }

  public BigDecimal getCosts(String nip, LocalDate startDate, LocalDate endDate) {
    BigDecimal costs = getValueFromInvoices(this::filterBuyerByNip, this::costToBigDecimal, nip, startDate, endDate);
    taxDetail.put("Costs: ", costs.setScale(2, RoundingMode.HALF_UP));
    return costs;
  }

  public BigDecimal getProfit(String nip, LocalDate startDate, LocalDate endDate) {
    BigDecimal profit = getIncome(nip, startDate, endDate).subtract(getCosts(nip, startDate, endDate));
    taxDetail.put("Income - Costs: ", profit.setScale(2, RoundingMode.HALF_UP));
    return profit;
  }

  public BigDecimal sumOfPensionInsurance(String nip, LocalDate startDate, LocalDate endDate) {
    BigDecimal pensionInsurance = insuranceService.getInsurancesByIssueDateAndByCompany(startDate, endDate, nip).stream()
        .filter(insurance -> insurance.getType() == InsuranceType.PENSION).map(Insurance::getAmount)
        .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    taxDetail.put("Pension insurance: ", pensionInsurance.setScale(2, RoundingMode.HALF_UP));
    return pensionInsurance;
  }

  public BigDecimal getTaxDue(String nip, LocalDate startDate, LocalDate endDate) {
    return getValueFromInvoices(this::filterSellerByNip, this::incomeVatToBigDecimal, nip, startDate, endDate);
  }

  public BigDecimal getTaxIncluded(String nip, LocalDate startDate, LocalDate endDate) {
    return getValueFromInvoices(this::filterBuyerByNip, this::taxToBigDecimal, nip, startDate, endDate);
  }

  public BigDecimal getVatPayable(String nip, LocalDate startDate, LocalDate endDate) {
    return getTaxDue(nip, startDate, endDate).subtract(getTaxIncluded(nip, startDate, endDate));
  }

  public BigDecimal taxCalculationBaseBeforeRounding(String nip, LocalDate startDate, LocalDate endDate) {
    BigDecimal taxBaseBeforeRounding = getProfit(nip, startDate, endDate).subtract(sumOfPensionInsurance(nip, startDate, endDate));
    taxDetail.put("Income - Costs - Pension Insurance: ", taxBaseBeforeRounding.setScale(2, RoundingMode.HALF_UP));
    return taxBaseBeforeRounding;
  }

  public BigDecimal taxCalculationBaseRounded(String nip, LocalDate startDate, LocalDate endDate) {
    BigDecimal taxBase = taxCalculationBaseBeforeRounding(nip, startDate, endDate).setScale(0, RoundingMode.HALF_EVEN);
    taxDetail.put("Tax Base: ", taxBase.setScale(2, RoundingMode.HALF_UP));
    return taxBase;
  }

  public BigDecimal getIncomeTax(String nip, LocalDate startDate, LocalDate endDate) {
    BigDecimal incomeTax = BigDecimal.ZERO;
    if (companyService.getCompanyByNip(nip).get().getTaxType() == TaxType.LINEAR) {
      incomeTax = BigDecimal.valueOf(0.19).multiply(taxCalculationBaseRounded(nip, startDate, endDate));
    } else if (companyService.getCompanyByNip(nip).get().getTaxType() == TaxType.GRADED) {
      incomeTax = calculateGradedIncomeTax(nip, startDate, endDate);
    }
    taxDetail.put("Income Tax: ", incomeTax.setScale(2, RoundingMode.HALF_UP));
    return incomeTax;
  }

  public BigDecimal sumOfHealthInsurancesToSubstract(String nip, LocalDate startDate, LocalDate endDate) {
    BigDecimal healthInsurance = insuranceService.getInsurancesByIssueDateAndByCompany(startDate, endDate, nip).stream()
        .filter(insurance -> insurance.getType() == InsuranceType.HEALTH).map(Insurance::getAmount)
        .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    taxDetail.put("Health insurance: ", healthInsurance.setScale(2, RoundingMode.HALF_UP));
    BigDecimal healthInsuranceForReduce = healthInsurance
        .multiply(BigDecimal.valueOf(0.0775)) // todo would be good to extract those magic numbers to constants with readable names
        .divide(BigDecimal.valueOf(0.09), 2, RoundingMode.HALF_UP);
    taxDetail.put("Health Insurance For Reducing: ", healthInsuranceForReduce.setScale(2, RoundingMode.HALF_UP));
    return healthInsuranceForReduce;
  }

  public BigDecimal incomeTaxMinusHealthInsurance(String nip, LocalDate startDate, LocalDate endDate) {
    BigDecimal incomeTaxMinusHealthInsurance = getIncomeTax(nip, startDate, endDate)
        .subtract(sumOfHealthInsurancesToSubstract(nip, startDate, endDate));
    taxDetail.put("Income tax - health insurance: ", incomeTaxMinusHealthInsurance.setScale(2, RoundingMode.HALF_UP));
    return incomeTaxMinusHealthInsurance;
  }

  public BigDecimal finalIncomeTax(String nip, LocalDate startDate, LocalDate endDate) {
    BigDecimal finalIncomeTaxValue = incomeTaxMinusHealthInsurance(nip, startDate, endDate).setScale(2, RoundingMode.HALF_EVEN);
    taxDetail.put("Final Income Tax Value: ", finalIncomeTaxValue.setScale(2, RoundingMode.HALF_UP));
    return finalIncomeTaxValue;
  }

  public Map<String, BigDecimal> returnTaxCalculationDetails(String nip, LocalDate startDate, LocalDate endDate) {
    finalIncomeTax(nip, startDate, endDate);
    return taxDetail;
  }

  @JsonIgnore
  public BigDecimal getTotalNetValue(Invoice invoice) {
    BigDecimal netValue = BigDecimal.ZERO;
    for (InvoiceEntry entry : invoice.getEntries()) {
      netValue = netValue.add(getNetValueOfInvoiceEntry(entry, invoice));
    }
    return netValue;
  }

  @JsonIgnore
  public BigDecimal getCostValue(Invoice invoice) {
    BigDecimal netValue = BigDecimal.ZERO;
    for (InvoiceEntry entry : invoice.getEntries()) {
      if (entry.getCategory().equals("car") && invoice.getBuyer().getIsPersonalUsageOfCar() == true) {
        netValue = netValue
            .add(getNetValueOfInvoiceEntry(entry, invoice));
        netValue = netValue.add(netValue
            .multiply(entry.getVatRate().getVatRateValue()).divide((BigDecimal.valueOf(2)), 2, BigDecimal.ROUND_UP));
      } else {
        netValue = netValue.add(getNetValueOfInvoiceEntry(entry, invoice));
      }
    }
    return netValue;
  }

  @JsonIgnore
  public BigDecimal getVatValue(Invoice invoice) {
    BigDecimal vatValue = BigDecimal.ZERO;
    for (InvoiceEntry entry : invoice.getEntries()) {
      vatValue = vatValue.add((getNetValueOfInvoiceEntry(entry, invoice))
          .multiply(entry.getVatRate().getVatRateValue()));
    }
    return vatValue;
  }

  @JsonIgnore
  public BigDecimal getIncomeVatValue(Invoice invoice) {
    BigDecimal vatValue = BigDecimal.ZERO;
    for (InvoiceEntry entry : invoice.getEntries()) {
      if (entry.getCategory().equals("car")
          && invoice.getBuyer().getIsPersonalUsageOfCar() == true) { // todo it repeats in multiple places - maybe you should extract it to function?

        vatValue = vatValue.add((getNetValueOfInvoiceEntry(entry, invoice))
            .multiply(entry.getVatRate().getVatRateValue()).divide((BigDecimal.valueOf(2)), 2, BigDecimal.ROUND_DOWN));
      } else {
        vatValue = vatValue.add((getNetValueOfInvoiceEntry(entry, invoice))
            .multiply(entry.getVatRate().getVatRateValue()));
      }
    }
    return vatValue;
  }

  private BigDecimal calculateGradedIncomeTax(String nip, LocalDate startDate, LocalDate endDate) {
    BigDecimal incomeTax;
    incomeTax = taxCalculationBaseRounded(nip, startDate, endDate).compareTo(TAX_THRESHOLD) <= 0
        ? BigDecimal.valueOf(0.18).multiply(taxCalculationBaseRounded(nip, startDate, endDate))
        : (BigDecimal.valueOf(0.18).multiply(TAX_THRESHOLD)
            .add(taxCalculationBaseRounded(nip, startDate, endDate).subtract(TAX_THRESHOLD).multiply(BigDecimal.valueOf(0.32))));
    return incomeTax;
  }

  private BigDecimal getNetValueOfInvoiceEntry(InvoiceEntry entry, Invoice invoice) {
    return entry.getNetValueBeforeDiscount().multiply(BigDecimal.ONE.subtract(invoice.getBuyer().getDiscount()));
  }
}