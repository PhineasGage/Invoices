package pl.coderstrust.accounting.logic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderstrust.accounting.controller.NipValidator;
import pl.coderstrust.accounting.model.Insurance;
import pl.coderstrust.accounting.model.InsuranceType;
import pl.coderstrust.accounting.model.Invoice;
import pl.coderstrust.accounting.model.InvoiceEntry;
import pl.coderstrust.accounting.model.TaxType;

@Service
public class TaxCalculatorService {

  private InvoiceService invoiceService;
  private CompanyService companyService;
  private InsuranceService insuranceService;

  private static BigDecimal TAX_THRESHOLD = BigDecimal.valueOf(85528); //todo keep this value in database/properties file

  @Autowired
  public TaxCalculatorService(InvoiceService invoiceService,
                              CompanyService companyService,
                              InsuranceService insuranceService) {
    this.invoiceService = invoiceService;
    this.companyService = companyService;
    this.insuranceService = insuranceService;
  }

  private BigDecimal getValueFromInvoices(BiPredicate<Invoice, String> buyerOrSeller, Function<Invoice, BigDecimal> taxOrIncomeToBigDecimal,
      String nip) {

    return invoiceService
        .getInvoices()
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

  public BigDecimal getIncome(String nip) {
    BigDecimal income = getValueFromInvoices(this::filterSellerByNip, this::incomeToBigDecimal, nip);
    taxDetail.put("Income: ", income);
    return income;
  }

  public BigDecimal getCosts(String nip) {
    BigDecimal costs = getValueFromInvoices(this::filterBuyerByNip, this::costToBigDecimal, nip);
    taxDetail.put("Costs: ", costs);
    return costs;
  }

  public BigDecimal getProfit(String nip) {
    BigDecimal profit = getIncome(nip).subtract(getCosts(nip));
    taxDetail.put("Income - Costs: ", profit);
    return profit;
  }

  public BigDecimal sumOfPensionInsurance(String nip) {
    BigDecimal pensionInsurance = insuranceService
        .getAllInsurancesByCompany(nip)
        .stream()
        .filter(insurance -> insurance.getType() == InsuranceType.PENSION)
        .map(Insurance::getAmount)
        .reduce(BigDecimal::add)
        .orElse(BigDecimal.ZERO);
    taxDetail.put("Pension insurance: ", pensionInsurance);
    return pensionInsurance;
  }

  public BigDecimal getTaxDue(String nip) {
    return getValueFromInvoices(this::filterSellerByNip, this::incomeVatToBigDecimal, nip);
  }

  public BigDecimal getTaxIncluded(String nip) {
    return getValueFromInvoices(this::filterBuyerByNip, this::taxToBigDecimal, nip);
  }

  public BigDecimal getVatPayable(String nip) {
    return getTaxDue(nip).subtract(getTaxIncluded(nip));
  }

  public BigDecimal taxCalculationBaseBeforeRounding(String nip) {
    BigDecimal taxBaseBeforeRounding = getProfit(nip).subtract(sumOfPensionInsurance(nip));
    taxDetail.put("Income - Costs - Pension Insurance: ", taxBaseBeforeRounding);
    return taxBaseBeforeRounding;
  }

  public BigDecimal taxCalculationBaseRounded(String nip) {
    BigDecimal taxBase = taxCalculationBaseBeforeRounding(nip).setScale(0, RoundingMode.HALF_EVEN);
    taxDetail.put("Tax Base: ", taxBase);
    return taxBase;
  }

  public BigDecimal getIncomeTax(String nip) {
    BigDecimal incomeTax = BigDecimal.ZERO;
    if (companyService.getCompanyByNip(nip).get().getTaxType() == TaxType.LINEAR) {
      incomeTax = BigDecimal.valueOf(0.19).multiply(taxCalculationBaseRounded(nip));
    } else if (companyService.getCompanyByNip(nip).get().getTaxType() == TaxType.GRADED) {
      incomeTax = taxCalculationBaseRounded(nip).compareTo(TAX_THRESHOLD) <= 0
          ? BigDecimal.valueOf(0.18).multiply(taxCalculationBaseRounded(nip))
          : (BigDecimal.valueOf(0.18).multiply(TAX_THRESHOLD)
              .add(taxCalculationBaseRounded(nip).subtract(TAX_THRESHOLD).multiply(BigDecimal.valueOf(0.32))));
    }
    taxDetail.put("Income Tax: ", incomeTax);
    return incomeTax;
  }

  public BigDecimal sumOfHealthInsurancesToSubstract(String nip) {
    BigDecimal healthInsurance = insuranceService.getAllInsurancesByCompany(nip).stream()
        .filter(insurance -> insurance.getType() == InsuranceType.HEALTH).map(Insurance::getAmount)
        .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    taxDetail.put("Health insurance: ", healthInsurance);
    BigDecimal healthInsuranceForReduce = healthInsurance.multiply(BigDecimal.valueOf(0.0775))
        .divide(BigDecimal.valueOf(0.09), 2, RoundingMode.HALF_UP);
    taxDetail.put("Health Insurance For Reducing: ", healthInsuranceForReduce);
    return healthInsuranceForReduce;
  }

  public BigDecimal incomeTaxMinusHealthInsurance(String nip) {
    BigDecimal incomeTaxMinusHealthInsurance = getIncomeTax(nip).subtract(sumOfHealthInsurancesToSubstract(nip));
    taxDetail.put("Income tax - health insurance: ", incomeTaxMinusHealthInsurance);
    return incomeTaxMinusHealthInsurance;
  }

  public BigDecimal finalIncomeTax(String nip) {
    BigDecimal finalIncomeTaxValue = incomeTaxMinusHealthInsurance(nip).setScale(2, RoundingMode.HALF_EVEN);
    taxDetail.put("Final Income Tax Value: ", finalIncomeTaxValue);
    return finalIncomeTaxValue;
  }

  public Map<String, BigDecimal> returnTaxCalculationDetails(String nip) {
    finalIncomeTax(nip);
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
      if (entry.getCategory().equals("car") && invoice.getBuyer().getIsPersonalUsageOfCar() == true) {
        vatValue = vatValue.add((getNetValueOfInvoiceEntry(entry, invoice))
            .multiply(entry.getVatRate().getVatRateValue()).divide((BigDecimal.valueOf(2)), 2, BigDecimal.ROUND_DOWN));
      } else {
        vatValue = vatValue.add((getNetValueOfInvoiceEntry(entry, invoice))
            .multiply(entry.getVatRate().getVatRateValue()));
      }
    }
    return vatValue;
  }

  private BigDecimal calculateGradedIncomeTax(String nip) {
    BigDecimal incomeTax;
    incomeTax = taxCalculationBaseRounded(nip).compareTo(TAX_THRESHOLD) <= 0
        ? BigDecimal.valueOf(0.18).multiply(taxCalculationBaseRounded(nip))
        : (BigDecimal.valueOf(0.18).multiply(TAX_THRESHOLD)
            .add(taxCalculationBaseRounded(nip).subtract(TAX_THRESHOLD).multiply(BigDecimal.valueOf(0.32))));
    return incomeTax;
  }

  private BigDecimal getNetValueOfInvoiceEntry(InvoiceEntry entry, Invoice invoice) {
    return entry.getNetValueBeforeDiscount().multiply(BigDecimal.ONE.subtract(invoice.getBuyer().getDiscount()));
  }
}