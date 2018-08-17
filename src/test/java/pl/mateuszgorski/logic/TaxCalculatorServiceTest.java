package pl.mateuszgorski.logic;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static pl.mateuszgorski.helpers.InvoiceProvider.INVOICE_CHELMNO_2016;
import static pl.mateuszgorski.helpers.InvoiceProvider.INVOICE_GRUDZIADZ_2017;
import static pl.mateuszgorski.helpers.InvoiceProvider.INVOICE_KRAKOW_2018;
import static pl.mateuszgorski.helpers.InvoiceProvider.INVOICE_RADOMSKO_2018;

import java.io.IOException;
import java.math.BigDecimal;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import pl.mateuszgorski.controller.NipValidator;
import pl.mateuszgorski.database.impl.memory.InMemoryDatabase;

public class TaxCalculatorServiceTest {

  // TODO do not hardcode values - use constants from InvoiceTestProvider
  private String drukpolNip = "5311688030";
  private String wasbudNip = "6271206366";
  private String drutexNip = "8421622720";
  private String transpolNip = "5621760000";

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private InvoiceService invoiceService = new InvoiceService(new InMemoryDatabase());
  private TaxCalculatorService taxCalculatorService = new TaxCalculatorService(invoiceService, new NipValidator());

  @Test
  public void shouldReturnZeroWhenNoInvoices() throws IOException {
    //given

    //when
    BigDecimal actual = taxCalculatorService
        .getValueFromInvoices(taxCalculatorService::biFilterBuyer,
            taxCalculatorService::taxToBigDecimal, wasbudNip);

    //then
    assertThat(actual, is(BigDecimal.ZERO));
  }

  @Test
  public void shouldReturnIllegalArgumentExceptionWhenNipIsInvalid() throws IOException {
    //given
    String incorrectNip = "1234567890";

    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Nip does not match specified pattern");

    // TODO // when // then missing
    BigDecimal actual = taxCalculatorService
        .getValueFromInvoices(taxCalculatorService::biFilterBuyer,
            taxCalculatorService::taxToBigDecimal, incorrectNip);
  }

  @Test
  public void shouldReturnIncome() throws IOException {
    //given
    invoiceService.saveInvoice(INVOICE_KRAKOW_2018);
    invoiceService.saveInvoice(INVOICE_RADOMSKO_2018);

    //when
    BigDecimal actual = taxCalculatorService
        .getValueFromInvoices(taxCalculatorService::biFilterSeller,
            taxCalculatorService::incomeToBigDecimal, drutexNip);

    //then
    assertThat(actual, is(BigDecimal.valueOf(138.0)));
  }

  @Test
  public void shouldReturnCosts() throws IOException {
    //given
    invoiceService.saveInvoice(INVOICE_KRAKOW_2018);
    invoiceService.saveInvoice(INVOICE_CHELMNO_2016);

    //when
    BigDecimal actual = taxCalculatorService
        .getValueFromInvoices(taxCalculatorService::biFilterBuyer,
            taxCalculatorService::incomeToBigDecimal, transpolNip);

    //then
    assertThat(actual, is(BigDecimal.valueOf(138.0)));
  }

  @Test
  public void shouldReturnVatDue() throws IOException {
    //given
    invoiceService.saveInvoice(INVOICE_RADOMSKO_2018);
    invoiceService.saveInvoice(INVOICE_GRUDZIADZ_2017);

    //when
    BigDecimal actual = taxCalculatorService
        .getValueFromInvoices(taxCalculatorService::biFilterSeller,
            taxCalculatorService::taxToBigDecimal, wasbudNip);

    //then
    assertThat(actual, is(BigDecimal.valueOf(11.592)));
  }

  @Test
  public void shouldReturnVatIncluded() throws IOException {
    //given
    invoiceService.saveInvoice(INVOICE_RADOMSKO_2018);
    invoiceService.saveInvoice(INVOICE_GRUDZIADZ_2017);
    invoiceService.saveInvoice(INVOICE_CHELMNO_2016);

    // when
    BigDecimal actual = taxCalculatorService
        .getValueFromInvoices(taxCalculatorService::biFilterBuyer,
            taxCalculatorService::taxToBigDecimal, drukpolNip);

    // then
    assertThat(actual, is(BigDecimal.valueOf(25.032)));
  }
}