package pl.coderstrust.accounting.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.coderstrust.accounting.logic.TaxCalculatorServiceWithDates;

@Api(value = "/taxcalculatorDates", description = "Operations regarding tax calculation")
@RequestMapping("/taxcalculatorDates")
@RestController
public class TaxCalculatorControllerWithDates {

  @Autowired
  private TaxCalculatorServiceWithDates taxCalculatorServiceWithDates;

  @Autowired
  public TaxCalculatorControllerWithDates(TaxCalculatorServiceWithDates taxCalculatorServiceWithDates) {
  }

  @ApiOperation(value = "Gets income by company",
      notes = "Returns invoice value where company specified is the seller",
      response = BigDecimal.class)
  @GetMapping("/income/{nip}")
  public BigDecimal getIncome(@PathVariable(name = "nip", required = true) String nip,
      @RequestParam(name = "startDate", required = true)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam(name = "endDate", required = true)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
    return taxCalculatorServiceWithDates.getIncome(nip, startDate, endDate).setScale(2, BigDecimal.ROUND_HALF_UP);
  }

  @ApiOperation(value = "Gets tax due",
      notes = "Tax due is the tax where company specified is the seller",
      response = BigDecimal.class)
  @GetMapping("/TaxDue/{nip}")
  public BigDecimal getTaxDue(@PathVariable(name = "nip", required = true) String nip,
      @RequestParam(name = "startDate", required = true)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam(name = "endDate", required = true)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
    return taxCalculatorServiceWithDates.getTaxDue(nip, startDate, endDate).setScale(2, BigDecimal.ROUND_HALF_UP);
  }

  @ApiOperation(value = "Gets tax included",
      notes = "Tax due is the tax where company specified is the buyer",
      response = BigDecimal.class)
  @GetMapping("/TaxIncluded/{nip}")
  public BigDecimal getTaxIncluded(@PathVariable(name = "nip", required = true) String nip,
      @RequestParam(name = "startDate", required = true)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam(name = "endDate", required = true)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
    return taxCalculatorServiceWithDates.getTaxIncluded(nip, startDate, endDate).setScale(2, BigDecimal.ROUND_HALF_UP);
  }

  @ApiOperation(value = "Gets costs by company",
      notes = "Returns invoice value where company specified is the buyer",
      response = BigDecimal.class)
  @GetMapping("/Costs/{nip}")
  public BigDecimal getCosts(@PathVariable(name = "nip", required = true) String nip,
      @RequestParam(name = "startDate", required = true)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam(name = "endDate", required = true)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
    return taxCalculatorServiceWithDates.getCosts(nip, startDate, endDate).setScale(2, BigDecimal.ROUND_HALF_UP);
  }

  @ApiOperation(value = "Gets profit = income - costs",
      notes = "Substracts Costs from Income for company specified",
      response = BigDecimal.class)
  @GetMapping("/Profit/{nip}")
  public BigDecimal getProfit(@PathVariable(name = "nip", required = true) String nip,
      @RequestParam(name = "startDate", required = true)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam(name = "endDate", required = true)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
    return taxCalculatorServiceWithDates.getProfit(nip, startDate, endDate).setScale(2, BigDecimal.ROUND_HALF_UP);

  }

  @ApiOperation(value = "Gets tax payable",
      notes = "Substracts tax included from tax due for specified company",
      response = BigDecimal.class)
  @GetMapping("/VatPayable/{nip}")
  public BigDecimal getVatPayable(@PathVariable(name = "nip", required = true) String nip,
      @RequestParam(name = "startDate", required = true)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam(name = "endDate", required = true)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
    return taxCalculatorServiceWithDates.getVatPayable(nip, startDate, endDate).setScale(2, BigDecimal.ROUND_HALF_UP);
  }

  @ApiOperation(value = "Gets final income taxes",
      notes = "Calculate final value of income taxes",
      response = BigDecimal.class)
  @GetMapping("/FinalIncomeTaxes/{nip}")
  public BigDecimal getFinalIncomeTaxes(@PathVariable(name = "nip", required = true) String nip,
      @RequestParam(name = "startDate", required = true)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam(name = "endDate", required = true)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
    return taxCalculatorServiceWithDates.finalIncomeTax(nip, startDate, endDate).setScale(2, BigDecimal.ROUND_HALF_UP);
  }

  @ApiOperation(value = "Gets Income Taxes details",
      notes = "Display all calculated components of the Income Taxes",
      response = Map.class)
  @GetMapping("/IncomeTaxesDetails/{nip}")
  public Map<String, BigDecimal> getIncomTaxesDetails(@PathVariable(name = "nip", required = true) String nip,
      @RequestParam(name = "startDate", required = true)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam(name = "endDate", required = true)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
    return taxCalculatorServiceWithDates.returnTaxCalculationDetails(nip, startDate, endDate);
  }
}