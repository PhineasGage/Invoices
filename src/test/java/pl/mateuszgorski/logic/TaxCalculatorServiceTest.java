package pl.mateuszgorski.logic;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static pl.mateuszgorski.helpers.CompanyProvider.COMPANY_DRUKPOL;
import static pl.mateuszgorski.helpers.CompanyProvider.COMPANY_DRUTEX;
import static pl.mateuszgorski.helpers.CompanyProvider.COMPANY_TRANSPOL;
import static pl.mateuszgorski.helpers.CompanyProvider.COMPANY_WASBUD;
import static pl.mateuszgorski.helpers.InvoiceProvider.INVOICE_DRUTEX_SPAN_CLAMP_SUPPORT_2018;
import static pl.mateuszgorski.helpers.InvoiceProvider.INVOICE_TRANSPOL_SPAN_CLAMP_SUPPORT_2016;
import static pl.mateuszgorski.helpers.InvoiceProvider.INVOICE_WASBUD_LINK_2018;
import static pl.mateuszgorski.helpers.InvoiceProvider.INVOICE_WASBUD_SPAN_CLAMP_2017;

import java.math.BigDecimal;
import org.junit.Test;
import pl.mateuszgorski.controller.NipValidator;
import pl.mateuszgorski.database.impl.memory.InMemoryDatabase;
import pl.mateuszgorski.helpers.CompanyProvider;

public class TaxCalculatorServiceTest {

  private InvoiceService invoiceService = new InvoiceService(new InMemoryDatabase());
  private TaxCalculatorService taxCalculatorService = new TaxCalculatorService(invoiceService, new NipValidator());

  @Test
  public void shouldReturnZeroWhenNoInvoices() {
    //given

    //when
    BigDecimal actual = taxCalculatorService.getTaxDue(CompanyProvider.COMPANY_WASBUD.getNip());

    //then
    assertThat(actual, is(BigDecimal.ZERO));
  }

  @Test
  public void shouldReturnIncome() {
    //given
    invoiceService.saveInvoice(INVOICE_DRUTEX_SPAN_CLAMP_SUPPORT_2018);
    invoiceService.saveInvoice(INVOICE_WASBUD_LINK_2018);

    //when
    BigDecimal actual = taxCalculatorService.getIncome(COMPANY_DRUTEX.getNip());

    //then
    assertThat(actual, is(BigDecimal.valueOf(138.0)));
  }

  @Test
  public void shouldReturnCosts() {
    //given
    invoiceService.saveInvoice(INVOICE_DRUTEX_SPAN_CLAMP_SUPPORT_2018);
    invoiceService.saveInvoice(INVOICE_TRANSPOL_SPAN_CLAMP_SUPPORT_2016);

    //when
    BigDecimal actual = taxCalculatorService.getCosts(COMPANY_TRANSPOL.getNip());

    //then
    assertThat(actual, is(BigDecimal.valueOf(138.0)));
  }

  @Test
  public void shouldReturnVatDue() {
    //given
    invoiceService.saveInvoice(INVOICE_WASBUD_LINK_2018);
    invoiceService.saveInvoice(INVOICE_WASBUD_SPAN_CLAMP_2017);

    //when
    BigDecimal actual = taxCalculatorService.getTaxDue(COMPANY_WASBUD.getNip()).setScale(2);

    //then
    assertThat(actual, is(BigDecimal.valueOf(14.70).setScale(2)));
  }

  @Test
  public void shouldReturnVatIncluded() {
    //given
    invoiceService.saveInvoice(INVOICE_WASBUD_LINK_2018);
    invoiceService.saveInvoice(INVOICE_WASBUD_SPAN_CLAMP_2017);
    invoiceService.saveInvoice(INVOICE_TRANSPOL_SPAN_CLAMP_SUPPORT_2016);

    // when
    BigDecimal actual = taxCalculatorService
        .getTaxIncluded(COMPANY_DRUKPOL.getNip());

    // then
    assertThat(actual, is(BigDecimal.valueOf(22.512)));
  }
}