package pl.coderstrust.accounting.logic;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static pl.coderstrust.accounting.helpers.BigDecimalProvider.createBigDecimal;
import static pl.coderstrust.accounting.helpers.CompanyProvider.COMPANY_DRUKPOL;
import static pl.coderstrust.accounting.helpers.CompanyProvider.COMPANY_DRUTEX;
import static pl.coderstrust.accounting.helpers.CompanyProvider.COMPANY_TRANSPOL;
import static pl.coderstrust.accounting.helpers.CompanyProvider.COMPANY_WASBUD;
import static pl.coderstrust.accounting.helpers.InvoiceProvider.INVOICE_DRUTEX_SPAN_CLAMP_SUPPORT_2018;
import static pl.coderstrust.accounting.helpers.InvoiceProvider.INVOICE_TRANSPOL_SPAN_CLAMP_SUPPORT_2016;
import static pl.coderstrust.accounting.helpers.InvoiceProvider.INVOICE_WASBUD_LINK_2018;
import static pl.coderstrust.accounting.helpers.InvoiceProvider.INVOICE_WASBUD_SPAN_CLAMP_2017;

import java.math.BigDecimal;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.coderstrust.accounting.configuration.DatabaseProvider;
import pl.coderstrust.accounting.controller.NipValidator;
import pl.coderstrust.accounting.database.impl.hibernate.InvoiceRepository;

public class TaxCalculatorServiceTest {

  @Autowired
  InvoiceRepository invoiceRepository;

  @Autowired
  JdbcTemplate jdbcTemplate;

  private InvoiceService invoiceService = new InvoiceService(
      DatabaseProvider.provideInvoiceDatabase(
          "InFileDatabase", "src/test/resources/test", invoiceRepository, jdbcTemplate));

  private TaxCalculatorService taxCalculatorService = new TaxCalculatorService(invoiceService, new NipValidator());

  @Before
  public void beforeMethod() {
    invoiceService.clearDatabase();
  }

  @Test
  public void shouldReturnZeroWhenNoInvoices() {
    //given

    //when
    BigDecimal actual = taxCalculatorService.getTaxDue(COMPANY_WASBUD.getNip()).setScale(2, BigDecimal.ROUND_HALF_UP);

    //then
    assertThat(actual, is(createBigDecimal(0)));
  }

  @Test
  public void shouldReturnIncome() {
    //given
    invoiceService.saveInvoice(INVOICE_DRUTEX_SPAN_CLAMP_SUPPORT_2018);
    invoiceService.saveInvoice(INVOICE_WASBUD_LINK_2018);

    //when
    BigDecimal actual = taxCalculatorService.getIncome(COMPANY_DRUTEX.getNip()).setScale(2, BigDecimal.ROUND_HALF_UP);

    //then
    assertThat(actual, is(createBigDecimal(138)));
  }

  @Test
  public void shouldReturnCosts() {
    //given
    invoiceService.saveInvoice(INVOICE_DRUTEX_SPAN_CLAMP_SUPPORT_2018);
    invoiceService.saveInvoice(INVOICE_TRANSPOL_SPAN_CLAMP_SUPPORT_2016);

    //when
    BigDecimal actual = taxCalculatorService.getCosts(COMPANY_TRANSPOL.getNip()).setScale(2, BigDecimal.ROUND_HALF_UP);

    //then
    assertThat(actual, is(createBigDecimal(138)));
  }

  @Test
  public void shouldReturnVatDue() {
    //given
    invoiceService.saveInvoice(INVOICE_WASBUD_LINK_2018);
    invoiceService.saveInvoice(INVOICE_WASBUD_SPAN_CLAMP_2017);

    //when
    BigDecimal actual = taxCalculatorService.getTaxDue(COMPANY_WASBUD.getNip()).setScale(2, BigDecimal.ROUND_HALF_UP);

    //then
    assertThat(actual, is(createBigDecimal(15.96)));

  }

  @Test
  public void shouldReturnVatIncluded() {
    //given
    invoiceService.saveInvoice(INVOICE_WASBUD_LINK_2018);
    invoiceService.saveInvoice(INVOICE_WASBUD_SPAN_CLAMP_2017);
    invoiceService.saveInvoice(INVOICE_TRANSPOL_SPAN_CLAMP_SUPPORT_2016);

    // when
    BigDecimal actual = taxCalculatorService.getTaxIncluded(COMPANY_DRUKPOL.getNip()).setScale(2, BigDecimal.ROUND_HALF_UP);

    //then
    assertThat(actual, is(createBigDecimal(25.032)));
  }
}