package pl.coderstrust.accounting.logic;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static pl.coderstrust.accounting.helpers.BigDecimalProvider.createBigDecimal;
import static pl.coderstrust.accounting.helpers.CompanyProvider.COMPANY_DRUKPOL;
import static pl.coderstrust.accounting.helpers.CompanyProvider.COMPANY_DRUTEX;
import static pl.coderstrust.accounting.helpers.CompanyProvider.COMPANY_TRANSPOL;
import static pl.coderstrust.accounting.helpers.CompanyProvider.COMPANY_WASBUD;
import static pl.coderstrust.accounting.helpers.InsuranceProvider.HEALTH_INSURANCE;
import static pl.coderstrust.accounting.helpers.InsuranceProvider.HEALTH_INSURANCE2;
import static pl.coderstrust.accounting.helpers.InsuranceProvider.HEALTH_INSURANCE3;
import static pl.coderstrust.accounting.helpers.InsuranceProvider.PENSION_INSURANCE;
import static pl.coderstrust.accounting.helpers.InsuranceProvider.PENSION_INSURANCE2;
import static pl.coderstrust.accounting.helpers.InsuranceProvider.PENSION_INSURANCE3;
import static pl.coderstrust.accounting.helpers.InvoiceProvider.INVOICE_DRUTEX_CAR_FUEL_ENTRY_2018;
import static pl.coderstrust.accounting.helpers.InvoiceProvider.INVOICE_DRUTEX_SPAN_CLAMP_SUPPORT_2018;
import static pl.coderstrust.accounting.helpers.InvoiceProvider.INVOICE_TRANSPOL_SPAN_CLAMP_SUPPORT_2016;
import static pl.coderstrust.accounting.helpers.InvoiceProvider.INVOICE_WASBUD_LINK_2018;
import static pl.coderstrust.accounting.helpers.InvoiceProvider.INVOICE_WASBUD_SPAN_CLAMP_2017;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.coderstrust.accounting.configuration.DatabaseProvider;
import pl.coderstrust.accounting.database.impl.hibernate.CompanyRepository;
import pl.coderstrust.accounting.database.impl.hibernate.InsuranceRepository;
import pl.coderstrust.accounting.database.impl.hibernate.InvoiceRepository;
import pl.coderstrust.accounting.helpers.CompanyProvider;

@RunWith(MockitoJUnitRunner.class)
public class TaxCalculatorServiceWithDatesTest {

  @Autowired
  InvoiceRepository invoiceRepository;

  @Autowired
  CompanyRepository companyRepository;

  @Autowired
  InsuranceRepository insuranceRepository;

  @Autowired
  JdbcTemplate jdbcTemplate;

  private InvoiceService invoiceService = new InvoiceService(DatabaseProvider.provideInvoiceDatabase(
      "InFileDatabase",
      "src/test/resources/test", invoiceRepository, jdbcTemplate));

  private CompanyService companyService = new CompanyService(DatabaseProvider.provideCompanyDatabase(
      "InFileDatabase",
      "src/test/resources/test", companyRepository, jdbcTemplate));

  private InsuranceService insuranceService = new InsuranceService(DatabaseProvider.provideInsuranceDatabase(
      "InFileDatabase",
      "src/test/resources/test", insuranceRepository, jdbcTemplate));

  private static final LocalDate START_DATE = LocalDate.of(2013, 1, 10);
  private static final LocalDate END_DATE = LocalDate.of(2018, 12, 31);

  private TaxCalculatorServiceWithDates taxCalculatorServiceWithDates = new TaxCalculatorServiceWithDates(invoiceService, companyService,
      insuranceService);

  @Before
  public void beforeMethod() {
    invoiceService.clearDatabase();
    companyService.clearDatabase();
    insuranceService.clearDatabase();
  }

  @Test
  public void shouldReturnZeroWhenNoInvoices() {
    //given

    //when
    BigDecimal actual = taxCalculatorServiceWithDates.getTaxDue(CompanyProvider.COMPANY_WASBUD.getNip(), START_DATE, END_DATE);

    //then
    assertThat(actual, is(BigDecimal.ZERO));
  }

  @Test
  public void shouldReturnIncome() {
    //given
    invoiceService.saveInvoice(INVOICE_DRUTEX_SPAN_CLAMP_SUPPORT_2018);
    invoiceService.saveInvoice(INVOICE_WASBUD_LINK_2018);

    //when
    BigDecimal actual = taxCalculatorServiceWithDates.getIncome(COMPANY_DRUTEX.getNip(), START_DATE, END_DATE).setScale(2, RoundingMode.HALF_UP);

    //then
    assertThat(actual, is(createBigDecimal(786.00)));
  }

  @Test
  public void shouldReturnCosts() {
    //given
    invoiceService.saveInvoice(INVOICE_DRUTEX_SPAN_CLAMP_SUPPORT_2018);
    invoiceService.saveInvoice(INVOICE_TRANSPOL_SPAN_CLAMP_SUPPORT_2016);

    //when
    BigDecimal actual = taxCalculatorServiceWithDates.getCosts(COMPANY_TRANSPOL.getNip(), START_DATE, END_DATE).setScale(2, RoundingMode.HALF_UP);

    //then
    assertThat(actual, is(createBigDecimal(786.00)));
  }

  @Test
  public void shouldReturnCostsForCompanyWithPersonalCarUsage() {
    //given
    invoiceService.saveInvoice(INVOICE_DRUTEX_CAR_FUEL_ENTRY_2018);

    //when
    BigDecimal actual = taxCalculatorServiceWithDates.getCosts(COMPANY_DRUTEX.getNip(), START_DATE, END_DATE).setScale(2, RoundingMode.HALF_UP);

    //then
    assertThat(actual, is(createBigDecimal(107.64)));
  }

  @Test
  public void shouldReturnVatDue() {
    //given
    invoiceService.saveInvoice(INVOICE_WASBUD_LINK_2018);
    invoiceService.saveInvoice(INVOICE_WASBUD_SPAN_CLAMP_2017);

    //when
    BigDecimal actual = taxCalculatorServiceWithDates.getTaxDue(COMPANY_WASBUD.getNip(), START_DATE, END_DATE).setScale(2, RoundingMode.HALF_UP);

    //then
    assertThat(actual, is(createBigDecimal(120.29)));
  }

  @Test
  public void shouldReturnVatIncluded() {
    //given
    invoiceService.saveInvoice(INVOICE_WASBUD_LINK_2018);
    invoiceService.saveInvoice(INVOICE_WASBUD_SPAN_CLAMP_2017);
    invoiceService.saveInvoice(INVOICE_TRANSPOL_SPAN_CLAMP_SUPPORT_2016);

    // when
    BigDecimal actual = taxCalculatorServiceWithDates.getTaxIncluded(COMPANY_DRUKPOL.getNip(), START_DATE, END_DATE)
        .setScale(2, RoundingMode.HALF_UP);

    // then
    assertThat(actual, is(createBigDecimal(233.69)));
  }

  @Test
  public void shouldReturnTaxBaseBeforeRounding() {
    //given
    for (int i = 0; i < 5; i++) {
      invoiceService.saveInvoice(INVOICE_TRANSPOL_SPAN_CLAMP_SUPPORT_2016);
    }

    String nip = COMPANY_TRANSPOL.getNip();
    insuranceService.saveInsurance(nip, PENSION_INSURANCE);
    insuranceService.saveInsurance(nip, PENSION_INSURANCE2);

    //when
    BigDecimal actual = taxCalculatorServiceWithDates.taxCalculationBaseBeforeRounding(COMPANY_TRANSPOL.getNip(), START_DATE, END_DATE)
        .setScale(2, RoundingMode.HALF_UP);

    //then
    assertThat(actual, is(createBigDecimal(1551.00)));
  }

  @Test
  public void shouldReturnSumOfPensionInsurances() {
    //given
    String nip = COMPANY_TRANSPOL.getNip();
    insuranceService.saveInsurance(nip, PENSION_INSURANCE);
    insuranceService.saveInsurance(nip, PENSION_INSURANCE2);

    //when
    BigDecimal actual = taxCalculatorServiceWithDates.sumOfPensionInsurance(COMPANY_TRANSPOL.getNip(), START_DATE, END_DATE)
        .setScale(2, RoundingMode.HALF_UP);

    //then
    assertThat(actual, is(createBigDecimal(1200.00)));
  }

  @Test
  public void shouldReturnSumOfHealthInsurances() {
    //given
    String nip = COMPANY_DRUTEX.getNip();
    insuranceService.saveInsurance(nip, HEALTH_INSURANCE);
    insuranceService.saveInsurance(nip, HEALTH_INSURANCE2);

    //when
    BigDecimal actual = taxCalculatorServiceWithDates.sumOfHealthInsurancesToSubstract(COMPANY_DRUTEX.getNip(), START_DATE, END_DATE);

    //then
    assertThat(actual, is(createBigDecimal(1033.33)));
  }

  @Test
  public void shouldReturnProfit() {
    //given
    invoiceService.saveInvoice(INVOICE_TRANSPOL_SPAN_CLAMP_SUPPORT_2016);

    //when
    BigDecimal actual = taxCalculatorServiceWithDates.getProfit(COMPANY_TRANSPOL.getNip(), START_DATE, END_DATE).setScale(2, RoundingMode.HALF_UP);

    //then
    assertThat(actual, is(createBigDecimal(550.20)));
  }

  @Test
  public void shouldCalculateNetValue() {
    //when
    BigDecimal actual = taxCalculatorServiceWithDates.getTotalNetValue(INVOICE_WASBUD_SPAN_CLAMP_2017).setScale(2, RoundingMode.HALF_UP);
    BigDecimal expected = createBigDecimal(504.00);

    //then
    assertThat(actual, is(expected));
  }

  @Test
  public void shoulCalculateFinalIncomeTaxForIncomeBelowTaxThreshold() {
    //given
    invoiceService.saveInvoice(INVOICE_WASBUD_SPAN_CLAMP_2017);
    invoiceService.saveInvoice(INVOICE_WASBUD_SPAN_CLAMP_2017);
    invoiceService.saveInvoice(INVOICE_WASBUD_SPAN_CLAMP_2017);


    //when
    BigDecimal actual = taxCalculatorServiceWithDates.finalIncomeTax(COMPANY_WASBUD.getNip(), START_DATE, END_DATE);

    //then
    assertThat(actual, is(createBigDecimal(272.16))); //todo check calculation again
  }

  @Test
  public void shouldCalculateFinalIncomeTaxForIncomeHigherThanTaxThreshold() {
    //given
    for (int i = 0; i < 200; i++) {
      invoiceService.saveInvoice(INVOICE_WASBUD_SPAN_CLAMP_2017);
    }
    String nip = COMPANY_WASBUD.getNip();
    insuranceService.saveInsurance(nip, HEALTH_INSURANCE2);
    insuranceService.saveInsurance(nip, HEALTH_INSURANCE3);
    insuranceService.saveInsurance(nip, PENSION_INSURANCE3);

    //when
    BigDecimal actual = taxCalculatorServiceWithDates.finalIncomeTax(COMPANY_WASBUD.getNip(), START_DATE, END_DATE).setScale(2);

    //then
    assertThat(actual, is(createBigDecimal(18798.41).setScale(2)));
  }

  @Test
  public void shouldCalculateFinalIncomeTaxForLinearTaxType() {
    //given
    for (int i = 0; i < 10; i++) {
      invoiceService.saveInvoice(INVOICE_TRANSPOL_SPAN_CLAMP_SUPPORT_2016);
    }
    String nip = COMPANY_TRANSPOL.getNip();
    insuranceService.saveInsurance(nip, PENSION_INSURANCE);
    insuranceService.saveInsurance(nip, PENSION_INSURANCE2);

    //when
    BigDecimal actual = taxCalculatorServiceWithDates.finalIncomeTax(COMPANY_TRANSPOL.getNip(), START_DATE, END_DATE).setScale(2);

    //then
    assertThat(actual, is(createBigDecimal(817.38).setScale(2)));
  }
}