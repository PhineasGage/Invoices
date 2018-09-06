package pl.coderstrust.accounting.logic;

import java.math.BigDecimal;
import java.util.function.BiPredicate;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderstrust.accounting.controller.NipValidator;
import pl.coderstrust.accounting.model.Invoice;

@Service
public class TaxCalculatorService {

  private InvoiceService invoiceService;
  private NipValidator nipValidator;

  @Autowired
  public TaxCalculatorService(InvoiceService invoiceService, NipValidator nipValidator) {
    this.invoiceService = invoiceService;
    this.nipValidator = nipValidator;
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
    return invoice.getVatValue();
  }

  private BigDecimal incomeToBigDecimal(Invoice invoice) {
    return invoice.getTotalNetValue();
  }

  public BigDecimal getIncome(String nip) {
    return getValueFromInvoices(this::filterSellerByNip, this::incomeToBigDecimal, nip);
  }

  public BigDecimal getCosts(String nip) {
    return getValueFromInvoices(this::filterBuyerByNip, this::incomeToBigDecimal, nip);
  }

  public BigDecimal getProfit(String nip) {
    return getIncome(nip).subtract(getCosts(nip));
  }

  public BigDecimal getTaxDue(String nip) {
    return getValueFromInvoices(this::filterSellerByNip, this::taxToBigDecimal, nip);
  }

  public BigDecimal getTaxIncluded(String nip) {
    return getValueFromInvoices(this::filterBuyerByNip, this::taxToBigDecimal, nip);
  }

  public BigDecimal getVatPayable(String nip) {
    return getTaxDue(nip).subtract(getTaxIncluded(nip));
  }
}